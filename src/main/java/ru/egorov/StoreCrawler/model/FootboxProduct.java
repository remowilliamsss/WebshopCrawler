package ru.egorov.StoreCrawler.model;

import lombok.Getter;
import lombok.Setter;
import ru.egorov.StoreCrawler.Store;

import javax.persistence.*;

@Entity
@Table(name = "footbox_product")
@Getter
@Setter
public class FootboxProduct extends Product {
    private String composition;
    private String coloring;

    @Override
    @Transient
    public Store getStore() {
        return Store.FOOTBOX;
    }
}
