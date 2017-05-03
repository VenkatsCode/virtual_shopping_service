package com.sap.cdom.vrshopping.models;

import java.util.Map;

public class Cart {
	
	@Override
	public String toString() {
		return "Cart [productQuantities=" + productQuantities + "]";
	}

	
	private Map<Long, Long> productQuantities;
	
	public Cart() {
	}
	
	public Map<Long, Long> getProductQuantities() {
		return productQuantities;
	}
	
	public void setProductQuantities(Map<Long, Long> productQuantities) {
		this.productQuantities = productQuantities;
	}
}
