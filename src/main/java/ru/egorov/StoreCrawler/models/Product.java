package ru.egorov.StoreCrawler.models;

// TODO: 20.11.2022 Если уж это абстрактный класс - мог бы и поля сюда вынести.
//  Стоит познакомиться со стратегиями наследования в Hibernate
public abstract class Product {
    public abstract int getId();

    public abstract String getSku();

    public abstract String getName();

    public abstract Double getPrice();

    public abstract String getBrand();
}
