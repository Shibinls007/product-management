package com.sls.product_management.controller;

import com.sls.product_management.constant.ProductConstants;
import com.sls.product_management.model.Product;
import com.sls.product_management.service.ProductService;
import com.sls.product_management.service.report.PdfReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(ProductConstants.API_PRODUCTS)
@Tag(name = "Product Management Controller", description = "Endpoint forProduct Management Controller")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private PdfReportService pdfReportService;

    @GetMapping
    @Operation(summary = "Get all products with pagination and filters", description = "Supports filtering by category, name, and minimum price.")
    public  ResponseEntity<?> getAllProducts(Pageable pageable, HttpServletRequest request) {

        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single product by ID", description = "Publicly accessible.")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize(ProductConstants.ANY_ROLE)
    @Operation(summary = "Add a new product", security = @SecurityRequirement(name = "basicAuth"), description = "Requires USER or ADMIN role.")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // ADMIN ONLY ACCESS
    @PutMapping("/{id}")
    @PreAuthorize(ProductConstants.ADMIN_ROLE)
    @Operation(summary = "Update an existing product", security = @SecurityRequirement(name = "basicAuth"), description = "Requires ADMIN role.")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(ProductConstants.ADMIN_ROLE)
    @Operation(summary = "Delete a product", security = @SecurityRequirement(name = "basicAuth"), description = "Requires ADMIN role.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Sales and Revenue
    @PostMapping("/{id}/sales")
    @PreAuthorize(ProductConstants.ANY_ROLE)
    @Operation(summary = "Add a sale for a product", security = @SecurityRequirement(name = "basicAuth"), description = "Requires USER or ADMIN role.")
    public ResponseEntity<?> addSale(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(productService.addSale(id, quantity));
    }

    @GetMapping(ProductConstants.API_REVENUE_TOTAL)
    @PreAuthorize(ProductConstants.ADMIN_ROLE)
    @Operation(summary = "Get total revenue from all sales", security = @SecurityRequirement(name = "basicAuth"), description = "Requires ADMIN role.")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        return ResponseEntity.ok(productService.getTotalRevenue());
    }

    @GetMapping("/{id}/revenue")
    @PreAuthorize(ProductConstants.ADMIN_ROLE)
    @Operation(summary = "Get revenue for a specific product", security = @SecurityRequirement(name = "basicAuth"), description = "Requires ADMIN role.")
    public ResponseEntity<BigDecimal> getRevenueByProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getRevenueByProduct(id));
    }

    // PDF Export
    @GetMapping(ProductConstants.EXPORT_PDF)
    @PreAuthorize(ProductConstants.ADMIN_ROLE)
    @Operation(summary = "Download product list as PDF", security = @SecurityRequirement(name = "basicAuth"), description = "Requires ADMIN role.")
    public void generateProductPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        pdfReportService.export(response);
    }
}