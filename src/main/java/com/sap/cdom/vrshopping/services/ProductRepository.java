package com.sap.cdom.vrshopping.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sap.cdom.vrshopping.models.Product;


/**
 * This class extends JpaRepository provided by Springboot and provides access to methods such as save, delete, findOne, findAll etc.
 * @author i849099
 *
 */
public interface ProductRepository extends JpaRepository<Product, Long>{
	
}

