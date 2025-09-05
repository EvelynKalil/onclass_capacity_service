package com.onclass.capacity.infrastructure.entrypoints.mapper;

import com.onclass.capacity.domain.model.Capacity;
import com.onclass.capacity.infrastructure.entrypoints.dto.CapacityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacityMapper {

    // DTO → Dominio
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name",        target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "technologyIds", target = "technologyIds")
    Capacity dtoToDomain(CapacityDTO dto);

    // Dominio → DTO
    @Mapping(source = "id",          target = "id")
    @Mapping(source = "name",        target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "technologyIds", target = "technologyIds")
    CapacityDTO toDto(Capacity model);
}
