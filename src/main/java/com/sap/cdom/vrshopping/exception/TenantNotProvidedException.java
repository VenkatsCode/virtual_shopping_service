/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.exception;

public class TenantNotProvidedException extends RuntimeException {
    public TenantNotProvidedException(final String message) {
        super(message);
    }
}
