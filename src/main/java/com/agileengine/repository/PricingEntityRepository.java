package com.agileengine.repository;


import com.agileengine.domain.Pricing;
import com.agileengine.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingEntityRepository extends JpaRepository<Pricing, Long> {
    List<Pricing> findByProduct(Product product);
}
