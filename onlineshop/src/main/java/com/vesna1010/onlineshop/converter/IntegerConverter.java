package com.vesna1010.onlineshop.converter;

import org.springframework.core.convert.converter.Converter;

public class IntegerConverter implements Converter<String, Integer> {

	@Override
	public Integer convert(String str) {
		try {
			return new Integer(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
