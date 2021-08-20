package com.mechatronic.codescanner;

public class scanData {
    private String SerialNumber;
    private String PartNumber;
    private String MACAddress;
    private String FSANnumber;
    private String SourceCompany;
    private String CustomerCompany;
    private String OrderNumber;
    private String SaleNumber;
    private boolean passFail;


    public scanData(String sn, String pn, String mc, String fs, String sc, String cc, String on, String sln, boolean pf) {
        SerialNumber = sn;
        PartNumber = pn;
        MACAddress = mc;
        FSANnumber = fs;
        SourceCompany = sc;
        CustomerCompany = cc;
        OrderNumber = on;
        SaleNumber = sln;
        passFail = pf;
    }

    public String getSerialNumber() { return SerialNumber; }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getPartNumber() {
        return PartNumber;
    }

    public void setPartNumber(String partNumber) {
        PartNumber = partNumber;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public String getFSANnumber() {
        return FSANnumber;
    }

    public void setFSANnumber(String FSANnumber) {
        this.FSANnumber = FSANnumber;
    }


    public String getSourceCompany() {
        return SourceCompany;
    }

    public void setSourceCompany(String sourceCompany) {
        SourceCompany = sourceCompany;
    }

    public String getCustomerCompany() {
        return CustomerCompany;
    }

    public void setCustomerCompany(String customerCompany) { CustomerCompany = customerCompany; }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getSaleNumber() {
        return SaleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        SaleNumber = saleNumber;
    }

    public boolean isPassFail() {
        return passFail;
    }

    public void setPassFail(boolean pf) {
        passFail = pf;
    }
}
