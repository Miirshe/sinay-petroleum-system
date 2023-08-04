package com.example.petroleumsystem;

public class FuelTypeClass {
    private  int id ;
    private String fuelType ;

    private double price_per_litters;

    public FuelTypeClass(int id , String fuelType , double price_per_litters){
        this.id = id ;
        this.fuelType = fuelType;
        this.price_per_litters = price_per_litters;
    }

    public int getId() {
        return id;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getPrice_per_litters() {
        return price_per_litters;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setPrice_per_litters(int price_per_litters) {
        this.price_per_litters = price_per_litters;
    }
}
