package org.example.record.repository;

import org.example.record.entity.ServiceVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceVoucherRepository extends JpaRepository<ServiceVoucher, Long>, JpaSpecificationExecutor<ServiceVoucher> {
    
    Optional<ServiceVoucher> findByVoucherNo(String voucherNo);
    
    List<ServiceVoucher> findByOrderId(Long orderId);
    
    List<ServiceVoucher> findByMemberId(Long memberId);
}
