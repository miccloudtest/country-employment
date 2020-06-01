package com.exercise.employment.countryemployment.services;

import com.exercise.employment.countryemployment.beans.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

public interface IExcelService {
    public ResponseMessage processFile(MultipartFile multipartFile) throws Exception;
}
