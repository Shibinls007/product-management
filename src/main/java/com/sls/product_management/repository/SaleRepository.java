package com.sls.product_management.repository;

import com.sls.product_management.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s WHERE s.product.id = :productId")
    List<Sale> findByProductId(Long productId);

    @Query("SELECT SUM(s.quantity * s.product.price) FROM Sale s")
    BigDecimal findTotalRevenue();
}