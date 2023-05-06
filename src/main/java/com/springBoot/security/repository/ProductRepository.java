package com.springBoot.security.repository;

import com.springBoot.security.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByIdNotNullOrderById();
    List<Product> findAllByCategory_Id(Long id);
    List<Product> findByNameIsContainingIgnoreCase(String keyword);
}
