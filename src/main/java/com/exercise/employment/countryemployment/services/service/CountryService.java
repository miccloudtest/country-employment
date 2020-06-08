package com.exercise.employment.countryemployment.services.service;

import com.exercise.employment.countryemployment.beans.CountryData;
import com.exercise.employment.countryemployment.beans.ResponseMessage;
import com.exercise.employment.countryemployment.beans.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CountryService {
    public ResponseMessage processFile(MultipartFile multipartFile, User user) throws Exception;
    List<CountryData> getCountriesData();
}
