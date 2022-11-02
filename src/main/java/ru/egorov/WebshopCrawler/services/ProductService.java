package ru.egorov.WebshopCrawler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egorov.WebshopCrawler.models.Product;
import ru.egorov.WebshopCrawler.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public void save(Product product) {
        productRepository.save(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been saved");
    }

    @Transactional
    public void update(int id, Product product) {
        product.setId(id);

        productRepository.save(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been updated");
    }

/*    Принимает список товаров. Обновляет информацию в базе данных о старых товарах, сохраняет новые.*/
    @Transactional
    public void updateProducts(List<Product> products) {
        for (Product product : products) {
            Optional<Product> foundProduct = findBySku(product.getSku());

            if (foundProduct.isPresent()) {

                if (!foundProduct.get().equals(product))
                    update(foundProduct.get().getId(), product);

            } else {
                save(product);
            }
        }
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);

        System.out.println("product " + product.getBrand() + " " + product.getName() + " has been removed");
    }

/*    Принимает список товаров. Удаляет из базы данных товары, которых нет в списке.*/
    @Transactional
    public void deleteOther(List<Product> products) {
        findAll().stream().filter(p -> !products.contains(p)).forEach(this::delete);
    }
}
