package org.example.mapstruct;

import org.example.dto.ReviewResponse;
import org.example.dto.ServiceRecordResponse;
import org.example.entity.Review;
import org.example.entity.ServiceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceRecordMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderNo", target = "orderNo")
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "service.name", target = "serviceName")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.nickname", target = "customerName")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    ServiceRecordResponse toResponse(ServiceRecord entity);
    List<ServiceRecordResponse> toResponseList(List<ServiceRecord> entities);
}
