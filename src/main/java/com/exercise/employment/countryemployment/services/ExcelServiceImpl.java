package com.exercise.employment.countryemployment.services;

import com.exercise.employment.countryemployment.Cache.CountryStateCache;
import com.exercise.employment.countryemployment.beans.*;
import com.exercise.employment.countryemployment.repository.CountryRepository;
import com.exercise.employment.countryemployment.repository.CountryStateRepository;
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

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements IExcelService {

    Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);
    @Autowired
    CountryStateRepository excelRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    POIReaderService poiReaderService;
    @Value("${app.file.upload.success.message}")
    public String SUCCESS_MSG;
    @Value("${app.file.type.error.message}")
    public String FILE_ERR_MSG;
    @Value("${app.file.upload.partial.success.message}")
    public String PARTIAL_SUCCESS_MSG;

    @Value("${app.file.upload.fail.message}")
    public String FILE_UPLOAD_FAIL_MSG;


    @Autowired
    CountryStateCache countryStateCache;

    Predicate<MultipartFile> isXls = file -> file.getOriginalFilename().toLowerCase().endsWith("xls");

    Predicate<MultipartFile> isXlsx = file -> file.getOriginalFilename().toLowerCase().endsWith("xlsx");

    Predicate<Long> isNotNull = var -> var != null && var != 0;

    BiPredicate<String, String> isValidCountryState = (country, state) -> {
        Set<String> states = countryStateCache.countryStateMap.get(country);
        return states != null ? states.contains(state) : false;
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

    private void saveOrUpdate(Map<String, Set<State>> entities) throws Exception {
        try {
            entities.forEach((key, value) -> {
                Country country = Country.builder().country(key).build();
                value.forEach(state -> {
                    state.setCountry(country);
                });
                country.setStates(value);
                countryRepository.save(country);
            });
        } catch (Exception ex) {
            logger.error("Error while storing records to oracle {}", ex.getMessage());
            throw ex;
        }
    }

    private Map<String, Set<State>> prepareMapping(List<ExcelSheet> records) {
        Map<String, Set<State>> countryStateMap = new HashMap<>();
        List<Country> country = new ArrayList<>();
        try {
            logger.info("Inserting record to database {}", records.size());
            records.forEach(record -> {
                if (countryStateMap.containsKey(record.getCountryName())) {
                    State stateNew = State.builder().stateName(record.getStateName()).
                            area(record.getArea()).isDeleted(record.getIsDeleted()).
                            lastUpdatedTs(record.getLastUpdatedTs()).
                            noOfEmployed(record.getNoOfEmployed()).
                            population(record.getPopulation()).build();
                    countryStateMap.get(record.getCountryName()).add(stateNew);
                } else {

                    State stateNew = State.builder().stateName(record.getStateName()).
                            area(record.getArea()).isDeleted(record.getIsDeleted()).
                            lastUpdatedTs(record.getLastUpdatedTs()).
                            noOfEmployed(record.getNoOfEmployed()).
                            population(record.getPopulation()).build();
                    Set<State> states = new HashSet<State>();
                    states.add(stateNew);
                    countryStateMap.put(record.getCountryName(), states);
                }
            });

        } catch (Exception ex) {
            logger.error("Error while mapping country state records {}", ex.getMessage());
            throw ex;
        }
        return countryStateMap;
    }


    private ExcelRecord processExcelData(List<ExcelSheet> excelRecords) throws Exception {
        List<ExcelSheet> validList = excelRecords.stream().filter(record -> isNotNull.test(record.getNoOfEmployed()) && isValidCountryState.test(record.getCountryName().trim(), record.getStateName().trim())).
                map(record -> {
                    record.setEmploymentRate((double) record.getPopulation() / record.getNoOfEmployed());
                    return record;
                }).collect(Collectors.toList());
        List<ExcelSheet> invalidList = excelRecords.stream()
                .filter(record -> !(isNotNull.test(record.getNoOfEmployed()) && isValidCountryState.test(record.getCountryName().trim(), record.getStateName().trim())))
                .collect(Collectors.toList());
        ExcelRecord excelRecord = ExcelRecord.builder().validRecords(validList).inValidRecords(invalidList).build();
        return excelRecord;
    }


    @Override
    public ResponseMessage processFile(MultipartFile multipartFile) throws Exception {
        ResponseMessage response = null;
        if (isXls.or(isXlsx).test(multipartFile)) {
            List<ExcelSheet> list = poiReaderService.readFile(multipartFile.getInputStream(), ExcelSheet.class);
            ExcelRecord excelRecord = processExcelData(list);
            Map<String, Set<State>> map = prepareMapping(excelRecord.getValidRecords());
            saveOrUpdate(map);
            String statusMsg = (excelRecord.getValidRecords().size() > 0 && excelRecord.getInValidRecords().size() == 0)
                    ? SUCCESS_MSG : (excelRecord.getValidRecords().size() == 0 && excelRecord.getInValidRecords().size() != 0)
                    ? FILE_UPLOAD_FAIL_MSG : PARTIAL_SUCCESS_MSG;

            //String statusMsg = (excelRecord.getValidRecords().size() > 0 && excelRecord.getInValidRecords().size() > 0) ? PARTIAL_SUCCESS_MSG : SUCCESS_MSG;
            response = ResponseMessage.builder().message(statusMsg).statusCode(HttpStatus.OK.value()).invalidRecords(excelRecord.getInValidRecords()).insertedRecords(excelRecord.getValidRecords()).build();
        } else {
            response = ResponseMessage.builder().message(FILE_ERR_MSG).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }
        return response;
    }

    private String getStatusMsg(ExcelRecord excelRecord) {

        String statusMsg = (excelRecord.getValidRecords().size() > 0 && excelRecord.getInValidRecords().size() == 0) ? SUCCESS_MSG : (excelRecord.getValidRecords().size() == 0 && excelRecord.getInValidRecords().size() != 0) ? FILE_UPLOAD_FAIL_MSG : PARTIAL_SUCCESS_MSG;
        return "";
    }

}

