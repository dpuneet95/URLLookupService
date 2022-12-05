package com.urllookup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response {
    Integer statusCode;
    String message;
}
