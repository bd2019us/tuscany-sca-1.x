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

package org.apache.tuscany.sca.implementation.java.invocation;

import java.util.List;

import org.apache.tuscany.sca.context.ComponentContextFactory;
import org.apache.tuscany.sca.context.PropertyValueFactory;
import org.apache.tuscany.sca.context.RequestContextFactory;
import org.apache.tuscany.sca.core.invocation.ProxyFactory;
import org.apache.tuscany.sca.databinding.DataBindingExtensionPoint;
import org.apache.tuscany.sca.implementation.java.JavaImplementation;
import org.apache.tuscany.sca.policy.util.PolicyHandlerTuple;
import org.apache.tuscany.sca.provider.ImplementationProvider;
import org.apache.tuscany.sca.provider.ImplementationProviderFactory;
import org.apache.tuscany.sca.runtime.RuntimeComponent;

/**
 * @version $Rev$ $Date$
 */
public class JavaImplementationProviderFactory implements ImplementationProviderFactory<JavaImplementation> {
    private PropertyValueFactory propertyValueObjectFactory;
    private DataBindingExtensionPoint dataBindingRegistry;
    private ProxyFactory proxyService;
    private ComponentContextFactory componentContextFactory;
    private RequestContextFactory requestContextFactory;
    private List<PolicyHandlerTuple> policyHandlerClassNames = null;

    public JavaImplementationProviderFactory(ProxyFactory proxyService,
                                             DataBindingExtensionPoint dataBindingRegistry,
                                             PropertyValueFactory propertyValueObjectFactory,
                                             ComponentContextFactory componentContextFactory,
                                             RequestContextFactory requestContextFactory,
                                             List<PolicyHandlerTuple> policyHandlerClassNames) {
        super();
        this.proxyService = proxyService;
        this.dataBindingRegistry = dataBindingRegistry;
        this.propertyValueObjectFactory = propertyValueObjectFactory;
        this.componentContextFactory = componentContextFactory;
        this.requestContextFactory = requestContextFactory;
        this.policyHandlerClassNames = policyHandlerClassNames;
    }

    public ImplementationProvider createImplementationProvider(RuntimeComponent component,
                                                               JavaImplementation implementation) {
        return new JavaImplementationProvider(component,
                                              implementation,
                                              proxyService,
                                              dataBindingRegistry,
                                              propertyValueObjectFactory,
                                              componentContextFactory,
                                              requestContextFactory,
                                              policyHandlerClassNames);
    }

    public Class<JavaImplementation> getModelType() {
        return JavaImplementation.class;
    }
}
