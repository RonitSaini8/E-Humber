package com.humber.EHumber.controllers;

import com.humber.EHumber.models.Product;
import com.humber.EHumber.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shop")
public class ProductController {
    @Value("${shop.name}")
    private String shopName;

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    ProductService productService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("shopName", shopName);
        return "home";
    }

    @GetMapping("/products/{pageNo}")
    public String products(Model model,
                           @RequestParam(required = false) String message,
                           @RequestParam(required = false) String searchedCategory,
                           @RequestParam(required = false) Double searchedPrice,
                           @PathVariable int pageNo,
                           @RequestParam(required = false, defaultValue = "id") String sortField,
                           @RequestParam(required = false, defaultValue = "ASC") String sortDirection
    ) {

        if (searchedCategory != null && searchedPrice != null) {
            List<Product> filteredProducts = productService.getFilteredProducts(searchedCategory, searchedPrice);
            model.addAttribute("message", !filteredProducts.isEmpty() ? "Products searched successfully !" : "Products not found !");
            model.addAttribute("products", !filteredProducts.isEmpty() ? filteredProducts : productService.getAllProducts());
            return "products";
        }

        Page<Product> page = productService.getPaginatedSortedProducts(pageNo, pageSize, sortField, sortDirection);
        model.addAttribute("products", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reversedSortDirection", sortDirection.equals("ASC") ? "desc" : "asc");

        model.addAttribute("message", message);
        return "products";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("product", new Product());
        model.addAttribute("message", message);
        return "add";
    }

    @PostMapping("/products/add")
    public String saveProduct(Model model, @ModelAttribute Product product) {
        int statusCode = productService.saveProduct(product);

        if (statusCode == 0) {
            model.addAttribute("message", "Product not added ! Missing name or price !");
            return "add";
        }
        return "redirect:/shop/products/1?message=Product saved successfully !";
    }

    @GetMapping("products/update/{id}")
    public String updateProduct(Model model, @PathVariable int id) {
        Optional<Product> productToUpdate = Optional.ofNullable(productService.getProductById(id));

        if (productToUpdate.isPresent()) {
            model.addAttribute("product", productToUpdate.get());
            return "add";
        }
        return "redirect:/shop/products/1?message=Product to be updated not found !";
    }

    @PostMapping("products/update/{id}")
    public String updateProduct(@ModelAttribute Product product) {
        System.out.println(product.toString());
        productService.updateProduct(product.getId(), product);
        return "redirect:/shop/products/1?message=Product updated successfully !";
    }

    @DeleteMapping("products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return ResponseEntity.ok("Product deleted successfully !");
    }
}
