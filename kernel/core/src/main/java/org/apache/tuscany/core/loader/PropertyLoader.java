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
package org.apache.tuscany.core.loader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.osoa.sca.Version.XML_NAMESPACE_1_0;
import org.osoa.sca.annotations.Constructor;
import org.w3c.dom.Document;

import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.extension.LoaderExtension;
import org.apache.tuscany.spi.loader.LoaderException;
import org.apache.tuscany.spi.loader.LoaderRegistry;
import org.apache.tuscany.spi.model.OverrideOptions;
import org.apache.tuscany.spi.model.Property;
import org.apache.tuscany.spi.annotation.Autowire;

/**
 * Loads a property from an XML-based assembly file
 *
 * @version $Rev$ $Date$
 */
public class PropertyLoader extends LoaderExtension<Property> {
    public static final String PROPERTY_NAME_ATTR = "name";
    public static final String PROPERTY_TYPE_ATTR = "type";
    public static final String PROPERTY_MANY_ATTR = "many";
    public static final String PROPERTY_OVERRIDE_ATTR = "override";
    
    public static final QName PROPERTY = new QName(XML_NAMESPACE_1_0, "property");
    private final DocumentBuilder documentBuilder;

    @Constructor({"registry"})
    public PropertyLoader(@Autowire LoaderRegistry registry) {
        super(registry);
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // we should be able to construct the default DocumentBuilder
            throw new AssertionError(e);
        }
    }

    public QName getXMLType() {
        return PROPERTY;
    }

    public Property<?> load(CompositeComponent parent, XMLStreamReader reader, DeploymentContext ctx)
        throws XMLStreamException, LoaderException {
        assert PROPERTY.equals(reader.getName());
        String name = reader.getAttributeValue(null, PROPERTY_NAME_ATTR);
        String typeName = reader.getAttributeValue(null, PROPERTY_TYPE_ATTR);
        QName xmlType = null;
        if (typeName != null) {
            int index = typeName.indexOf(':');
            if (index != -1) {
                String prefix = typeName.substring(0, index);
                String localName = typeName.substring(index + 1);
                String ns = reader.getNamespaceURI(prefix);
                xmlType = new QName(ns, localName, prefix);
            }
        }
        boolean many = Boolean.parseBoolean(reader.getAttributeValue(null, PROPERTY_MANY_ATTR));
        String override = reader.getAttributeValue(null, PROPERTY_OVERRIDE_ATTR);
        
        Document value = StAXUtil.createPropertyValue(reader, xmlType, documentBuilder);

        Property<?> property = new Property();
        property.setName(name);
        property.setXmlType(xmlType);
        property.setMany(many);
        
        if (override != null) {
            property.setOverride(OverrideOptions.valueOf(override.toUpperCase()));
        }
        property.setDefaultValue(value);
        return property;
    }
}
