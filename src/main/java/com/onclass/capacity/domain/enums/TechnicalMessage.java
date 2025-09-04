package com.onclass.capacity.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    // Generic
    INTERNAL_ERROR("500", "Something went wrong, please try again", ""),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Something went wrong in adapters, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.code, "Bad Parameters, please verify data", ""),
    UNSUPPORTED_OPERATION("501", "Method not supported, please try again", ""),

    // Capacity (HU2 y HU3)
    CAPACITY_CREATED("201", "Capacity created successfully", ""),
    CAPACITY_ALREADY_EXISTS("400", "Capacity name already exists", "name"),
    CAPACITY_NAME_REQUIRED("400", "Capacity name is required", "name"),
    CAPACITY_DESCRIPTION_REQUIRED("400", "Capacity description is required", "description"),
    CAPACITY_NAME_TOO_LONG("400", "Capacity name max length is 50", "name"),
    CAPACITY_DESCRIPTION_TOO_LONG("400", "Capacity description max length is 90", "description"),
    ADAPTER_RESPONSE_NOT_FOUND("404", "Capacity not found", "id");


    public final String code;
    public final String message;
    public final String param;
}
