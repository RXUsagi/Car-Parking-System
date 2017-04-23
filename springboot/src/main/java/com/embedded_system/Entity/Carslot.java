package com.embedded_system.Entity;

/**
 * Created by onion on 4/12/2017.
 */
public class Carslot {
    private String carNumber;
    private String customerFullname;
    private String customerTelNumber;
    private String date;
    public String index;

    public Carslot(String carNumber, String customerFullname, String customerTelNumber, String date) {
        this.carNumber = carNumber;
        this.customerFullname = customerFullname;
        this.customerTelNumber = customerTelNumber;
        this.date = date;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCustomerFullname() {
        return customerFullname;
    }

    public void setCustomerFullname(String customerFullname) {
        this.customerFullname = customerFullname;
    }

    public String getCustomerTelNumber() {
        return customerTelNumber;
    }

    public void setCustomerTelNumber(String customerTelNumber) {
        this.customerTelNumber = customerTelNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
