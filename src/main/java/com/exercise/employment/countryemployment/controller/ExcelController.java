package com.exercise.employment.countryemployment.controller;

import com.exercise.employment.countryemployment.beans.ResponseMessage;
import com.exercise.employment.countryemployment.services.IExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("excel")
public class ExcelController {
    Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    IExcelService excelService;

    @Value("${app.file.upload.internal.server.error}")
    private String SERVER_ERROR;


    @PostMapping("upload")
    @ResponseBody
    public ResponseEntity<ResponseMessage> uploadExcel(@RequestParam("file") MultipartFile file) {
        ResponseMessage responseMessage;
        ResponseEntity responseEntity;
        try {
            responseMessage = excelService.processFile(file);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            logger.info("Requested proceed successfully filename {}", file.getOriginalFilename());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error while uploading file {}", ex.getMessage());
            responseMessage = ResponseMessage.builder().message(SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
        return responseEntity;

    }

}
