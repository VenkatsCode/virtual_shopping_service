/*
 * Copyright (c) 2016 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.context;



import com.sap.cdom.vrshopping.exception.InvalidHeaderException;
import com.sap.cloud.yaas.servicesdk.patternsupport.traits.YaasAwareTrait;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.regex.Pattern;

@Component
public class RequestContextResolver {

    private static final String ATTRIBUTE_YAAS_AWARE_PARAMETER = "yaas-aware";
    private static final String ATTRIBUTE_REQUEST_TIMESTAMP = "request-timestamp";

    public YaasAwareParameter getYaasAwareParameter() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //if (requestAttributes == null)
         //   throw new IllegalStateException("No request attributes found.");
        YaasAwareParameter yaasAwareParameter = (YaasAwareParameter) requestAttributes.getAttribute(ATTRIBUTE_YAAS_AWARE_PARAMETER, RequestAttributes.SCOPE_REQUEST);
        if (yaasAwareParameter == null) {
            yaasAwareParameter = new YaasAwareParameter();
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            yaasAwareParameter.tenant = request.getHeader(YaasAwareTrait.Headers.TENANT);
            checkPattern(yaasAwareParameter.tenant, YaasAwareTrait.Headers.TENANT_PATTERN, YaasAwareTrait.Headers.TENANT);
            yaasAwareParameter.client = request.getHeader(YaasAwareTrait.Headers.CLIENT);
            checkPattern(yaasAwareParameter.client, YaasAwareTrait.Headers.CLIENT_PATTERN, YaasAwareTrait.Headers.CLIENT);
            yaasAwareParameter.hop = request.getHeader(YaasAwareTrait.Headers.HOP);
            yaasAwareParameter.requestId = request.getHeader(YaasAwareTrait.Headers.REQUEST_ID);
            yaasAwareParameter.scopes = request.getHeader(YaasAwareTrait.Headers.SCOPES);
            checkPattern(yaasAwareParameter.scopes, YaasAwareTrait.Headers.SCOPES_PATTERN, YaasAwareTrait.Headers.SCOPES);
            yaasAwareParameter.user = request.getHeader(YaasAwareTrait.Headers.USER);
            requestAttributes.setAttribute(ATTRIBUTE_YAAS_AWARE_PARAMETER, yaasAwareParameter, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.setRequestAttributes(requestAttributes);
        }
        return yaasAwareParameter;
    }

    private void checkPattern(final String value, final Pattern pattern, final String header) {
        if (!StringUtils.isEmpty(value) && !pattern.matcher(value).matches())
            throw new InvalidHeaderException(header, value, pattern);
    }

    public Instant getRequestTimestamp() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Instant requestTimestamp = (Instant) requestAttributes.getAttribute(ATTRIBUTE_REQUEST_TIMESTAMP, RequestAttributes.SCOPE_REQUEST);
        if (requestTimestamp == null) {
            requestTimestamp = Instant.now();
            requestAttributes.setAttribute(ATTRIBUTE_REQUEST_TIMESTAMP, requestTimestamp, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.setRequestAttributes(requestAttributes);
        }
        return requestTimestamp;
    }

    public static final class YaasAwareParameter {
        private String tenant;
        private String client;
        private String hop;
        private String requestId;
        private String scopes;
        private String user;

        public String getTenant() {
            return tenant;
        }

        public void setTenant(String tenant) {
            this.tenant = tenant;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getHop() {
            return hop;
        }

        public void setHop(String hop) {
            this.hop = hop;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getScopes() {
            return scopes;
        }

        public void setScopes(String scopes) {
            this.scopes = scopes;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }


}

