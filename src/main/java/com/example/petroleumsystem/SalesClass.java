package com.example.petroleumsystem;

import java.util.Date;
public class SalesClass {
    private int id ;
    private String customer_name ;
    private int customer_phone ;
    private String fuel_type ;
    private int tunk_number ;
    private int litters ;
    private double per_litters;
    private double total_price ;
    private Date date ;

    public SalesClass(int id , String customer_name , int customer_phone , String fuel_type , int tunk_number , int litters , double per_litters , double total_price , Date date ){
        this.id = id;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.fuel_type = fuel_type;
        this.tunk_number = tunk_number;
        this.litters = litters;
        this.per_litters = per_litters;
        this.total_price = total_price ;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public int getCustomer_phone() {
        return customer_phone;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public int getTunk_number() {
        return tunk_number;
    }

    public int getLitters() {
        return litters;
    }

    public double getPer_litters() {
        return per_litters;
    }

    public double getTotal_price() {
        return total_price;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setSupplier_phone(int customer_phone) {
        this.customer_phone= customer_phone;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }
    public void setTunk_number(int tunk_number) {
        this.tunk_number = tunk_number;
    }

    public void setLitters(int litters) {
        this.litters = litters;
    }

    public void setPer_litters(double per_litters) {
        this.per_litters = per_litters;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
