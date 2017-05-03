package com.sap.cdom.vrshopping.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.sap.cdom.vrshopping.jpa.MultiTenantJpaTransactionManager;
import com.sap.cdom.vrshopping.services.VRShoppingService;


@Configuration
@EnableRetry
@EnableJpaRepositories(basePackages = {"com.sap.cdom.vrshopping"})
public class JPAConfiguration extends JpaBaseConfiguration{

	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
		final JpaTransactionManager transactionManager = new MultiTenantJpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
	
	@Bean
    RestOperations restOperations() {
        return new RestTemplate();
    }
	
	@Bean
	VRShoppingService vrShoppingService() {
        return new VRShoppingService();
    }
	
	protected JPAConfiguration(DataSource dataSource, JpaProperties properties,
			ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
		super(dataSource, properties, jtaTransactionManagerProvider);
	}

	
	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new EclipseLinkJpaVendorAdapter();
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		final HashMap<String, Object> map = new HashMap<>();
		map.put(PersistenceUnitProperties.WEAVING, detectWeavingMode());
		return map;
	}

	private String detectWeavingMode() {
		return InstrumentationLoadTimeWeaver.isInstrumentationAvailable() ? "true" : "static";
	}
	
}
