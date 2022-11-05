package ru.egorov.StoreCrawler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.StoreCrawler.models.SneakerheadProduct;
import ru.egorov.StoreCrawler.repositories.SneakerheadProductsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SneakerheadProductsService {

    private final SneakerheadProductsRepository sneakerheadProductsRepository;

    @Autowired
    public SneakerheadProductsService(SneakerheadProductsRepository sneakerheadProductsRepository) {
        this.sneakerheadProductsRepository = sneakerheadProductsRepository;
    }

    public Optional<SneakerheadProduct> findBySku(String sku) {
        return sneakerheadProductsRepository.findBySku(sku);
    }

    public List<SneakerheadProduct> findAll() {
        return sneakerheadProductsRepository.findAll();
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

/*    Принимает список товаров. Обновляет информацию в базе данных о старых товарах, сохраняет новые.*/
    @Transactional
    public void updateProducts(List<SneakerheadProduct> products) {
        for (SneakerheadProduct product : products) {
            Optional<SneakerheadProduct> foundProduct = findBySku(product.getSku());

            if (foundProduct.isPresent()) {

                if (!foundProduct.get().equals(product))
                    update(foundProduct.get().getId(), product);

            } else {
                save(product);
            }
        }
    }

    @Transactional
    public void delete(SneakerheadProduct product) {
        sneakerheadProductsRepository.delete(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been removed");
    }

/*    Принимает список товаров. Удаляет из базы данных товары, которых нет в списке.*/
    @Transactional
    public void deleteOther(List<SneakerheadProduct> products) {
        findAll().stream().filter(p -> !products.contains(p)).forEach(this::delete);
    }
}
