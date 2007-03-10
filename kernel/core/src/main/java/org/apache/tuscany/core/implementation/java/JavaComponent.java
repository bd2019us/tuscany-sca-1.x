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
package org.apache.tuscany.core.implementation.java;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.osoa.sca.ComponentContext;

import org.apache.tuscany.spi.CoreRuntimeException;
import org.apache.tuscany.spi.ObjectCreationException;
import org.apache.tuscany.spi.component.AtomicComponent;
import org.apache.tuscany.spi.component.ComponentException;
import org.apache.tuscany.spi.component.Reference;
import org.apache.tuscany.spi.component.RegistrationException;
import org.apache.tuscany.spi.component.ScopeContainer;
import org.apache.tuscany.spi.component.Service;
import org.apache.tuscany.spi.component.TargetDestructionException;
import org.apache.tuscany.spi.component.TargetInitializationException;
import org.apache.tuscany.spi.component.TargetInvokerCreationException;
import org.apache.tuscany.spi.component.TargetResolutionException;
import org.apache.tuscany.spi.component.WorkContext;
import org.apache.tuscany.spi.component.InstanceWrapper;
import org.apache.tuscany.spi.event.Event;
import org.apache.tuscany.spi.event.EventFilter;
import org.apache.tuscany.spi.event.RuntimeEventListener;
import org.apache.tuscany.spi.model.Operation;
import org.apache.tuscany.spi.model.PropertyValue;
import org.apache.tuscany.spi.model.Scope;
import org.apache.tuscany.spi.model.physical.PhysicalOperationDefinition;
import org.apache.tuscany.spi.wire.TargetInvoker;
import org.apache.tuscany.spi.wire.Wire;

import org.apache.tuscany.core.component.InstanceFactory;

/**
 * @version $Revision$ $Date$
 */
public class JavaComponent<T> implements AtomicComponent {

    // Instance factory class
    private Class<InstanceFactory<T>> instanceFactoryClass; //NOPMD
    private Class<T> implementationClass;
    private WorkContext workContext;

    // Scope container
    private ScopeContainer scopeContainer; //NOPMD

    /**
     * Injects the instance factory class.
     *
     * @param instanceFactoryClass Instance factory class.
     */
    public void setInstanceFactoryClass(Class<InstanceFactory<T>> instanceFactoryClass) {
        this.instanceFactoryClass = instanceFactoryClass;
    }

    /**
     * Injects the scope container.
     *
     * @param scopeContainer Scope container.
     */
    public void setScopeContainer(ScopeContainer scopeContainer) {
        throw new UnsupportedOperationException();
    }


    /**
     * Injects the work context.
     *
     * @param workContext the work context.
     */
    public void setWorkContext(WorkContext workContext) {
        this.workContext = workContext;
    }

    /**
     * Sets the component implementation class.
     *
     * @param implementationClass the component implementation class.
     */
    public void setImplementationClass(Class<T> implementationClass) {
        this.implementationClass = implementationClass;
    }
    
    public void attachCallbackWire(Wire wire) {
        throw new UnsupportedOperationException();
    }

    public void attachWire(Wire wire) {
        throw new UnsupportedOperationException();
    }

    public void attachWires(List<Wire> wires) {
        throw new UnsupportedOperationException();
    }

    public ComponentContext getComponentContext() {
        throw new UnsupportedOperationException();
    }

    public Map<String, PropertyValue<?>> getDefaultPropertyValues() {
        throw new UnsupportedOperationException();
    }

    public Reference getReference(String name) {
        throw new UnsupportedOperationException();
    }

    public Scope getScope() {
        throw new UnsupportedOperationException();
    }

    public Service getService(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Wire> getWires(String name) {
        throw new UnsupportedOperationException();
    }

    public boolean isOptimizable() {
        throw new UnsupportedOperationException();
    }

    public void register(Service service) throws RegistrationException {
        throw new UnsupportedOperationException();
    }

    public void register(Reference reference) throws RegistrationException {
        throw new UnsupportedOperationException();
    }

    public void setDefaultPropertyValues(Map<String, PropertyValue<?>> defaultPropertyValues) {
        throw new UnsupportedOperationException();
    }

    public TargetInvoker createTargetInvoker(String targetName, Operation operation)
        throws TargetInvokerCreationException {
        throw new UnsupportedOperationException();
    }

    public TargetInvoker createTargetInvoker(String targetName, PhysicalOperationDefinition operation)
        throws TargetInvokerCreationException {
        List<String> params = operation.getParameters();
        Class<?>[] paramTypes = new Class<?>[params.size()];
        ClassLoader loader = implementationClass.getClassLoader();
        assert loader != null;
        for (int i = 0; i < params.size(); i++) {
            String param = params.get(i);
            try {
                paramTypes[i] = loader.loadClass(param);
            } catch (ClassNotFoundException e) {
                throw new TypeNotFoundException(operation, e);
            }
        }
        Method method;
        try {
            method = implementationClass.getMethod(operation.getName(), paramTypes);
        } catch (NoSuchMethodException e) {
            throw new TargetMethodNotFoundException(operation);
        }
        return new JavaTargetInvoker(method, this, workContext);
    }

    public URI getUri() {
        throw new UnsupportedOperationException();
    }

    public void addListener(RuntimeEventListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addListener(EventFilter filter, RuntimeEventListener listener) {
        throw new UnsupportedOperationException();
    }

    public void publish(Event object) {
        throw new UnsupportedOperationException();
    }

    public void removeListener(RuntimeEventListener listener) {
        throw new UnsupportedOperationException();
    }

    public int getLifecycleState() {
        throw new UnsupportedOperationException();
    }

    public void start() throws CoreRuntimeException {
        throw new UnsupportedOperationException();
    }

    public void stop() throws CoreRuntimeException {
        throw new UnsupportedOperationException();
    }

    public boolean isEagerInit() {
        return false;
    }

    public boolean isDestroyable() {
        return false;
    }

    public int getInitLevel() {
        return 0;
    }

    public long getMaxIdleTime() {
        return 0;
    }

    public long getMaxAge() {
        return 0;
    }

    public void init(Object instance) throws TargetInitializationException {

    }

    public void destroy(Object instance) throws TargetDestructionException {

    }

    public Object createInstance() throws ObjectCreationException {
        return null;
    }

    public void removeInstance() throws ComponentException {

    }

    public InstanceWrapper<?> createInstanceWrapper() throws ObjectCreationException {
        throw new UnsupportedOperationException();
    }

    public Object getTargetInstance() throws TargetResolutionException {
        return scopeContainer.getInstance(this);
    }

    public Object getAssociatedTargetInstance() throws TargetResolutionException {
        return scopeContainer.getAssociatedInstance(this);
    }
}
