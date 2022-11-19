package ru.egorov.StoreCrawler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.StoreCrawler.models.Product;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;
import ru.egorov.StoreCrawler.repositories.SneakerheadProductsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SneakerheadProductsService extends ProductsService {
    private final SneakerheadProductsRepository sneakerheadProductsRepository;

    @Autowired
    public SneakerheadProductsService(SneakerheadProductsRepository sneakerheadProductsRepository) {
        this.sneakerheadProductsRepository = sneakerheadProductsRepository;
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return sneakerheadProductsRepository.findBySku(sku);
    }

    public List<Product> findAll() {
        return new ArrayList<>(sneakerheadProductsRepository.findAll());
    }

    @Transactional
    public void save(SneakerheadProduct product) {
        sneakerheadProductsRepository.save(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been saved");
    }

    @Transactional
    public void update(int id, SneakerheadProduct product) {
        product.setId(id);

        sneakerheadProductsRepository.save(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been updated");
    }

    /*    Принимает список товаров. Обновляет информацию в базе данных о старых товарах, сохраняет новые.
    Параметр isStopped для прерывания выполнения метода извне.*/
    @Transactional
    public void updateProducts(List<Product> products, Boolean isStopped) {
        for (Product product : products) {
            if (isStopped)
                return;

            SneakerheadProduct sneakerheadProduct = (SneakerheadProduct) product;

            Optional<Product> foundProduct = findBySku(sneakerheadProduct.getSku());

            if (foundProduct.isPresent()) {
                if (!foundProduct.get().equals(sneakerheadProduct))
                    update(foundProduct.get().getId(), sneakerheadProduct);
            } else {
                save(sneakerheadProduct);
            }
        }
    }

    @Transactional
    public void delete(Product product) {
        sneakerheadProductsRepository.delete((SneakerheadProduct) product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been removed");
    }

    /*    Принимает список товаров. Удаляет из базы данных товары, которых нет в списке.
    Параметр isStopped для прерывания выполнения метода извне.*/
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
        return sneakerheadProductsRepository.findAllByNameContainingIgnoreCase(name).orElse(Collections.emptyList());
    }

    @Override
    public String getStoreName() {
        return "Sneakerhead";
    }
}
