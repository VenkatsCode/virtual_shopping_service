/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.exception;



import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


public class InvalidHeaderException extends RuntimeException {


    private final String headerName;
    private final String headerValue;
    private final String expectedPattern;
    
    @Autowired
	private static MessageSource messageSource;


    public InvalidHeaderException(final String headerName, final String headerValue, final Pattern expectedPattern) {
        super(messageSource.getMessage("exception.invalidheader", new Object[] {headerName,headerName, expectedPattern.toString()}, LocaleContextHolder.getLocale()));
        this.headerName = headerName;
        this.headerValue = headerValue;
        this.expectedPattern = expectedPattern.toString();
    }


    public String getHeaderName() {
        return headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public String getExpectedPattern() {
        return expectedPattern;
    }
}
