package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.entity.ProductEntity;
import com.example.springbootecommerceapi.model.UpdateProduct;
import com.example.springbootecommerceapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("api/ecommerce/v1/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/{productNumber}")
    public ResponseEntity<ProductEntity> getProduct(@Positive @PathVariable long productNumber) {
        ProductEntity product = productService.getProduct(productNumber);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping("")
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductEntity product) {
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productNumber}")
    public ResponseEntity<Void> removeProduct(@Positive @PathVariable long productNumber) {
        productService.removeProduct(productNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{productNumber}")
    public ResponseEntity<Void> updateProduct(@Valid @RequestBody UpdateProduct updateProduct,
                                              @PathVariable long productNumber) {
        productService.updateProduct(updateProduct, productNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
