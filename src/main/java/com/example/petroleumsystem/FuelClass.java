package com.example.petroleumsystem;

public class FuelClass{
    private int id ;
    private int tunk_number;
    private String fuel_type;
    private int tunk_capacity;

    private double price_per_litters ;
    public FuelClass(int id , int tunk_number , int tunk_capacity , String fuel_type , double price_per_litters){

        this.id = id;
        this.tunk_number = tunk_number;
        this.tunk_capacity = tunk_capacity;
        this.fuel_type = fuel_type;
        this.price_per_litters = price_per_litters ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTunk_number() {
        return tunk_number;
    }

    public void setTunk_number(int tunk_number) {
        this.tunk_number = tunk_number;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public int getTunk_capacity() {
        return tunk_capacity;
    }

    public void setTunk_capacity(int tunk_capacity) {
        this.tunk_capacity = tunk_capacity;
    }

    public double getPrice_per_litters() {
        return price_per_litters;
    }

    public void setPrice_per_litters(double price_per_litters) {
        this.price_per_litters = price_per_litters;
    }
}
