package ru.egorov.StoreCrawler.model;

import javax.persistence.*;

@Entity
@Table(name = "sneakerhead_product")
public class SneakerheadProduct extends Product {

    {
        setStoreType(StoreType.sneakerhead);
    }
}
