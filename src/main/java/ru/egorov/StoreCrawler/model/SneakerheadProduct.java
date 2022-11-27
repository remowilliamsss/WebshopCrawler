package ru.egorov.StoreCrawler.model;

import ru.egorov.StoreCrawler.Store;

import javax.persistence.*;

@Entity
@Table(name = "sneakerhead_product")
public class SneakerheadProduct extends Product {

    @Override
    @Transient
    public Store getStore() {
        return Store.SNEAKERHEAD;
    }
}
