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
package org.apache.tuscany.spi.component;

import javax.xml.namespace.QName;

import org.apache.tuscany.spi.model.Operation;
import org.apache.tuscany.spi.model.ServiceContract;
import org.apache.tuscany.spi.wire.InboundWire;
import org.apache.tuscany.spi.wire.OutboundWire;
import org.apache.tuscany.spi.wire.TargetInvoker;

/**
 * Manages an SCA reference configured with a binding
 *
 * @version $Rev$ $Date$
 */
public interface ReferenceBinding extends SCAObject {

    /**
     * Returns the binding qualified name
     *
     * @return the binding qualified name
     */
    QName getBindingType();
    
    /**
     * Sets the parent reference for the binding
     *
     * @param reference the parent reference for the binding
     */
    void setReference(Reference reference);

    /**
     * Returns the inbound wire for flowing a request through the reference
     */
    InboundWire getInboundWire();

    /**
     * Sets the inbound wire for flowing a request through the reference
     */
    void setInboundWire(InboundWire wire);

    /**
     * Returns the outbound wire used by the reference to connect to a target
     */
    OutboundWire getOutboundWire();

    /**
     * Sets the outbound wire used by the reference to connect to a target
     */
    void setOutboundWire(OutboundWire wire);

    /**
     * Callback to create a {@link org.apache.tuscany.spi.wire.TargetInvoker} which dispatches to the target service of
     * the reference
     *
     * @param contract  the service contract to invoke on
     * @param operation the operation to invoke
     * @throws TargetInvokerCreationException
     */
    TargetInvoker createTargetInvoker(ServiceContract contract, Operation operation)
        throws TargetInvokerCreationException;

    /**
     * Returns the service contract for the binding
     *
     * @return the service contract for the binding
     */
    ServiceContract<?> getBindingServiceContract();


    /**
     * Set the ServiceContract for the binding. This contract will be used for the outbound wire. If not set, it will be
     * the same as the ServideContract from the interface.
     *
     * @param serviceContract the service contract
     */
    void setBindingServiceContract(ServiceContract<?> serviceContract);

}
