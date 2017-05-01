/*
 * © 201X SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cdom.vrshopping.jpa;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.AggregateObjectMapping;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.sessions.Session;

import java.util.Map;

/**
 * This is a customizer of the EclipseLink Session.
 * EclipseLink comes with no similar naming strategy as hibernate to resolve duplicate attributes names resulting from emmbedded entities (@Embedded, @Embeddable)
 * This customizer resolves those duplicates by adding the attribute name in the embedding entity as a prefix to the attribute name within the embeddable entity.
 * <p>
 * Example:
 * Entity Subscription embeds attributes customer and provider both referencing @Embeddable entity ResourceLocation having attributes id and link. The resulting field names in the subscription db table are:
 * - CUSTOMER_ID, CUSTOMER_LINK and
 * - PROVIDER_ID, PROVIDER_LINK
 * <p>
 * An alternative way to solve this would be to use @AttributeOverrides - but this is cumbersome, since it has to be applied for each and every embedded entity.
 * <p>
 * In order to make this class effective it has to be setup in application.properties:
 * spring.jpa.properties.eclipselink.session.customizer=com.sap.cec.yaas.aspectregistry.jpa.EmbeddedFieldNamesSessionCustomizer
 */
public class EmbeddedFieldNamesSessionCustomizer implements SessionCustomizer {

    @Override
    public void customize(final Session session) throws Exception {
        final Map<Class, ClassDescriptor> descriptors = session.getDescriptors();
        for (final ClassDescriptor classDescriptor : descriptors.values()) {
            classDescriptor.getMappings().stream().filter(DatabaseMapping::isAggregateObjectMapping).forEach(databaseMapping -> {
                final AggregateObjectMapping aggregateObjectMapping = (AggregateObjectMapping) databaseMapping;
                final Map<String, DatabaseField> aggregateToSourceFields = aggregateObjectMapping.getAggregateToSourceFields();
                final ClassDescriptor refDescriptor = descriptors.get(aggregateObjectMapping.getReferenceClass());
                refDescriptor.getMappings().stream().filter(DatabaseMapping::isDirectToFieldMapping).forEach(refMapping -> {
                    final DirectToFieldMapping refDirectMapping = (DirectToFieldMapping) refMapping;
                    final String refFieldName = refDirectMapping.getField().getName();
                    if (!aggregateToSourceFields.containsKey(refFieldName)) {
                        final DatabaseField mappedField = refDirectMapping.getField().clone();
                        mappedField.setName(aggregateObjectMapping.getAttributeName() + "_" + mappedField.getName());
                        aggregateToSourceFields.put(refFieldName, mappedField);
                    }
                });
            });
        }
    }

}
