package org.aadi.ecommerce.product.repository;

import org.aadi.ecommerce.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, Integer> {

}
