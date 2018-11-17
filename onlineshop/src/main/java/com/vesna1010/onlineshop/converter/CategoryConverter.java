package com.vesna1010.onlineshop.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.service.CategoryService;

public class CategoryConverter implements Converter<String, Category> {

	@Autowired
	private CategoryService service;

	@Override
	public Category convert(String id) {
		return ((id == null || id.isEmpty()) ? null : service.findCategoryById(Long.valueOf(id)));
	}

}