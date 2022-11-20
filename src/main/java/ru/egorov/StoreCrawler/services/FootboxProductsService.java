package ru.egorov.StoreCrawler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.StoreCrawler.models.FootboxProduct;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.repositories.FootboxProductsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FootboxProductsService extends ProductsService {
    private final FootboxProductsRepository footboxProductsRepository;

    @Autowired
    public FootboxProductsService(FootboxProductsRepository footboxProductsRepository) {
        this.footboxProductsRepository = footboxProductsRepository;
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return footboxProductsRepository.findBySku(sku);
    }

    // TODO: 20.11.2022 мб стоит прикрутить пагинацию? Что будет, если найдет миллион записей?
    public List<Product> findAll() {
        return new ArrayList<>(footboxProductsRepository.findAll());
    }

    @Transactional
    // TODO: 20.11.2022 Методы create и update должны возвращать обновленное значение
    public void save(FootboxProduct product) {
        footboxProductsRepository.save(product);

        // TODO: 20.11.2022 Советую познакомиться с логгерами. logback, log4j, log4j2 - на твой выбор
        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been saved");
    }

    @Transactional
    // TODO: 20.11.2022 Никогда так не делай. product не под управлением EM,
    //  пытаться его обновить в транзакции - не самая разумная затея.
    //  Я вообще не уверен, что этот метод работает у тебя так, как ты ожидаешь:)
    public void update(int id, FootboxProduct product) {
        product.setId(id);

        footboxProductsRepository.save(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been updated");
    }

    /*    Принимает список товаров. Обновляет информацию в базе данных о старых товарах, сохраняет новые.
    Параметр isStopped для прерывания выполнения метода извне.*/
    @Transactional
    public void updateProducts(List<Product> products, Boolean isStopped) {
        for (Product product : products) {
            if (isStopped)
                return;

            FootboxProduct footboxProduct = (FootboxProduct) product;

            Optional<Product> foundProduct = findBySku(footboxProduct.getSku());

            if (foundProduct.isPresent()) {
                if (!foundProduct.get().equals(footboxProduct))
                    update(foundProduct.get().getId(), footboxProduct);
            } else {
                save(footboxProduct);
            }
        }
    }

    @Transactional
    public void delete(Product product) {
        footboxProductsRepository.delete((FootboxProduct) product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been removed");
    }

    /*    Принимает список товаров. Удаляет из базы данных товары, которых нет в списке.
    Параметр isStopped для прерывания выполнения метода извне.*/
    // TODO: 20.11.2022 как ты представляешь прерывание метода извне?
    @Transactional
    public void deleteOther(List<Product> products, Boolean isStopped) {
        for (Product product : findAll()) {
            if (isStopped)
                return;

            if (!products.contains(product))
                delete(product);
        }
    }

    @Override
    public List<Product> findAllByName(String name) {
        return footboxProductsRepository.findAllByNameContainingIgnoreCase(name).orElse(Collections.emptyList());
    }

    @Override
    public String getStoreName() {
        return "Footbox";
    }
}
