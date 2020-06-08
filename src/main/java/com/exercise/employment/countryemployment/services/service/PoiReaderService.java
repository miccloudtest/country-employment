package com.exercise.employment.countryemployment.services.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;

public interface PoiReaderService<R> {
    public List<R> readFile(MultipartFile file, Class<R> bean) throws Exception;
}
