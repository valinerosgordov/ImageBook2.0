package ru.imagebook.server.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.UserRemoteService;
import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.admin.ProductsResult;
import ru.minogin.core.server.hibernate.Dehibernate;

/**
 * Created by rifat on 11.01.17.
 */
@Service
public class UserRemoteServiceImpl implements UserRemoteService {
    @Autowired
    private ProductRepository productRepository;

    @Dehibernate
    @Transactional
    @Override
    public ProductsResult loadProducts(int offset, int limit, String query) {
        List<Product> products = productRepository.loadProducts(offset, limit, query);
        long productsCount = productRepository.countProducts(query);
        return new ProductsResult(products, productsCount);
    }
}
