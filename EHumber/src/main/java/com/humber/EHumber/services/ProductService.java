package com.humber.EHumber.services;

import com.humber.EHumber.models.Product;
import com.humber.EHumber.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public void addProduct(Product product) {
        Product existingProduct = productRepository.findProductByName(product.getName());

        if (existingProduct != null) {
            throw new IllegalStateException("A product with this name already exists !");
        }
        productRepository.save(product);
    }

    public int saveProduct(Product product) {
        if (product.getName().isEmpty() || product.getPrice() < 0) {
            return 0;
        }
        productRepository.save(product);
        return 1;
    }

    public void updateProduct(int productId, Product product) {
        boolean existsProduct = productRepository.existsById(productId);

        if (!existsProduct) {
            throw new IllegalStateException("A product with the id " + product.getId() + " does not exist !");
        }
        product.setId(productId);
        productRepository.save(product);
    }

    public void deleteProduct(int productId) {
        boolean existsProduct = productRepository.existsById(productId);

        if (!existsProduct) {
            throw new IllegalStateException("A product with the id " + productId + " does not exist !");
        }
        productRepository.deleteById(productId);
    }

    public List<Product> getFilteredProducts(String searchedName, double searchedPrice) {
        return productRepository.findByIgnoreCaseNameAndPrice(searchedName, searchedPrice);
    }

    public Page<Product> getPaginatedSortedProducts(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return productRepository.findAll(pageable);
    }
}
