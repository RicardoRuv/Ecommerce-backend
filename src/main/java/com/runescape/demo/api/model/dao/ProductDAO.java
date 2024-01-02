package com.runescape.demo.api.model.dao;

import com.runescape.demo.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long>{

}
