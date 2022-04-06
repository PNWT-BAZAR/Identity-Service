package com.unsa.etf.Identity.Service.Responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaginatedObjectResponse<ObjectType> {
    private List<ObjectType> foundObjects;
    private long numberOfItems;
    private int numberOfPages;
}
