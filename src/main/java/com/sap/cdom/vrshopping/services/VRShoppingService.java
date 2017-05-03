package com.sap.cdom.vrshopping.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cdom.vrshopping.models.Cart;
import com.sap.cdom.vrshopping.models.Order;
import com.sap.cdom.vrshopping.models.Product;

public class VRShoppingService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Product> readExtension() {
		return productRepository.findAll();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Product readById(Long id) {
		return productRepository.findOne(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Product create(Product product) {
		Product savedExtension = productRepository.save(product);
		return savedExtension;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Product change(Long id, Product product) {
		return productRepository.save(product);
	}

	public void delete(Long id) {
		productRepository.delete(id);
	}

	public Order order(Cart cart) {
		Long quantity = 0L;
		Float totalPrice = 0.0f;
		for (Map.Entry<Long, Long> entry : cart.getProductQuantities().entrySet())
		{
			Product product = readById(entry.getKey());
			totalPrice = totalPrice + (product.getPrice()*entry.getValue());
			quantity = quantity + entry.getValue();
		}
		Order order = new Order();
		order.setQuantity(quantity);
		order.setTotal(totalPrice);
		order.setDate(new java.sql.Date(System.currentTimeMillis()));
		return orderRepository.save(order);
	}

	public Order readOrderById(Long id) {
		return orderRepository.findOne(id);
	}

	public List<Order> readAllOrders() {
		return orderRepository.findAll();
	}

}
