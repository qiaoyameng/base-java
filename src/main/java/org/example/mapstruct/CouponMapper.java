package org.example.mapstruct;

import org.example.dto.CouponRequest;
import org.example.dto.CouponResponse;
import org.example.entity.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    CouponResponse toResponse(Coupon entity);
    List<CouponResponse> toResponseList(List<Coupon> entities);
    Coupon toEntity(CouponRequest request);
}
