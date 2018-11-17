package com.vesna1010.onlineshop.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	private String street;
	private String zipCode;
	private String city;
	private String state;

	public Address() {
	}

	public Address(String street, String zipCode, String city, String state) {
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.state = state;
	}

	@Pattern(regexp = "^[a-zA-Z0-9\\s]{3,}$", message = "{address.street}")
	@Column(name = "ADDRESS_STREET")
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Pattern(regexp = "^\\d{5}$", message = "{address.zipCode}")
	@Column(name = "ADDRESS_ZIP_CODE")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{3,}$", message = "{address.city}")
	@Column(name = "ADDRESS_CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{3,}$", message = "{address.state}")
	@Column(name = "ADDRESS_STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(street);
		sb.append("\n");
		sb.append(zipCode);
		sb.append(" " + city);
		sb.append("\n");
		sb.append(state);
			
		return sb.toString();
	}

}
