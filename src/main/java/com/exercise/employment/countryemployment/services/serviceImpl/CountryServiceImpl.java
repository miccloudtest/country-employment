package com.exercise.employment.countryemployment.services.serviceImpl;

import com.exercise.employment.countryemployment.beans.*;
import com.exercise.employment.countryemployment.cache.CountryCacheInitializer;
import com.exercise.employment.countryemployment.repositories.repository.CountryRepository;
import com.exercise.employment.countryemployment.services.service.CountryService;
import com.exercise.employment.countryemployment.services.service.PoiReaderService;
import com.exercise.employment.countryemployment.utils.excel.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
    @Autowired
    private PoiReaderService poiReaderService;
    @Value("${app.file.upload.message.success}")
    public String SUCCESS_MSG;
    @Value("${app.file.upload.message.fileTypeError}")
    public String FILE_ERR_MSG;
    @Value("${app.file.upload.message.partialSuccess}")
    public String PARTIAL_SUCCESS_MSG;
    @Value("${app.file.upload.message.fail}")
    public String FILE_UPLOAD_FAIL_MSG;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    CountryCacheInitializer stateCacheIntializer;


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

    Consumer<String> doBackUp = message -> {
        if(message.equalsIgnoreCase(SUCCESS_MSG)||message.equalsIgnoreCase(PARTIAL_SUCCESS_MSG))
            countryRepository.exceCountryStateBackUpProc();
    };


    private ExcelRecord processExcelData(List<CountryData> excelRecords, User user) throws Exception {
        List<CountryData> validList = excelRecords.stream().filter(record -> isNotNull.test(record.getStateNumEmployed()) && isValidCountryState.test(record.getCountryName().trim(), record.getStateName().trim())).
                map(record -> {
                    record.setEmploymentRate((double) record.getStatePopulation() / record.getStateNumEmployed());
                    CountryStateIDMaster countryStateMaster = getCountryStateMapping.apply(record.getCountryName(), record.getStateName());
                    record.setStateId(countryStateMaster.getStateId());
                    record.setCountryId(countryStateMaster.getCountryId());
                    return record;
                }).collect(Collectors.toList());
        List<CountryData> invalidList = excelRecords.stream()
                .filter(record -> !(isNotNull.test(record.getStateNumEmployed()) && isValidCountryState.test(record.getCountryName().trim(), record.getStateName().trim())))
                .collect(Collectors.toList());
        Set<Integer> stateName = validList.stream().map(CountryData::getStateId).collect(Collectors.toSet());
        ExcelRecord excelRecord = ExcelRecord.builder().validRecords(validList).inValidRecords(invalidList).build();
        doListPartition(validList, stateName, excelRecord, user);
        return excelRecord;
    }

    private void doListPartition(List<CountryData> insertRecord, Set<Integer> stateName, ExcelRecord excelRecord, User user) {
        Map<Integer, Integer> existCountryStateIdMap = countryRepository.getExistingCountryStateId(stateName);
        Map<Boolean, List<CountryData>> recordList = insertRecord.stream()
                .collect(Collectors.groupingBy(record -> existCountryStateIdMap.containsKey(record.getStateId())));
        List<CountryData> updateRecords = recordList.get(true);
        List<CountryData> insertRecords = recordList.get(false);
        if (insertRecords != null) {
            excelRecord.setInsertRecords(insertRecords.stream().map(record -> {
                record.setCreated_ts(new Date());
                record.setCreated_by(user.getEmail());
                return record;
            }).collect(Collectors.toList()));
        }
        if (updateRecords != null) {
            excelRecord.setUpdateRecords(updateRecords.stream().map(record -> {
                record.setModified_ts(new Date());
                record.setModified_by(user.getEmail());
                return record;
            }).collect(Collectors.toList()));
        }

    }

    @Transactional
    @Override
    public ResponseMessage processFile(MultipartFile multipartFile, User user) throws Exception {
        ResponseMessage response = null;
        if (ExcelUtil.isXls.or(ExcelUtil.isXlsx).test(multipartFile)) {
            List<CountryData> list = poiReaderService.readFile(multipartFile, CountryData.class);
            ExcelRecord excelRecord = processExcelData(list, user);
            countryRepository.insertBatchExecute(excelRecord.getInsertRecords());
            countryRepository.updateBatchExecute(excelRecord.getUpdateRecords());
            String statusMsg = (excelRecord.getValidRecords().size() > 0 && excelRecord.getInValidRecords().size() == 0)
                    ? SUCCESS_MSG : (excelRecord.getValidRecords().size() == 0 && excelRecord.getInValidRecords().size() != 0)
                    ? FILE_UPLOAD_FAIL_MSG : PARTIAL_SUCCESS_MSG;
            doBackUp.accept(statusMsg);
            /*response = ResponseMessage.builder().message(statusMsg).statusCode(HttpStatus.OK.value()).invalidRecords(excelRecord.getInValidRecords()).insertedRecords(excelRecord.getValidRecords()).build();*/
            response = ResponseMessage.builder().message(statusMsg).statusCode(HttpStatus.OK.value()).body(excelRecord).build();
        } else {
            response = ResponseMessage.builder().message(FILE_ERR_MSG).statusCode(HttpStatus.BAD_REQUEST.value()).build();
        }
        return response;
    }

    @Override
    public List<CountryData> getCountriesData() {
        List<CountryData> countryData = countryRepository.getCountriesData();
        return countryData;
    }

}

