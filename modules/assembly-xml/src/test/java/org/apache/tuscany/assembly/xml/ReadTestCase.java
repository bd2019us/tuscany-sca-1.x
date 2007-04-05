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

package org.apache.tuscany.assembly.xml;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.apache.tuscany.assembly.AssemblyFactory;
import org.apache.tuscany.assembly.impl.DefaultAssemblyFactory;
import org.apache.tuscany.assembly.xml.LoaderRegistry;
import org.apache.tuscany.assembly.xml.impl.ComponentTypeProcessor;
import org.apache.tuscany.assembly.xml.impl.CompositeProcessor;
import org.apache.tuscany.assembly.xml.impl.ConstrainingTypeProcessor;
import org.apache.tuscany.assembly.xml.impl.LoaderRegistryImpl;
import org.apache.tuscany.policy.PolicyFactory;
import org.apache.tuscany.policy.impl.DefaultPolicyFactory;

/**
 * Test the usability of the assembly model API when loading SCDL
 * 
 * @version $Rev$ $Date$
 */
public class ReadTestCase extends TestCase {

    private XMLInputFactory inputFactory;
    private AssemblyFactory assemblyFactory;
    private PolicyFactory policyFactory;
    private LoaderRegistry loaderRegistry;

    public void setUp() throws Exception {
        inputFactory = XMLInputFactory.newInstance();
        assemblyFactory = new DefaultAssemblyFactory();
        policyFactory = new DefaultPolicyFactory();
        loaderRegistry = new LoaderRegistryImpl();
    }

    public void tearDown() throws Exception {
        assemblyFactory = null;
        policyFactory = null;
        inputFactory = null;
        loaderRegistry = null;
    }

    public void testReadComponentType() throws Exception {
        ComponentTypeProcessor componentTypeReader = new ComponentTypeProcessor(assemblyFactory, policyFactory, loaderRegistry);
        InputStream is = getClass().getClassLoader().getResourceAsStream("CalculatorImpl.componentType");
        XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
        assertNotNull(componentTypeReader.load(reader));
        is.close();
    }

    public void testReadConstrainingType() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("CalculatorComponent.constrainingType");
        ConstrainingTypeProcessor constrainingTypeReader = new ConstrainingTypeProcessor(assemblyFactory, policyFactory, loaderRegistry);
        XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
        assertNotNull(constrainingTypeReader.load(reader));
        is.close();

    }

    public void testReadComposite() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Calculator.composite");
        CompositeProcessor compositeReader = new CompositeProcessor(assemblyFactory, policyFactory, loaderRegistry);
        XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
        assertNotNull(compositeReader.load(reader));
        is.close();

    }

    public void testReadCompositeAndWireIt() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Calculator.composite");
        CompositeProcessor compositeReader = new CompositeProcessor(assemblyFactory, policyFactory, loaderRegistry);
        XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
        assertNotNull(compositeReader.load(reader));
        is.close();
    }

}
