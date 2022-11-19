package ru.egorov.StoreCrawler.models;

public abstract class Product {
    public abstract int getId();

    public abstract String getSku();

    public abstract String getName();

    public abstract Double getPrice();

    public abstract String getBrand();
}
