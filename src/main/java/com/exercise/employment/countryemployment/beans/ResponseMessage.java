package com.exercise.employment.countryemployment.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@Data
public class ResponseMessage<T> implements Serializable {
    private static final long serialVersionUID = -718349591992929899L;
    private Integer statusCode;
    private String message;
    private T body;
}
