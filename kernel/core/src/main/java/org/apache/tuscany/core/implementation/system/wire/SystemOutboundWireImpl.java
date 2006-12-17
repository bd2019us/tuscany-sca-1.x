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
package org.apache.tuscany.core.implementation.system.wire;

import java.util.Collections;
import java.util.Map;

import org.apache.tuscany.spi.QualifiedName;
import org.apache.tuscany.spi.component.SCAObject;
import org.apache.tuscany.spi.component.TargetException;
import org.apache.tuscany.spi.idl.java.JavaServiceContract;
import org.apache.tuscany.spi.model.Operation;
import org.apache.tuscany.spi.model.ServiceContract;
import org.apache.tuscany.spi.wire.InboundInvocationChain;
import org.apache.tuscany.spi.wire.InboundWire;
import org.apache.tuscany.spi.wire.OutboundInvocationChain;

/**
 * Default implementation of a system outbound wire
 *
 * @version $$Rev$$ $$Date$$
 */
public class SystemOutboundWireImpl implements SystemOutboundWire {
    private String referenceName;
    private QualifiedName targetName;
    private ServiceContract serviceContract;
    private SystemInboundWire targetWire;
    private SCAObject container;
    private Class<?> interfaze;

    public SystemOutboundWireImpl(String referenceName, QualifiedName targetName, Class<?> interfaze) {
        this.referenceName = referenceName;
        this.targetName = targetName;
        serviceContract = new JavaServiceContract(interfaze);
        this.interfaze = interfaze;
    }

    public ServiceContract getServiceContract() {
        return serviceContract;
    }

    public void setServiceContract(ServiceContract serviceContract) {
        this.serviceContract = serviceContract;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public QualifiedName getTargetName() {
        return targetName;
    }

    public void setTargetName(QualifiedName targetName) {
        this.targetName = targetName;
    }

    public Object getTargetService() throws TargetException {
        assert targetWire != null;
        return interfaze.cast(targetWire.getTargetService());
    }

    public void setCallbackInterface(Class<?> interfaze) {
        throw new UnsupportedOperationException();
    }

    public Class<?> getCallbackInterface() {
        throw new UnsupportedOperationException();
    }

    public void addCallbackInterface(Class<?> claz) {
        throw new UnsupportedOperationException();
    }

    public Class[] getImplementedCallbackInterfaces() {
        throw new UnsupportedOperationException();
    }

    public Map<Operation<?>, OutboundInvocationChain> getInvocationChains() {
        return Collections.emptyMap();
    }

    public void addInvocationChain(Operation operation, OutboundInvocationChain chains) {
        throw new UnsupportedOperationException();
    }

    public void addInvocationChains(Map chains) {
        throw new UnsupportedOperationException();
    }

    public Map<Operation<?>, InboundInvocationChain> getTargetCallbackInvocationChains() {
        throw new UnsupportedOperationException();
    }

    public void addTargetCallbackInvocationChains(Map<Operation<?>, InboundInvocationChain> chains) {
        throw new UnsupportedOperationException();
    }

    public void addTargetCallbackInvocationChain(Operation operation, InboundInvocationChain chain) {
        throw new UnsupportedOperationException();
    }

    public void addInterface(Class claz) {
        throw new UnsupportedOperationException();
    }

    public void setTargetWire(InboundWire wire) {
        assert wire instanceof SystemInboundWire : "wire must be a " + SystemInboundWire.class.getName();
        this.targetWire = (SystemInboundWire) wire;
    }

    public boolean isOptimizable() {
        return true;
    }

    public SCAObject getContainer() {
        return container;
    }

    public void setContainer(SCAObject container) {
        this.container = container;
    }

}
