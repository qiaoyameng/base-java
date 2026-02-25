package org.example.mapstruct;

import org.example.dto.OrderRequest;
import org.example.dto.OrderResponse;
import org.example.entity.WashOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "service.name", target = "serviceName")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    OrderResponse toResponse(WashOrder entity);

    List<OrderResponse> toResponseList(List<WashOrder> entities);

    WashOrder toEntity(OrderRequest request);
}
