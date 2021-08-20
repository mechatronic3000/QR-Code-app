package com.mechatronic.codescanner;

public class orderData {
    private static String SourceCompany;
    private static String CustomerCompany;
    private static String OrderNumber;
    private static String SaleNumber;
    private static boolean passFail;

    public orderData(String sc, String cc, String on, String sn, Boolean pf ){
        SourceCompany = sc;
        CustomerCompany = cc;
        OrderNumber = on;
        SaleNumber = sn;
        passFail = pf;
    }

    public static String getSourceCompany() {
        return SourceCompany;
    }

    public static void setSourceCompany(String sourceCompany) {
        SourceCompany = sourceCompany;
    }

    public static String getCustomerCompany() {
        return CustomerCompany;
    }

    public static void setCustomerCompany(String customerCompany) { CustomerCompany = customerCompany; }

    public static String getOrderNumber() {
        return OrderNumber;
    }

    public static void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public static String getSaleNumber() {
        return SaleNumber;
    }

    public static void setSaleNumber(String saleNumber) {
        SaleNumber = saleNumber;
    }

    public static boolean isPassFail() {
        return passFail;
    }

    public static void setPassFail(boolean passFail) {
        orderData.passFail = passFail;
    }
}
