package com.agileengine.repository;


import com.agileengine.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductEntityRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name );
}
