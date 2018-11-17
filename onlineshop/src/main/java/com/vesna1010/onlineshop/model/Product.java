package com.vesna1010.onlineshop.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;
	private Float price;
	private Category category;
	private byte[] image;
	private Integer stocks;

	public Product() {
	}

	public Product(String id, String name, String description, Float price, Category category, byte[] image,
			Integer stocks) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.image = image;
		this.stocks = stocks;
	}

	@Id
	@Pattern(regexp = "\\d{13}", message = "{product.id}")
	@Column(name = "PRODUCT_CODE")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{5,}$", message = "{product.name}")
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{10,}$", message = "{product.description}")
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull(message = "{product.price.not_null}")
	@DecimalMin(value = "0.01", message = "{product.price.decimal_min}")
	@Column(name = "PRICE")
	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@NotNull(message = "{product.category}")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORY_ID")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@NotNull(message = "{product.image}")
	@Lob
	@Column(name = "IMAGE")
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@NotNull(message = "{product.stocks.not_null}")
	@PositiveOrZero(message = "{product.stocks.positive_or_zero}")
	@Column(name = "STOCKS")
	public Integer getStocks() {
		return stocks;
	}

	public void setStocks(Integer stocks) {
		this.stocks = stocks;
	}

	@Transient
	public CommonsMultipartFile getFile() {
		return null;
	}

	public void setFile(CommonsMultipartFile file) {
		if (!file.isEmpty()) {
			this.setImage(file.getBytes());
		}
	}

	@Transient
	public String getImageString() throws UnsupportedEncodingException {
		return new String(Base64.encode(this.image), "UTF-8");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
