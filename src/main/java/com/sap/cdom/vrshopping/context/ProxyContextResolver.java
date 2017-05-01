package com.sap.cdom.vrshopping.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyContextResolver {

	private String tenant;

	@Autowired
	private RequestContextResolver requestContextResolver;

	/**
	 * TODO: To be updated base on the application context. 
	 */
	public String updateCurrentTenant() {
		this.tenant = this.requestContextResolver.getYaasAwareParameter().getTenant();
		return this.tenant;
	}

	public String getTenant() {		
		return tenant;
	}

}
