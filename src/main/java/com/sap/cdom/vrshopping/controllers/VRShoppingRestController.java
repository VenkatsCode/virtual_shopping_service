/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cdom.vrshopping.models.Cart;
import com.sap.cdom.vrshopping.models.Order;
import com.sap.cdom.vrshopping.models.Product;
import com.sap.cdom.vrshopping.services.VRShoppingService;

@RestController
@RequestMapping("/vr")
public class VRShoppingRestController {

	@Autowired
	private VRShoppingService vrShoppingService;

	@RequestMapping(value = "/product", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity get() {
		return ResponseEntity.ok(vrShoppingService.readExtension());
	}

	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity getById(@PathVariable("id") final Long id) {
		return ResponseEntity.ok(vrShoppingService.readById(id));
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST, consumes = {"application/json"},
		produces = {"application/json"})
	public ResponseEntity post(@RequestBody final Product product) {
		System.out.println("in post " + product.toString());
		Product createdProduct = vrShoppingService.create(product);
		final URI uriOfCreatedExtension = ServletUriComponentsBuilder.fromCurrentContextPath().path("product/{id}")
			.buildAndExpand(createdProduct.getId()).toUri();
		return ResponseEntity.created(uriOfCreatedExtension).build();
	}

	@RequestMapping(value = "/product/{id}", method = RequestMethod.PUT, consumes = {"application/json"})
	public ResponseEntity put(@PathVariable("id") final Long id, @RequestBody final Product product) {
		Product createdProduct = vrShoppingService.change(id, product);
		final URI uriOfSavedExtension = ServletUriComponentsBuilder.fromCurrentContextPath().path("product/{id}")
			.buildAndExpand(createdProduct.getId()).toUri();
		return ResponseEntity.created(uriOfSavedExtension).build();
	}

	@RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable("id") final Long id) {
		vrShoppingService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
		produces = {"application/json"})
	public ResponseEntity priceAndOrder(@RequestParam final String productQuantities) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("productQuantities: "+productQuantities);
		Cart cart = new Cart();
		HashMap<Long, Long> products = new HashMap<>();
		JSONArray jsonArray = new JSONArray(productQuantities);
		System.out.println("jsonArray: "+jsonArray.toString());
	    for (int i = 0; i < jsonArray.length(); i++) {            
	    	JSONObject jsonObject = jsonArray.optJSONObject(i);
	    	HashMap<String, Object> productsNew = new ObjectMapper().readValue(jsonObject.toString(), HashMap.class);
	    	System.out.println("productsNew: "+productsNew);
	    	System.out.println("productsNew.getid: "+productsNew.get("id"));
	    	System.out.println("productsNew.getvalue: "+productsNew.get("value"));
	    	products.put(Long.parseLong((productsNew.get("id")).toString()), Long.parseLong((productsNew.get("value")).toString()));
	    	
	    }
	    cart.setProductQuantities(products);
		Order order = vrShoppingService.order(cart);
		final URI uriOfCreatedOrder = ServletUriComponentsBuilder.fromCurrentContextPath().path("order/{id}")
			.buildAndExpand(order.getId()).toUri();
		return ResponseEntity.created(uriOfCreatedOrder).build();
	}
	
	@RequestMapping(value = "/order/{id}", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity getOrderById(@PathVariable("id") final Long id) {
		return ResponseEntity.ok(vrShoppingService.readOrderById(id));
	}
	
	@RequestMapping(value = "/order", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity getAllOrders() {
		return ResponseEntity.ok(vrShoppingService.readAllOrders());
	}

}
