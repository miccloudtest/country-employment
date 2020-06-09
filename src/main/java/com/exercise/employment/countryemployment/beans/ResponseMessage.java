package com.exercise.employment.countryemployment.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Data
public class ResponseMessage<T> implements Serializable {
    private static final long serialVersionUID = -718349591992929899L;
    private Integer statusCode;
    private String message;
    private T body;
}
