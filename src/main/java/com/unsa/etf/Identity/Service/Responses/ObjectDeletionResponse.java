package com.unsa.etf.Identity.Service.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ObjectDeletionResponse {

    private int statusCode;
    private String message;
    private BadRequestResponseBody error;
}
