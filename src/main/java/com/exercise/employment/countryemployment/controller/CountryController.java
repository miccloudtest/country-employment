package com.exercise.employment.countryemployment.controller;

import com.exercise.employment.countryemployment.beans.CountryData;
import com.exercise.employment.countryemployment.beans.ResponseMessage;
import com.exercise.employment.countryemployment.beans.User;
import com.exercise.employment.countryemployment.services.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.LifecycleState;
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
@RequestMapping("excel")
public class CountryController {
    Logger logger = LoggerFactory.getLogger(CountryController.class);
    @Autowired
    CountryService countryService;

    @Value("${app.file.upload.internal.server.error}")
    private String SERVER_ERROR;


    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity<ResponseMessage> uploadExcel(@RequestParam("user") String userData, @RequestParam("file") MultipartFile file) {
        ResponseMessage responseMessage;
        ResponseEntity responseEntity;
        try {
            User user  = new ObjectMapper().readValue(userData, User.class);
            responseMessage = countryService.processFile(file,user);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            logger.info("Requested proceed successfully filename {}", file.getOriginalFilename());
        } catch (Exception ex) {
            logger.error("Error while uploading file {}", ex.getMessage());
            responseMessage = ResponseMessage.builder().message(SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
        return responseEntity;

    }
    //in progress for now dont review report generation functionality
    public ResponseEntity getCountriesData(){
        List<CountryData> countryData=null;
        return ResponseEntity.status(HttpStatus.OK).body(countryData);
    }

}