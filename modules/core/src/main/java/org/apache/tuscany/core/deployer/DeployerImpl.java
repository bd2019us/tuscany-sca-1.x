/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.tuscany.core.deployer;

import java.net.URI;
import java.util.Collection;

import javax.xml.stream.XMLInputFactory;

import org.apache.tuscany.core.resolver.AutowireResolver;
import org.apache.tuscany.spi.Monitor;
import org.apache.tuscany.spi.Scope;
import org.apache.tuscany.spi.builder.Builder;
import org.apache.tuscany.spi.builder.BuilderException;
import org.apache.tuscany.spi.builder.BuilderInstantiationException;
import org.apache.tuscany.spi.builder.BuilderRegistry;
import org.apache.tuscany.spi.builder.Connector;
import org.apache.tuscany.spi.component.Component;
import org.apache.tuscany.spi.component.ComponentManager;
import org.apache.tuscany.spi.component.RegistrationException;
import org.apache.tuscany.spi.component.SCAObject;
import org.apache.tuscany.spi.component.ScopeContainer;
import org.apache.tuscany.spi.component.ScopeContainerMonitor;
import org.apache.tuscany.spi.component.ScopeRegistry;
import org.apache.tuscany.spi.deployer.Deployer;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.resolver.ResolutionException;
import org.osoa.sca.annotations.Reference;

/**
 * Default implementation of Deployer.
 * 
 * @version $Rev$ $Date$
 */
public class DeployerImpl implements Deployer {
    private XMLInputFactory xmlFactory;
    private Builder builder;
    private ScopeContainerMonitor monitor;
    private AutowireResolver resolver;
    private Connector connector;
    private ComponentManager componentManager;
    private ScopeRegistry scopeRegistry;

    public DeployerImpl(XMLInputFactory xmlFactory,
                        Builder builder,
                        ComponentManager componentManager,
                        AutowireResolver resolver,
                        Connector connector) {
        this.xmlFactory = xmlFactory;
        this.builder = builder;
        this.componentManager = componentManager;
        this.resolver = resolver;
        this.connector = connector;
    }

    public DeployerImpl() {
        xmlFactory = XMLInputFactory.newInstance("javax.xml.stream.XMLInputFactory", getClass().getClassLoader());
    }

    @Reference
    public void setBuilder(BuilderRegistry builder) {
        this.builder = builder;
    }

    @Monitor
    public void setMonitor(ScopeContainerMonitor monitor) {
        this.monitor = monitor;
    }

    @Reference
    public void setResolver(AutowireResolver resolver) {
        this.resolver = resolver;
    }

    @Reference
    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Reference
    public void setComponentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    @Reference
    public void setScopeRegistry(ScopeRegistry scopeRegistry) {
        this.scopeRegistry = scopeRegistry;
    }

    public Collection<Component> deploy(Component parent, org.apache.tuscany.assembly.Component componentDef)
        throws BuilderException, ResolutionException {
        @SuppressWarnings("unchecked")
        ScopeContainer<URI> scopeContainer = scopeRegistry.getScopeContainer(Scope.COMPOSITE);
        URI groupId = parent != null ? parent.getUri() : URI.create("/");
        DeploymentContext deploymentContext = new RootDeploymentContext(null, null, groupId, xmlFactory,
                                                                        scopeContainer, false);
        // load the model
        // load(parent, componentDefinition, deploymentContext);
        // resolve autowires
        resolver.resolve(null, componentDef);
        // build runtime artifacts
        build(parent, componentDef, deploymentContext);
        /*
         * // create a listener so the scope container is shutdown when the
         * top-level composite stops RuntimeEventListener listener = new
         * RuntimeEventListener() { public void onEvent(Event event) {
         * scopeContainer.onEvent(event); if (event instanceof ComponentStop) {
         * scopeContainer.stop(); } } }; component.addListener(listener);
         */

        Collection<Component> components = deploymentContext.getComponents().values();
        for (Component toRegister : components) {
            try {
                componentManager.register(toRegister);
            } catch (RegistrationException e) {
                throw new BuilderInstantiationException("Error registering component", e);
            }
        }
        connector.connect(groupId, componentDef);
        return components;
    }

    /**
     * Build the runtime context for a loaded componentDefinition.
     * 
     * @param parent the context that will be the parent of the new sub-context
     * @param componentDefinition the componentDefinition being deployed
     * @param deploymentContext the current deployment context
     * @return the new runtime context
     */
    protected SCAObject build(Component parent,
                              org.apache.tuscany.assembly.Component componentDefinition,
                              DeploymentContext deploymentContext) throws BuilderException {
        return builder.build(componentDefinition, deploymentContext);
    }

}
