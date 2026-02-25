package org.example.mapstruct;

import org.example.dto.ReviewResponse;
import org.example.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderNo", target = "orderNo")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.nickname", target = "customerName")
    ReviewResponse toResponse(Review entity);
    List<ReviewResponse> toResponseList(List<Review> entities);
}
