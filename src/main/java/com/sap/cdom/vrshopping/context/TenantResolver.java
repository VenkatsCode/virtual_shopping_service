/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sap.cdom.vrshopping.exception.TenantNotProvidedException;
import com.sap.cloud.yaas.servicesdk.patternsupport.traits.YaasAwareTrait;

@Component
public class TenantResolver {

	public static final String HEADER_HYBRIS_TENANT = YaasAwareTrait.Headers.TENANT;
	@Value("${tenant}")
	private String defaultTenant;

	@Autowired
	private ProxyContextResolver proxyContextResolver;
	
	@Autowired
	private MessageSource messageSource;

	public String getCurrentTenant() {
		final String tenant = proxyContextResolver.getTenant();
		final String resolvedTenant = tenant == null ? defaultTenant : tenant;
		if (StringUtils.isEmpty(resolvedTenant)) {
			Object[] args = new Object[] {HEADER_HYBRIS_TENANT};
			throw new TenantNotProvidedException(messageSource.getMessage("tenantresolver.tenantnotprovidedexception", args, LocaleContextHolder.getLocale()));
		}
		return resolvedTenant;
	}
}
