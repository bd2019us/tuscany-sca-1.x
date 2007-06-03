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

package org.apache.tuscany.sca.spi.impl;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.tuscany.sca.assembly.AssemblyFactory;
import org.apache.tuscany.sca.assembly.Implementation;
import org.apache.tuscany.sca.contribution.service.ContributionReadException;
import org.apache.tuscany.sca.contribution.service.ContributionWriteException;
import org.apache.tuscany.sca.spi.utils.AbstractStAXArtifactProcessor;
import org.apache.tuscany.sca.spi.utils.DynamicImplementation;

/**
 * An SCDL ArtifactProcessor which uses the Implementation class getters/setters
 * to define the SCDL attributes.
 */
public class SCDLProcessor extends AbstractStAXArtifactProcessor<Implementation> {

    protected QName scdlQName;
    protected Class<Implementation> implementationClass;

    protected Map<String, Method> attributeSetters;
    protected Method elementTextSetter;

    public SCDLProcessor(AssemblyFactory assemblyFactory, QName scdlQName, Class<Implementation> implementationClass) {
        super(assemblyFactory);
        this.scdlQName = scdlQName;
        this.implementationClass = implementationClass;
        initAttributes();
    }

    protected void initAttributes() {
        attributeSetters = new HashMap<String, Method>();
        Set<Method> methods = new HashSet<Method>(Arrays.asList(implementationClass.getMethods()));
        methods.removeAll(Arrays.asList(DynamicImplementation.class.getMethods()));
        for (Method m : methods) {
            if ("setElementText".equals(m.getName())) {
                elementTextSetter = m;
            } else if ((m.getName().startsWith("set"))) {
                attributeSetters.put(m.getName().substring(3).toLowerCase(), m);
            }
        }
    }

    public QName getArtifactType() {
        return scdlQName;
    }

    public Class<Implementation> getModelType() {
        return implementationClass;
    }

    public Implementation read(XMLStreamReader reader) throws ContributionReadException, XMLStreamException {
        Implementation impl;
        try {
            impl = implementationClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String attribute : attributeSetters.keySet()) {
            String value = reader.getAttributeValue(null, attribute);
            if (value != null && value.length() > 0) {
                try {
                    attributeSetters.get(attribute).invoke(impl, new Object[] {value});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (elementTextSetter != null) {
            try {
                String value = reader.getElementText();
                if (value != null && value.length() > 0) {
                    elementTextSetter.invoke(impl, value);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        while (!(reader.getEventType() == END_ELEMENT && scdlQName.equals(reader.getName())) && reader.hasNext()) {
            reader.next();
        }

        return impl;
    }

    public void write(Implementation arg0, XMLStreamWriter arg1) throws ContributionWriteException, XMLStreamException {
    }

}
