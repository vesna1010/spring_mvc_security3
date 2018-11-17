package com.vesna1010.onlineshop.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.service.ProductService;

public class ProductConverter implements Converter<String, Product> {

	@Autowired
	private ProductService service;

	@Override
	public Product convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findProductById(id));
	}

}
