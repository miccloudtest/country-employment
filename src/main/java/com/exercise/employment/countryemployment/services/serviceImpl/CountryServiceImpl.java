package com.exercise.employment.countryemployment.services.serviceImpl;

import com.exercise.employment.countryemployment.Cache.CountryCacheInitializer;
import com.exercise.employment.countryemployment.beans.*;
import com.exercise.employment.countryemployment.repositories.repository.CountryRepository;
import com.exercise.employment.countryemployment.services.service.CountryService;
import com.exercise.employment.countryemployment.services.service.PoiReaderService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
    @Autowired
    private PoiReaderService poiReaderService;
    @Value("${app.file.upload.success.message}")
    public String SUCCESS_MSG;
    @Value("${app.file.type.error.message}")
    public String FILE_ERR_MSG;
    @Value("${app.file.upload.partial.success.message}")
    public String PARTIAL_SUCCESS_MSG;

    @Value("${app.file.upload.fail.message}")
    public String FILE_UPLOAD_FAIL_MSG;
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    CountryCacheInitializer stateCacheIntializer;

    Predicate<MultipartFile> isXls = file -> file.getOriginalFilename().toLowerCase().endsWith("xls");

    Predicate<MultipartFile> isXlsx = file -> file.getOriginalFilename().toLowerCase().endsWith("xlsx");

    Predicate<Long> isNotNull = var -> var != null && var != 0;

    BiFunction<String, String, CountryStateIDMaster> getCountryStateMapping = (countryName, stateName) -> {
        List<CountryStateIDMaster> states = stateCacheIntializer.countryStateCache.get(countryName.trim());
        CountryStateIDMaster countryStateMaster = null;
        if (states != null) {
            countryStateMaster = states.stream().filter(state -> state.getStateName().trim().equalsIgnoreCase(stateName.trim())).findAny().orElse(null);
        }
        return countryStateMaster;
    };
    BiPredicate<String, String> isValidCountryState = (country, state) -> {
        CountryStateIDMaster countryState = getCountryStateMapping.apply(country, state);
        return countryState != null ? countryState.getStateName().equalsIgnoreCase(state.trim()) : false;
    };


    Function<MultipartFile, Workbook> getWorkBook = file -> {
        Workbook workbook = null;
        try {
            if (isXlsx.test(file)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (isXls.test(file)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }
        } catch (Exception ex) {
            logger.error("The specified file is not Excel file");
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    };


    private ExcelRecord processExcelData(List<CountryData> excelRecords, User user) throws Exception {
        List<CountryData> validList = excelRecords.stream().filter(record -> isNotNull.test(record.getStateNumOfEmployed()) && isValidCountryState.test(record.getCountryName().trim(), record.getStateName().trim())).
                map(record -> {
                    record.setEmploymentRate((double) record.getStatePopulation() / record.getStateNumOfEmployed());
                    record.setCreated_by(user.getEmail());
                    record.setModified_by(user.getEmail());
                    record.setCreated_ts(new Date());
                    record.setModified_ts(new Date());
                    CountryStateIDMaster countryStateMaster = getCountryStateMapping.apply(record.getCountryName(), record.getStateName());
                    record.setStateId(countryStateMaster.getStateId());
                    record.setCountryId(countryStateMaster.getCountryId());
                    return record;
                }).collect(Collectors.toList());
        List<CountryData> invalidList = excelRecords.stream()
                .filter(record -> !(isNotNull.test(record.getStateNumOfEmployed()) && isValidCountryState.test(record.getCountryName().trim(), record.getStateName().trim())))
                .collect(Collectors.toList());
        Set<Integer> stateName = validList.stream().map(CountryData::getStateId).collect(Collectors.toSet());
        ExcelRecord excelRecord = ExcelRecord.builder().validRecords(validList).inValidRecords(invalidList).build();
        doListPartition(validList, stateName, excelRecord);
        return excelRecord;
    }

    private void doListPartition(List<CountryData> insertRecord, Set<Integer> stateName, ExcelRecord excelRecord) {
        Map<Integer, Integer> existCountryStateIdMap = countryRepository.getExistingCountryStateId(stateName);
        Map<Boolean, List<CountryData>> recordList = insertRecord.stream()
                .collect(Collectors.groupingBy(record -> existCountryStateIdMap.containsKey(record.getStateId())));
        List<CountryData> updateRecords = recordList.get(true);
        List<CountryData> insertRecords = recordList.get(false);
        if (insertRecords != null) {
            excelRecord.setInsertRecords(insertRecords);
        }
        if (updateRecords != null) {
            excelRecord.setUpdateRecords(updateRecords);
        }

    }


    @Override
    public ResponseMessage processFile(MultipartFile multipartFile, User user) throws Exception {
        ResponseMessage response = null;
        if (isXls.or(isXlsx).test(multipartFile)) {
            List<CountryData> list = poiReaderService.readFile(multipartFile.getInputStream(), CountryData.class);
            ExcelRecord excelRecord = processExcelData(list, user);
            countryRepository.insertBatchExecute(excelRecord.getInsertRecords());
            countryRepository.updateBatchExecute(excelRecord.getUpdateRecords());
            String statusMsg = (excelRecord.getValidRecords().size() > 0 && excelRecord.getInValidRecords().size() == 0)
                    ? SUCCESS_MSG : (excelRecord.getValidRecords().size() == 0 && excelRecord.getInValidRecords().size() != 0)
                    ? FILE_UPLOAD_FAIL_MSG : PARTIAL_SUCCESS_MSG;
            response = ResponseMessage.builder().message(statusMsg).statusCode(HttpStatus.OK.value()).invalidRecords(excelRecord.getInValidRecords()).insertedRecords(excelRecord.getValidRecords()).build();
        } else {
            response = ResponseMessage.builder().message(FILE_ERR_MSG).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }
        return response;
    }

    @Override
    public List<CountryData> getCountriesData() {

        return null;
    }

}
