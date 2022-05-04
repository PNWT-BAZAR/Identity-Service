package com.unsa.etf.Identity.Service.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ObjectListResponse<ObjectType> {

    private int statusCode;
    private List<ObjectType> objectsList;
    private BadRequestResponseBody error;
}
