package com.exercise.employment.countryemployment.controller;

import com.exercise.employment.countryemployment.beans.CountryData;
import com.exercise.employment.countryemployment.beans.ResponseMessage;
import com.exercise.employment.countryemployment.beans.User;
import com.exercise.employment.countryemployment.services.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/")
public class CountryController {
    Logger logger = LoggerFactory.getLogger(CountryController.class);
    @Autowired
    CountryService countryService;
    @Value("${app.request.notfound}")
    public String DATA_NOT_FOUND;
    @Value("${app.request.success}")
    public String SUCCESS;
    @Value("${app.request.error}")
    public String SERVER_ERROR;

    @PostMapping("excelupload")
    @ResponseBody
    public ResponseEntity<ResponseMessage> uploadExcel(@RequestParam("user") String userData, @RequestParam("file") MultipartFile file) {
        ResponseMessage responseMessage;
        ResponseEntity responseEntity;
        try {
            User user = new ObjectMapper().readValue(userData, User.class);
            responseMessage = countryService.processFile(file, user);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            logger.info("Requested process status for file {} ,{}", file.getOriginalFilename(), responseMessage.getMessage());
        } catch (Exception ex) {
            logger.error("Error while uploading file {}", ex.getMessage());
            responseMessage = ResponseMessage.builder().message(SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
        return responseEntity;

    }

    @GetMapping("countries")
    @ResponseBody
    public ResponseEntity getCountriesData() {
        List<CountryData> countryData = null;
        String message = "";
        try {
            countryData = countryService.getCountriesData();
            message = !countryData.isEmpty() ? SUCCESS : DATA_NOT_FOUND;
            return ResponseEntity.status(HttpStatus.OK).
                    body(ResponseMessage.builder()
                            .statusCode(HttpStatus.OK.value()).
                                    message(message).body(countryData).build());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(ResponseMessage.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                                    message(SERVER_ERROR).build());
        }

    }

}
