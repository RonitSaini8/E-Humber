package com.humber.EHumber.repositories;

import com.humber.EHumber.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductByName(String name);

    List<Product> findByIgnoreCaseNameAndPrice(String name, double price);

}
