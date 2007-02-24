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

package org.apache.tuscany.core.property;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.tuscany.core.databinding.xml.DOMDataBinding;
import org.apache.tuscany.spi.ObjectCreationException;
import org.apache.tuscany.spi.ObjectFactory;
import org.apache.tuscany.spi.annotation.Autowire;
import org.apache.tuscany.spi.databinding.DataBinding;
import org.apache.tuscany.spi.databinding.DataBindingRegistry;
import org.apache.tuscany.spi.databinding.Mediator;
import org.apache.tuscany.spi.databinding.extension.SimpleTypeMapperExtension;
import org.apache.tuscany.spi.idl.ElementInfo;
import org.apache.tuscany.spi.idl.TypeInfo;
import org.apache.tuscany.spi.idl.XMLType;
import org.apache.tuscany.spi.loader.LoaderException;
import org.apache.tuscany.spi.loader.PropertyObjectFactory;
import org.apache.tuscany.spi.model.DataType;
import org.apache.tuscany.spi.model.Property;
import org.apache.tuscany.spi.model.PropertyValue;
import org.osoa.sca.annotations.Constructor;
import org.osoa.sca.annotations.Scope;
import org.osoa.sca.annotations.Service;
import org.w3c.dom.Node;

@Service(PropertyObjectFactory.class)
@Scope("COMPOSITE")
public class PropertyObjectFactoryImpl implements PropertyObjectFactory {
    private DataBindingRegistry registry;
    private Mediator mediator;

    public PropertyObjectFactoryImpl() {
    }

    @Constructor( {"registry", "mediator"})
    public PropertyObjectFactoryImpl(@Autowire
    DataBindingRegistry registry, @Autowire
    Mediator mediator) {
        super();
        this.registry = registry;
        this.mediator = mediator;
    }

    public <T> ObjectFactory<T> createObjectFactory(Property<T> property, PropertyValue<T> value) {
        if (mediator == null) {
            return new SimplePropertyObjectFactory<T>(property, value.getValue().get(0));
        }
        return new ObjectFactoryImpl<T>(property, value);
    }

    public <T> ObjectFactory<List<T>> createListObjectFactory(Property<T> property, PropertyValue<T> value)
        throws LoaderException {
        if (mediator == null) {
            return new SimpleMultivaluedPropertyObjectFactory<T>(property, value.getValue());
        }
        return new ListObjectFactoryImpl<T>(property, value);
    }

    public class ObjectFactoryImplBase<P> {
        protected Property<P> property;
        protected PropertyValue<P> propertyValue;
        protected DataType<XMLType> sourceDataType;
        protected DataType<?> targetDataType;

        public ObjectFactoryImplBase(Property<P> property, PropertyValue<P> propertyValue) {
            this.property = property;
            this.propertyValue = propertyValue;
            sourceDataType =
                new DataType<XMLType>(DOMDataBinding.NAME, Node.class, new XMLType(null, this.property.getXmlType()));
            TypeInfo typeInfo = null;
            if (this.property.getXmlType() != null) {
                if (SimpleTypeMapperExtension.isSimpleXSDType(this.property.getXmlType())) {
                    typeInfo = new TypeInfo(property.getXmlType(), true, null);
                } else {
                    typeInfo = new TypeInfo(property.getXmlType(), false, null);
                }
            } else {
                typeInfo = new TypeInfo(property.getXmlType(), false, null);
            }

            XMLType xmlType = new XMLType(typeInfo);
            /*
             * ElementInfo elementInfo = new ElementInfo(null, typeInfo);
             * sourceDataType.setMetadata(ElementInfo.class.getName(),
             * elementInfo);
             */
            Class javaType = this.property.getJavaType();
            String dataBinding = (String)property.getExtensions().get(DataBinding.class.getName());
            if (dataBinding != null) {
                targetDataType = new DataType<XMLType>(dataBinding, javaType, xmlType);
            } else {
                targetDataType = new DataType<XMLType>(dataBinding, javaType, xmlType);
                registry.introspectType(targetDataType, null);
            }
        }
    }

    public class ObjectFactoryImpl<P> extends ObjectFactoryImplBase<P> implements ObjectFactory<P> {

        public ObjectFactoryImpl(Property<P> property, PropertyValue<P> propertyValue) {
            super(property, propertyValue);
        }

        @SuppressWarnings("unchecked")
        public P getInstance() throws ObjectCreationException {
            return (P)mediator.mediate(propertyValue.getValue().get(0), sourceDataType, targetDataType, null);
        }
    }

    public class ListObjectFactoryImpl<P> extends ObjectFactoryImplBase<P> implements ObjectFactory<List<P>> {

        public ListObjectFactoryImpl(Property<P> property, PropertyValue<P> propertyValue) {
            super(property, propertyValue);
        }

        @SuppressWarnings("unchecked")
        public List<P> getInstance() throws ObjectCreationException {
            List<P> instances = new ArrayList<P>();
            for (int count = 0; count < propertyValue.getValue().size(); ++count) {
                instances.add((P)mediator.mediate(propertyValue.getValue().get(count),
                                                  sourceDataType,
                                                  targetDataType,
                                                  null));
            }
            return instances;
        }
    }
}
