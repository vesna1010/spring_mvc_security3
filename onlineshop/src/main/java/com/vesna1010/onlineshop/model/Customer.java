package com.vesna1010.onlineshop.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "CUSTOMERS")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String fullName;
	private String email;
	private Address address;
	private String telephone;
	private Float totalAccount = 0f;
	private Map<Product, Integer> products = new HashMap<>();
	private LocalDate date = LocalDate.now();

	public Customer() {
	}

	public Customer(Map<Product, Integer> products) {
		this.products = products;
	}

	public Customer(String fullName, String email, Address address, String telephone, LocalDate date) {
		this.fullName = fullName;
		this.email = email;
		this.address = address;
		this.telephone = telephone;
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Pattern(regexp = "^[a-zA-Z\\s]{5,}$", message = "{customer.fullName}")
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@NotBlank(message = "{customer.email.not_blank}")
	@Email(message = "{customer.email.valid}")
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Valid
	@Embedded
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Pattern(regexp = "^(00|\\+)\\d{3}\\s?\\d{2}\\s?\\d{3}\\s?\\d{3,4}$", message = "{customer.telephone}")
	@Column(name = "TELEPHON")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@NotEmpty(message = "{customer.products}")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CUSTOMERS_PRODUCTS", 
	                 joinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID"))
	@MapKeyColumn(name = "PRODUCT_ID", columnDefinition = "PRODUCT_CODE")
	@Column(name = "NUMBER_OF_PRODUCTS")
	@Cascade(value = { CascadeType.ALL })
	public Map<Product, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<Product, Integer> products) {
		this.products = products;
	}
	
	public void addProducts(Product product, Integer numberOfProducts) {
		this.products.put(product, numberOfProducts);
	}

	@Column(name = "TOTAL_ACCOUNT")
	public Float getTotalAccount() {
		this.totalAccount = 0f;
		Set<Product> products = this.products.keySet();

		for (Product product : products) {
			this.totalAccount += product.getPrice() * this.products.get(product);
		}
		
		return this.totalAccount;
	}

	public void setTotalAccount(Float totalAccount) {
		this.totalAccount = totalAccount;
	}

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Column(name = "DATE")
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
