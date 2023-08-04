package com.example.petroleumsystem;

public class FetchCustomer{
    private int id;
    private String name ;
    private String fuel ;
    private int litters;
    private double price_per_litter;
    private double total_price;

    public FetchCustomer(int id , String name , String fuel , int litters ,double price_per_litter , double total_price){
        this.id = id;
        this.name = name;
        this.fuel=fuel;
        this.litters = litters;
        this.price_per_litter = price_per_litter;
        this.total_price = total_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public int getLitters() {
        return litters;
    }

    public void setLitters(int litters) {
        this.litters = litters;
    }

    public double getPrice_per_litter() {
        return price_per_litter;
    }

    public void setPrice_per_litter(double price_per_litter) {
        this.price_per_litter = price_per_litter;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
