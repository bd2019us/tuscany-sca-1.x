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

package org.apache.tuscany.core.databinding.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.apache.tuscany.spi.ObjectCreationException;
import org.apache.tuscany.spi.component.ServiceBinding;
import org.apache.tuscany.spi.component.TargetResolutionException;
import org.apache.tuscany.spi.databinding.DataBindingRegistry;
import org.apache.tuscany.spi.extension.AtomicComponentExtension;
import org.apache.tuscany.spi.idl.java.JavaServiceContract;
import org.apache.tuscany.spi.model.Operation;
import org.apache.tuscany.spi.model.ServiceContract;
import org.apache.tuscany.spi.wire.InboundInvocationChain;
import org.apache.tuscany.spi.wire.InboundWire;
import org.apache.tuscany.spi.wire.Interceptor;
import org.apache.tuscany.spi.wire.OutboundInvocationChain;
import org.apache.tuscany.spi.wire.OutboundWire;
import org.apache.tuscany.spi.wire.TargetInvoker;

import junit.framework.TestCase;
import org.apache.tuscany.core.wire.InboundInvocationChainImpl;
import org.apache.tuscany.core.wire.OutboundInvocationChainImpl;
import org.apache.tuscany.core.wire.SynchronousBridgingInterceptor;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

/**
 * Testcase for testing if the PassByValueWireProcessor adds the PassByValueInterceptor to the invocation chains and
 * also ensure that the outbound and inbound chain of interceptors are linked after this insertion
 *
 * @version $Rev$ $Date$
 */
public class PassByValueWirePostProcessorTestCase extends TestCase {
    private PassByValueWirePostProcessor processor;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.processor = new PassByValueWirePostProcessor();
        DataBindingRegistry dataBindingRegistry = createMock(DataBindingRegistry.class);
        processor.setDataBindingRegistry(dataBindingRegistry);
    }

    public void testProcessInclusionOfInterceptor() {

        InboundWire inboundWire = createMock(InboundWire.class);
        OutboundWire outboundWire = createMock(OutboundWire.class);

        ServiceContract<Type> serviceContract = new JavaServiceContract(null);
        serviceContract.setRemotable(true);
        Map<Operation<?>, InboundInvocationChain> inChainsMap =
            new Hashtable<Operation<?>, InboundInvocationChain>();

        Operation<Type> operation1 = new Operation<Type>("testMethod", null, null, null);
        operation1.setServiceContract(serviceContract);
        InboundInvocationChainImpl inChain = new InboundInvocationChainImpl(operation1);
        inChainsMap.put(operation1, inChain);

        AtomicComponentExtension componentExtn = new FooComponent();
        componentExtn.setPassByReferenceMethods(new ArrayList<String>());

        Map<Operation<?>, OutboundInvocationChain> outChainsMap =
            new Hashtable<Operation<?>, OutboundInvocationChain>();
        OutboundInvocationChainImpl outChain = new OutboundInvocationChainImpl(operation1);
        outChainsMap.put(operation1, outChain);

        expect(inboundWire.getServiceContract()).andReturn(serviceContract);
        expect(inboundWire.getContainer()).andReturn(componentExtn).anyTimes();
        expect(outboundWire.getContainer()).andReturn(componentExtn).anyTimes();
        expect(inboundWire.getServiceContract()).andReturn(serviceContract);
        expect(inboundWire.getInvocationChains()).andReturn(inChainsMap);
        expect(outboundWire.getServiceContract()).andReturn(serviceContract).times(2);
        expect(outboundWire.getInvocationChains()).andReturn(outChainsMap).times(2);

        Interceptor inInterceptor = createMock(Interceptor.class);
        Interceptor outInterceptor = createMock(Interceptor.class);
        inChain.addInterceptor(0, inInterceptor);
        outChain.addInterceptor(0, outInterceptor);
        outChain.addInterceptor(new SynchronousBridgingInterceptor(inChain.getHeadInterceptor()));

        EasyMock.replay(inboundWire, outboundWire);
        processor.process(outboundWire, inboundWire);

        assertEquals(true, inChain.getHeadInterceptor() instanceof PassByValueInterceptor);
        assertEquals(true,
            outChain.getTailInterceptor().getNext() instanceof PassByValueInterceptor);
        assertEquals(true, outChain.getTailInterceptor().getNext().equals(
            inChain.getHeadInterceptor()));

    }

    public void testProcessExclusionOfInterceptorWhenAllowsPassByReference() {
        InboundWire inboundWire = createMock(InboundWire.class);
        OutboundWire outboundWire = createMock(OutboundWire.class);

        ServiceContract<Type> serviceContract = new JavaServiceContract(null);
        serviceContract.setRemotable(true);
        Map<Operation<?>, InboundInvocationChain> inChainsMap =
            new Hashtable<Operation<?>, InboundInvocationChain>();

        Operation<?> operation1 = new Operation<Type>("testMethod", null, null, null);
        InboundInvocationChainImpl inChain = new InboundInvocationChainImpl(operation1);
        inChainsMap.put(operation1, inChain);

        AtomicComponentExtension componentExtn = new FooComponent();
        componentExtn.setAllowsPassByReference(true);
        componentExtn.setPassByReferenceMethods(new ArrayList<String>());


        Map<Operation<?>, OutboundInvocationChain> outChainsMap =
            new Hashtable<Operation<?>, OutboundInvocationChain>();
        OutboundInvocationChainImpl outChain = new OutboundInvocationChainImpl(operation1);
        outChainsMap.put(operation1, outChain);

        expect(inboundWire.getServiceContract()).andReturn(serviceContract);
        expect(inboundWire.getContainer()).andReturn(componentExtn).anyTimes();
        expect(outboundWire.getContainer()).andReturn(componentExtn).anyTimes();
        expect(inboundWire.getServiceContract()).andReturn(serviceContract);
        expect(inboundWire.getInvocationChains()).andReturn(inChainsMap);
        expect(outboundWire.getServiceContract()).andReturn(serviceContract).anyTimes();
        expect(outboundWire.getInvocationChains()).andReturn(outChainsMap).times(2);

        Interceptor inInterceptor = createMock(Interceptor.class);
        Interceptor outInterceptor = createMock(Interceptor.class);
        inChain.addInterceptor(0, inInterceptor);
        outChain.addInterceptor(0, outInterceptor);
        outChain.addInterceptor(new SynchronousBridgingInterceptor(inChain.getHeadInterceptor()));

        EasyMock.replay(inboundWire, outboundWire);
        processor.process(outboundWire, inboundWire);

        assertEquals(false, inChain.getHeadInterceptor() instanceof PassByValueInterceptor);
        assertEquals(false,
            outChain.getTailInterceptor().getNext() instanceof PassByValueInterceptor);
        assertEquals(true, outChain.getTailInterceptor().getNext().equals(
            inChain.getHeadInterceptor()));
    }
    
    public void testProcessExclusionOfInterceptorWhenAllowsPassByReferenceAtMethod() {
        InboundWire inboundWire = createMock(InboundWire.class);
        OutboundWire outboundWire = createMock(OutboundWire.class);

        ServiceContract<Type> serviceContract = new JavaServiceContract(null);
        serviceContract.setRemotable(true);
        Map<Operation<?>, InboundInvocationChain> inChainsMap =
            new Hashtable<Operation<?>, InboundInvocationChain>();

        Operation<?> operation1 = new Operation<Type>("testMethod", null, null, null);
        InboundInvocationChainImpl inChain = new InboundInvocationChainImpl(operation1);
        inChainsMap.put(operation1, inChain);

        AtomicComponentExtension componentExtn = new FooComponent();
        componentExtn.setPassByReferenceMethods(new ArrayList<String>());
        componentExtn.getPassByReferenceMethods().add("testMethod");


        Map<Operation<?>, OutboundInvocationChain> outChainsMap =
            new Hashtable<Operation<?>, OutboundInvocationChain>();
        OutboundInvocationChainImpl outChain = new OutboundInvocationChainImpl(operation1);
        outChainsMap.put(operation1, outChain);

        expect(inboundWire.getServiceContract()).andReturn(serviceContract);
        expect(inboundWire.getContainer()).andReturn(componentExtn).anyTimes();
        expect(outboundWire.getContainer()).andReturn(componentExtn).anyTimes();
        expect(inboundWire.getServiceContract()).andReturn(serviceContract);
        expect(inboundWire.getInvocationChains()).andReturn(inChainsMap);
        expect(outboundWire.getServiceContract()).andReturn(serviceContract).anyTimes();
        expect(outboundWire.getInvocationChains()).andReturn(outChainsMap).times(2);

        Interceptor inInterceptor = createMock(Interceptor.class);
        Interceptor outInterceptor = createMock(Interceptor.class);
        inChain.addInterceptor(0, inInterceptor);
        outChain.addInterceptor(0, outInterceptor);
        outChain.addInterceptor(new SynchronousBridgingInterceptor(inChain.getHeadInterceptor()));

        EasyMock.replay(inboundWire, outboundWire);
        processor.process(outboundWire, inboundWire);

        assertEquals(false, inChain.getHeadInterceptor() instanceof PassByValueInterceptor);
        assertEquals(false,
            outChain.getTailInterceptor().getNext() instanceof PassByValueInterceptor);
        assertEquals(true, outChain.getTailInterceptor().getNext().equals(
            inChain.getHeadInterceptor()));
    }
    
    public void testProcessExclusionOfInterceptorForBinding() {
        InboundWire inboundWire = createMock(InboundWire.class);
        OutboundWire outboundWire = createMock(OutboundWire.class);
        
        ServiceBinding serviceBinding = createMock(ServiceBinding.class);
        expect(outboundWire.getContainer()).andReturn(serviceBinding).anyTimes();
        expect(inboundWire.getContainer()).andReturn(null).anyTimes();

        ServiceContract<Type> serviceContract = new JavaServiceContract(null);
        serviceContract.setRemotable(true);
        Map<Operation<?>, InboundInvocationChain> inChainsMap =
            new Hashtable<Operation<?>, InboundInvocationChain>();

        Operation<?> operation1 = new Operation<Type>("testMethod", null, null, null);
        InboundInvocationChainImpl inChain = new InboundInvocationChainImpl(operation1);
        inChainsMap.put(operation1, inChain);

        Map<Operation<?>, OutboundInvocationChain> outChainsMap =
            new Hashtable<Operation<?>, OutboundInvocationChain>();
        OutboundInvocationChainImpl outChain = new OutboundInvocationChainImpl(operation1);
        outChainsMap.put(operation1, outChain);

        Interceptor inInterceptor = createMock(Interceptor.class);
        Interceptor outInterceptor = createMock(Interceptor.class);
        inChain.addInterceptor(0, inInterceptor);
        outChain.addInterceptor(0, outInterceptor);
        outChain.addInterceptor(new SynchronousBridgingInterceptor(inChain.getHeadInterceptor()));

        EasyMock.replay(inboundWire, outboundWire);
        processor.process(outboundWire, inboundWire);

        assertEquals(false, inChain.getHeadInterceptor() instanceof PassByValueInterceptor);
        assertEquals(false,
            outChain.getTailInterceptor().getNext() instanceof PassByValueInterceptor);
        assertEquals(true, outChain.getTailInterceptor().getNext().equals(
            inChain.getHeadInterceptor()));
    }

    private class FooComponent extends AtomicComponentExtension {

        public FooComponent() {
            super(null, null, null, null, null, null, 0);
        }

        public Object createInstance() throws ObjectCreationException {
            return null;
        }

        public Object getTargetInstance() throws TargetResolutionException {
            return null;
        }

        public TargetInvoker createTargetInvoker(String targetName, Operation operation, InboundWire callbackWire) {
            return null;
        }

    }
}
