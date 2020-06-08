package com.exercise.employment.countryemployment.services.service;

import java.io.InputStream;
import java.util.List;

public interface PoiReaderService<R> {
    public List<R> readFile(InputStream inputStream, Class<R> bean) throws Exception;
}
