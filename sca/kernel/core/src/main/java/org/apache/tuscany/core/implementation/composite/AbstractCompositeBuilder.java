package org.apache.tuscany.core.implementation.composite;

import org.apache.tuscany.spi.builder.BuilderException;
import org.apache.tuscany.spi.builder.BuilderInstantiationException;
import org.apache.tuscany.spi.component.Component;
import org.apache.tuscany.spi.component.ComponentRegistrationException;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.component.Reference;
import org.apache.tuscany.spi.component.SCAObject;
import org.apache.tuscany.spi.component.Service;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.extension.ComponentBuilderExtension;
import org.apache.tuscany.spi.model.ComponentDefinition;
import org.apache.tuscany.spi.model.CompositeComponentType;
import org.apache.tuscany.spi.model.CompositeReferenceDefinition;
import org.apache.tuscany.spi.model.Implementation;
import org.apache.tuscany.spi.model.ModelObject;
import org.apache.tuscany.spi.model.AbstractReferenceDefinition;
import org.apache.tuscany.spi.model.ServiceDefinition;

/**
 * Abstract builder for composites
 * 
 * @version $Rev$ $Date$
 */
public abstract class AbstractCompositeBuilder<T extends Implementation<CompositeComponentType>> extends
    ComponentBuilderExtension<T> {

    public CompositeComponent build(CompositeComponent parent,
                                    CompositeComponent component,
                                    CompositeComponentType<?, ?, ?> componentType,
                                    DeploymentContext deploymentContext) throws BuilderException {

        for (ComponentDefinition<? extends Implementation<?>> definition : componentType.getComponents().values()) {
            try {
                Component child = builderRegistry.build(component, definition, deploymentContext);
                component.register(child);
            } catch (ComponentRegistrationException e) {
                throw new BuilderInstantiationException("Error registering component", e);
            }
        }
        for (ServiceDefinition definition : componentType.getServices().values()) {
            try {
                Service service = builderRegistry.build(component, definition, deploymentContext);
                component.register(service);
            } catch (ComponentRegistrationException e) {
                throw new BuilderInstantiationException("Error registering service", e);
            }
        }
        for (CompositeReferenceDefinition definition : componentType.getReferences().values()) {
            try {
                Reference child = builderRegistry.build(component, definition, deploymentContext);
                component.register(child);
            } catch (ComponentRegistrationException e) {
                throw new BuilderInstantiationException("Error registering reference", e);
            }
        }
        return component;
    }

}
