package com.sap.cdom.vrshopping.models;

import java.util.Map;

public class Cart {
	
	private Long id;
	
	private Map<Long, Long> productQuantities;
	
	public Cart() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Map<Long, Long> getProductQuantities() {
		return productQuantities;
	}
	
	public void setProductQuantities(Map<Long, Long> productQuantities) {
		this.productQuantities = productQuantities;
	}

	
}
