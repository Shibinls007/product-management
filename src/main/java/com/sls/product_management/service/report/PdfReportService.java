package com.sls.product_management.service.report;

import com.sls.product_management.model.Product;
import com.sls.product_management.repository.ProductRepository;
import com.sls.product_management.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfReportService {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public void export(HttpServletResponse response) throws IOException {
        try {
            // Fetch all products
            List<Product> products = productRepository.findAll();

            // Create a list of maps for the report data source
            List<Map<String, Object>> reportData = products.stream().map(product -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", product.getId());
                item.put("name", product.getName());
                item.put("description", product.getDescription());
                item.put("price", product.getPrice());
                item.put("quantity", product.getQuantity());
                // Calculate revenue for each product
                item.put("revenue", productService.getRevenueByProduct(product.getId()));
                return item;
            }).collect(Collectors.toList());


            // Load JRXML file and compile it
            File file = ResourceUtils.getFile("classpath:jasper/product_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            // Create a JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            // Export the report to PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            log.info("PDF Report generated successfully.");

        } catch (JRException e) {
            log.error("Error generating Jasper report", e);
            throw new IOException("Error generating report", e);
        }
    }
}