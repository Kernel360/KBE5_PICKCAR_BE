package com.pickcar.company.presentation.dto.response;

import lombok.Builder;

@Builder
public record CompanyListResponse (Long id, String name){
}
