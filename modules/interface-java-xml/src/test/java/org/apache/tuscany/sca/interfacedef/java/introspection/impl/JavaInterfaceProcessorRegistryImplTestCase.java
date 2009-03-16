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
package org.apache.tuscany.sca.interfacedef.java.introspection.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import junit.framework.TestCase;

import org.apache.tuscany.sca.interfacedef.DataType;
import org.apache.tuscany.sca.interfacedef.InvalidInterfaceException;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.interfacedef.java.DefaultJavaInterfaceFactory;
import org.apache.tuscany.sca.interfacedef.java.JavaInterface;
import org.apache.tuscany.sca.interfacedef.java.introspect.DefaultJavaInterfaceIntrospectorExtensionPoint;
import org.apache.tuscany.sca.interfacedef.java.introspect.ExtensibleJavaInterfaceIntrospector;
import org.apache.tuscany.sca.interfacedef.java.introspect.JavaInterfaceVisitor;
import org.easymock.EasyMock;

/**
 * @version $Rev$ $Date$
 */
public class JavaInterfaceProcessorRegistryImplTestCase extends TestCase {
    private ExtensibleJavaInterfaceIntrospector impl;
    private DefaultJavaInterfaceIntrospectorExtensionPoint visitors;

    @SuppressWarnings("unchecked")
    public void testSimpleInterface() throws InvalidInterfaceException {
        JavaInterface intf = (JavaInterface)impl.introspect(Simple.class);

        assertEquals(Simple.class, intf.getJavaClass());
        List<Operation> operations = intf.getOperations();
        assertEquals(1, operations.size());
        Operation baseInt = operations.get(0);
        assertEquals("baseInt", baseInt.getName());

        DataType<Type> returnType = baseInt.getOutputType();
        assertEquals(Integer.TYPE, returnType.getPhysical());
        assertEquals(Integer.TYPE, returnType.getLogical());

        List<DataType> parameterTypes = baseInt.getInputType().getLogical();
        assertEquals(1, parameterTypes.size());
        DataType<Type> arg0 = parameterTypes.get(0);
        assertEquals(Integer.TYPE, arg0.getPhysical());
        assertEquals(Integer.TYPE, arg0.getLogical());

        List<DataType> faultTypes = baseInt.getFaultTypes();
        assertEquals(1, faultTypes.size());
        DataType<Type> fault0 = faultTypes.get(0);
        assertEquals(IOException.class, fault0.getPhysical());
        assertEquals(IOException.class, fault0.getLogical());
    }

    public void testUnregister() throws Exception {
        JavaInterfaceVisitor extension = createMock(JavaInterfaceVisitor.class);
        extension.visitInterface(EasyMock.isA(JavaInterface.class));
        expectLastCall().once();
        replay(extension);
        visitors.addInterfaceVisitor(extension);
        impl.introspect(Base.class);
        visitors.removeInterfaceVisitor(extension);
        impl.introspect(Base.class);
        verify(extension);
    }

    protected void setUp() throws Exception {
        super.setUp();
        visitors = new DefaultJavaInterfaceIntrospectorExtensionPoint();
        impl = new ExtensibleJavaInterfaceIntrospector(new DefaultJavaInterfaceFactory(), visitors);

    }

    private static interface Base {
        int baseInt(int param) throws IllegalArgumentException, IOException;
    }

    private static interface Simple extends Base {

    }
}