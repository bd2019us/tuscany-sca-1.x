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

package org.apache.tuscany.sca.interfacedef.wsdl.introspect;

import java.util.ArrayList;
import java.util.List;

import javax.wsdl.PortType;

import org.apache.tuscany.sca.contribution.resolver.ModelResolver;
import org.apache.tuscany.sca.interfacedef.InvalidInterfaceException;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.interfacedef.wsdl.WSDLFactory;
import org.apache.tuscany.sca.interfacedef.wsdl.WSDLInterface;
import org.apache.ws.commons.schema.XmlSchemaCollection;

/**
 * Introspector for creating WSDLInterface definitions from WSDL PortTypes.
 */
public class DefaultWSDLInterfaceIntrospector implements WSDLInterfaceIntrospector {
    
    private WSDLFactory wsdlFactory;
    
    public DefaultWSDLInterfaceIntrospector(WSDLFactory wsdlFactory) {
        super();
        this.wsdlFactory = wsdlFactory;
    }

    // FIXME: Do we want to deal with document-literal wrapped style based on the JAX-WS spec?
    protected List<Operation> introspectOperations(PortType portType, XmlSchemaCollection inlineSchemas, ModelResolver resolver) throws InvalidInterfaceException {
        List<Operation> operations = new ArrayList<Operation>();
        for (Object o : portType.getOperations()) {
            javax.wsdl.Operation wsdlOp = (javax.wsdl.Operation)o;
            WSDLOperation op = new WSDLOperation(wsdlFactory, wsdlOp, inlineSchemas, null, resolver);
            operations.add(op.getOperation());
        }
        return operations;
    }

    public WSDLInterface introspect(PortType portType, XmlSchemaCollection inlineSchemas, ModelResolver resolver) throws InvalidInterfaceException {
        WSDLInterface wsdlInterface = wsdlFactory.createWSDLInterface();
        wsdlInterface.setPortType(portType);
        wsdlInterface.getOperations().addAll(introspectOperations(portType, inlineSchemas, resolver));
        // FIXME: set to Non-conversational for now
        wsdlInterface.setConversational(false);
        return wsdlInterface;
    }

}