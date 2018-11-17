package com.vesna1010.onlineshop.converter;

import org.springframework.core.convert.converter.Converter;

public class FloatConverter implements Converter<String, Float> {

	@Override
	public Float convert(String str) {
		try {
			return new Float(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
