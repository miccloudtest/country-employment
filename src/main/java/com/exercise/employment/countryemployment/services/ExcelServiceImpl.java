package com.exercise.employment.countryemployment.services;

import com.exercise.employment.countryemployment.Utils.Util;
import com.exercise.employment.countryemployment.beans.Country;
import com.exercise.employment.countryemployment.beans.ExcelRecord;
import com.exercise.employment.countryemployment.beans.ResponseMessage;
import com.exercise.employment.countryemployment.beans.State;
import com.exercise.employment.countryemployment.repository.CountryRepository;
import com.exercise.employment.countryemployment.repository.CountryStateRepository;
import com.exercise.employment.countryemployment.repository.StateRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ExcelServiceImpl implements IExcelService {

    Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);
    @Autowired
    CountryStateRepository excelRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    StateRepository stateRepository;
    @Value("${app.file.upload.success.message}")
    public String SUCCESS_MSG;
    @Value("${app.file.type.error.message}")
    public String FILE_ERR_MSG;
    @Value("${app.file.upload.partial.success.message}")
    public String PARTIAL_SUCCESS_MSG;

    @Autowired
    Util util;

    Predicate<MultipartFile> isXls = file -> file.getOriginalFilename().toLowerCase().endsWith("xls");

    Predicate<MultipartFile> isXlsx = file -> file.getOriginalFilename().toLowerCase().endsWith("xlsx");

    Predicate<Long> isNotNull = var -> var != null && var != 0;

    BiPredicate<String, String> isValidCountryState = (country, state) -> {
        Set<String> states = util.countryStatePairMap.get(country);
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


    private List<Country> saveOrUpdate(List<Country> countryList) {
        try {
            List<Country> insertedRecords = new ArrayList<>();
            logger.info("Inserting record to database {}", countryList.size());
            countryList.stream().filter(country -> country.getStates().size() > 0).forEach(country -> {
                Country cnt = countryRepository.save(country);
                insertedRecords.add(cnt);
                logger.info("Record Inserted", country);
            });
            return insertedRecords;
        } catch (Exception ex) {
            logger.error("Error while inserting records into database {}", ex.getMessage());
            throw ex;
        }

    }


    private ExcelRecord processExcelData(Map<String, Set<State>> countryStateMap) {
        List<Country> countryList = new ArrayList<>();
        Set<State> invalidState = new HashSet<>();
        countryStateMap.forEach((key, value) -> {
            Country country = new Country();
            country.setCountryName(key);
            value.forEach(state -> {
                if (isValidCountryState.test(key, state.getStateName()) && isNotNull.test(state.getNoOfEmployed())) {
                    /* if (isNotNull.test(state.getNoOfEmployed())) {
                     */
                    state.setEmploymentRate((double) state.getPopulation() / state.getNoOfEmployed());
                    /*}*/
                    state.setCountry(country);
                    country.getStates().add(state);
                } else {
                    state.setAssociatedCountry(key);
                    invalidState.add(state);
                    logger.info("Invalid state records {}", state);
                }
            });
            countryList.add(country);
        });
        return ExcelRecord.builder().validRecords(countryList).invalidStateSet(invalidState).build();
    }


    @Override
    public ResponseMessage processFile(MultipartFile multipartFile) throws Exception {
        ResponseMessage response;
        if (isXls.or(isXlsx).test(multipartFile)) {
            Map<String, Set<State>> countryStateMap = readExcelData(multipartFile);
            ExcelRecord excelRecord = processExcelData(countryStateMap);
            List<Country> insertedRecords = saveOrUpdate(excelRecord.getValidRecords());
            String statusMsg = (excelRecord.getInvalidStateSet().size() > 0 && insertedRecords.size() > 0) ? PARTIAL_SUCCESS_MSG : SUCCESS_MSG;
            response = ResponseMessage.builder().message(statusMsg).statusCode(HttpStatus.OK.value()).invalidRecords(excelRecord.getInvalidStateSet()).insertedRecords(insertedRecords).build();
        } else {
            response = ResponseMessage.builder().message(FILE_ERR_MSG).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }
        return response;
    }

    private Map<String, Set<State>> readExcelData(MultipartFile file) throws IOException {
        try (Workbook workbook = getWorkBook.apply(file);) {
            Map<String, Set<State>> countryStateMap = new HashMap<>();
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // Avoiding to read header data
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                String countryName = "";
                State state = new State();
                int cellIndx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndx) {
                        case 0:
                            countryName = (String) currentCell.getStringCellValue().trim();
                            if (!countryStateMap.containsKey(countryName)) {
                                countryStateMap.put(countryName, new HashSet<>());
                            }
                            break;
                        case 1:
                            state.setStateName(currentCell.getStringCellValue().trim());
                            break;
                        case 2:
                            state.setPopulation((long) currentCell.getNumericCellValue());
                            break;
                        case 3:
                            state.setNoOfEmployed((long) currentCell.getNumericCellValue());
                            break;
                        case 4:
                            state.setArea((int) currentCell.getNumericCellValue());
                            break;
                        case 5:
                            state.setIsDeleted(currentCell.getStringCellValue().trim());
                            break;
                        case 6:
                            state.setLastUpdatedTs(currentCell.getDateCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIndx++;
                }
                if (!countryName.isEmpty()) {
                    countryStateMap.get(countryName).add(state);
                }
            }
            return countryStateMap;
        }
    }
}

