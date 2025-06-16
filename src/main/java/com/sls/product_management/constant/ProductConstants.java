package com.sls.product_management.constant;

public class ProductConstants {
    private ProductConstants(){
    }

    public static final String API_PRODUCTS ="/api/products";
    public static final String ADMIN_ROLE ="hasRole('ADMIN')";
    public static final String ANY_ROLE ="hasAnyRole('ADMIN', 'USER')";

    public static final String EXPORT_PDF ="/export/pdf";
    public static final String API_REVENUE_TOTAL ="/revenue/total";
}
