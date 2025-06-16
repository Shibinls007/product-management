package com.sls.product_management.service;

import com.sls.product_management.exception.ResourceNotFoundException;
import com.sls.product_management.model.Product;
import com.sls.product_management.model.Sale;
import com.sls.product_management.repository.ProductRepository;
import com.sls.product_management.repository.SaleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    public ProductService(ProductRepository productRepository, SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }


    public Page<Product> getAllProducts(Pageable pageable) {
        log.info("Fetching all products for page: {}", pageable.getPageNumber());
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long  id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product addProduct(Product product) {
        log.info("Adding new product: {}", product.getName());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct != null) {
            log.info("Updating product with id: {}", existingProduct.getId());
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        log.warn("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }

    public Sale addSale(Long productId, int quantity) {
        log.info("Adding sale for product id: {} with quantity: {}", productId, quantity);
        Product product = getProductById(productId);
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(quantity);
        sale.setSaleDate(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    //
    public BigDecimal getTotalRevenue() {
        log.info("Calculating total revenue");
        BigDecimal totalRevenue = saleRepository.findTotalRevenue();
        return totalRevenue == null ? BigDecimal.ZERO : totalRevenue;
    }

    //
    public BigDecimal getRevenueByProduct(Long productId) {
        log.info("Calculating revenue for product id: {}", productId);
        Product product = getProductById(productId);
        List<Sale> sales = saleRepository.findByProductId(productId);
        return sales.stream()
                .map(sale -> product.getPrice().multiply(new BigDecimal(sale.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}