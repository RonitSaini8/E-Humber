package com.humber.EHumber.controllers;

import com.humber.EHumber.models.Product;
import com.humber.EHumber.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ProductController {
    @Value("EHumber")
    private String shopName;

    @Value("5")
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
}
