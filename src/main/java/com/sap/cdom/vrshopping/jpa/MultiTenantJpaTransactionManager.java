/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.sap.cdom.vrshopping.context.TenantResolver;

import javax.persistence.EntityManager;

public class MultiTenantJpaTransactionManager extends JpaTransactionManager {

    public static final String TENANT_DESCRIMINATOR_NAME = "eclipselink.tenant-id";

    @Autowired
    private TenantResolver tenantResolver;
    
    @Autowired
	private MessageSource messageSource;

    @Override
    protected void doBegin(final Object transaction, final TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        final EntityManagerHolder entityManagerHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(getEntityManagerFactory());
        final EntityManager entityManager = entityManagerHolder.getEntityManager();
        final String resolvedTenant = tenantResolver.getCurrentTenant();
        final String currentTenantSet = (String) entityManager.getProperties().get(TENANT_DESCRIMINATOR_NAME);
        //check whether there is a conflict due to concurrent access to entity manager
        if (currentTenantSet != null && !currentTenantSet.equals(resolvedTenant))
        {
        	Object[] args = new Object[] {currentTenantSet,resolvedTenant};
            throw new IllegalStateException(messageSource.getMessage("multitenantjpatransactionmanager.illegalstateexception", args, LocaleContextHolder.getLocale()));
        }
        entityManager.setProperty(TENANT_DESCRIMINATOR_NAME, resolvedTenant);
    }
}
