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
package org.apache.tuscany.binding.axis2;

import java.lang.reflect.InvocationTargetException;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.tuscany.spi.wire.InboundWire;
import org.apache.tuscany.spi.wire.InvocationRuntimeException;
import org.apache.tuscany.spi.wire.Message;

public class Axis2AsyncTargetInvoker extends Axis2TargetInvoker {

    protected static final OMElement RESPONSE = null;

    private InboundWire wire;
    private Axis2ReferenceCallbackTargetInvoker callbackInvoker;

    public Axis2AsyncTargetInvoker(ServiceClient serviceClient,
                                   QName wsdlOperationName,
                                   Options options,
                                   SOAPFactory soapFactory,
                                   InboundWire wire) {
        super(serviceClient, wsdlOperationName, options, soapFactory);
        this.wire = wire;
    }

    public Object invokeTarget(final Object payload) throws InvocationTargetException {
        throw new InvocationTargetException(new InvocationRuntimeException("Operation not supported"));
    }

    private Object invokeTarget(final Object payload, Object messageId) throws InvocationTargetException {
        try {
            Object[] args = (Object[])payload;
            OperationClient operationClient = createOperationClient(args);
            callbackInvoker.setCorrelationId(messageId);
            Axis2ReferenceCallback callback = new Axis2ReferenceCallback(callbackInvoker);
            operationClient.setCallback(callback);

            operationClient.execute(false);

            // REVIEW it seems ok to return null
            return RESPONSE;
        } catch (AxisFault e) {
            throw new InvocationTargetException(e);
        }
    }

    public Message invoke(Message msg) throws InvocationRuntimeException {
        try {
            Object messageId = msg.getMessageId();
            wire.addMapping(messageId, msg.getFromAddress());
            Object resp = invokeTarget(msg.getBody(), messageId);
            msg.setBody(resp);
        } catch (Throwable e) {
            msg.setBodyWithFault(e);
        }
        return msg;
    }

    public void setCallbackTargetInvoker(Axis2ReferenceCallbackTargetInvoker callbackInvoker) {
        this.callbackInvoker = callbackInvoker;
    }
}
