package org.example.mapstruct;

import org.example.dto.StoreRequest;
import org.example.dto.StoreResponse;
import org.example.entity.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreResponse toResponse(Store entity);
    List<StoreResponse> toResponseList(List<Store> entities);
    Store toEntity(StoreRequest request);
}
