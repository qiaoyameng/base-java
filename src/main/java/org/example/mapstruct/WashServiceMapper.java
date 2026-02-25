package org.example.mapstruct;

import org.example.dto.WashServiceRequest;
import org.example.dto.WashServiceResponse;
import org.example.entity.WashService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WashServiceMapper {

    WashServiceMapper INSTANCE = Mappers.getMapper(WashServiceMapper.class);

    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "store.id", target = "storeId")
    WashServiceResponse toResponse(WashService entity);

    List<WashServiceResponse> toResponseList(List<WashService> entities);

    WashService toEntity(WashServiceRequest request);
}
