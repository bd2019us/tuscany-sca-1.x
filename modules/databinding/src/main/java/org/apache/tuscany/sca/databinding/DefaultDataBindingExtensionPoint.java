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
package org.apache.tuscany.sca.databinding;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tuscany.sca.databinding.javabeans.JavaBeansDataBinding;
import org.apache.tuscany.sca.databinding.javabeans.JavaExceptionDataBinding;
import org.apache.tuscany.sca.extensibility.ServiceDeclaration;
import org.apache.tuscany.sca.extensibility.ServiceDiscovery;
import org.apache.tuscany.sca.interfacedef.DataType;
import org.apache.tuscany.sca.interfacedef.impl.DataTypeImpl;

/**
 * The default implementation of a data binding extension point.
 * 
 * @version $Rev$ $Date$
 */
public class DefaultDataBindingExtensionPoint implements DataBindingExtensionPoint {
    private final Map<String, DataBinding> bindings = new HashMap<String, DataBinding>();
    private final List<DataBinding> databindings = new ArrayList<DataBinding>();
    private static final Logger logger = Logger.getLogger(DefaultDataBindingExtensionPoint.class.getName());
    private boolean loadedDataBindings;

    public DefaultDataBindingExtensionPoint() {
    }

    public DataBinding getDataBinding(String id) {
        if (id == null) {
            return null;
        }
        DataBinding dataBinding = bindings.get(id.toLowerCase());
        if (dataBinding == null) {
            loadDataBindings();
            dataBinding = bindings.get(id.toLowerCase());
        }
        return dataBinding;
    }

    public void addDataBinding(DataBinding dataBinding) {
        if (logger.isLoggable(Level.FINE)) {
            String className = dataBinding.getClass().getName();
            boolean lazy = false;
            if (dataBinding instanceof LazyDataBinding) {
                className = ((LazyDataBinding)dataBinding).dataBindingDeclaration.getClassName();
                lazy = true;
            }
            logger.fine("Adding databinding: " + className + ";type=" + dataBinding.getName() + ",lazy=" + lazy);
        }
        databindings.add(dataBinding);
        bindings.put(dataBinding.getName().toLowerCase(), dataBinding);

        String[] aliases = dataBinding.getAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                bindings.put(alias.toLowerCase(), dataBinding);
            }
        }
    }

    public DataBinding removeDataBinding(String id) {
        if (id == null) {
            return null;
        }
        DataBinding dataBinding = bindings.remove(id.toLowerCase());
        if (dataBinding != null) {
            databindings.remove(dataBinding);
            String[] aliases = dataBinding.getAliases();
            if (aliases != null) {
                for (String alias : aliases) {
                    bindings.remove(alias.toLowerCase());
                }
            }
        }
        return dataBinding;
    }

    /**
     * Dynamically load data bindings declared under META-INF/services
     */
    private void loadDataBindings() {
        if (loadedDataBindings)
            return;

        // Get the databinding service declarations
        Set<ServiceDeclaration> dataBindingDeclarations;
        try {
            dataBindingDeclarations = ServiceDiscovery.getInstance().getServiceDeclarations(DataBinding.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        // Load data bindings
        for (ServiceDeclaration dataBindingDeclaration : dataBindingDeclarations) {
            Map<String, String> attributes = dataBindingDeclaration.getAttributes();
            String type = attributes.get("type");
            String name = attributes.get("name");

            // Create a data binding wrapper and register it
            DataBinding dataBinding = new LazyDataBinding(type, name, dataBindingDeclaration);
            addDataBinding(dataBinding);
        }

        loadedDataBindings = true;
    }

    /**
     * A data binding facade allowing data bindings to be lazily loaded and
     * initialized.
     */
    private static class LazyDataBinding implements DataBinding {

        private String name;
        private String[] aliases;
        private ServiceDeclaration dataBindingDeclaration;
        private DataBinding dataBinding;

        private LazyDataBinding(String type, String name, ServiceDeclaration dataBindingDeclaration) {
            this.name = type;
            if (name != null) {
                this.aliases = new String[] {name};
            }
            this.dataBindingDeclaration = dataBindingDeclaration;
        }

        /**
         * Load and instantiate the data binding class.
         * 
         * @return The data binding.
         */
        @SuppressWarnings("unchecked")
        private DataBinding getDataBinding() {
            if (dataBinding == null) {
                try {
                    Class<DataBinding> dataBindingClass = (Class<DataBinding>)dataBindingDeclaration.loadClass();
                    Constructor<DataBinding> constructor = dataBindingClass.getConstructor();
                    dataBinding = constructor.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            return dataBinding;
        }

        public Object copy(Object object) {
            return getDataBinding().copy(object);
        }

        public String[] getAliases() {
            return aliases;
        }

        public String getName() {
            return name;
        }

        public SimpleTypeMapper getSimpleTypeMapper() {
            return getDataBinding().getSimpleTypeMapper();
        }

        public WrapperHandler getWrapperHandler() {
            return getDataBinding().getWrapperHandler();
        }

        public boolean introspect(DataType dataType, Annotation[] annotations) {
            return getDataBinding().introspect(dataType, annotations);
        }

        public DataType introspect(Object value) {
            return getDataBinding().introspect(value);
        }
    }

    //FIXME The following methods should not be on the extension point
    // they should be on a separate class
    public boolean introspectType(DataType dataType, Annotation[] annotations) {
        return introspectType(dataType, annotations, false);
    }

    private boolean introspectArray(DataType dataType, Annotation[] annotations) {
        Class physical = dataType.getPhysical();
        if (!physical.isArray() || physical == byte[].class) {
            return false;
        }
        Class componentType = physical.getComponentType();
        DataType logical = new DataTypeImpl(componentType, dataType.getLogical());
        introspectType(logical, annotations);
        dataType.setDataBinding("java:array");
        dataType.setLogical(logical);
        return true;
    }

    //
    // Leverage the DataBinding ExceptionHandler to calculate the DataType of an exception DataType
    //
    public boolean introspectType(DataType dataType, Annotation[] annotations, boolean isException) {
        loadDataBindings();
        for (DataBinding binding : databindings) {
            // don't introspect for JavaBeansDatabinding as all javatypes will
            // anyways match to its basetype
            // which is java.lang.Object. Default to this only if no databinding
            // results
            if (!binding.getName().equals(JavaBeansDataBinding.NAME)) {
                /*
                if (isException) {
                    // Next look to see if the DB's exceptionHandler handles this exception
                    ExceptionHandler excHandler = binding.getExceptionHandler();
                    if (excHandler != null) {
                        // Assymetric to have the introspect() methods set the DataBindings themselves
                        // whereas we're setting it ourselves here.   
                        DataType faultType = excHandler.getFaultType(dataType);
                        if (faultType != null) {
                            dataType.setDataBinding(binding.getName());
                            dataType.setLogical(faultType);
                            return true;
                        }
                    }
                } else
                */ 
                {
                    if (binding.introspect(dataType, annotations)) {
                        return true;
                    }
                }
            }
        }
        // FIXME: Should we honor the databinding from operation/interface
        // level?
        Class physical = dataType.getPhysical();
        if (physical == Object.class) {
            return false;
        }
        if (dataType.getPhysical().isArray()) {
            introspectArray(dataType, annotations);
            return true;
        } else if (isException) {
            dataType.setDataBinding(JavaExceptionDataBinding.NAME);
            return false;
        } else {
            dataType.setDataBinding(JavaBeansDataBinding.NAME);
            return false;
        }
    }

    public DataType introspectType(Object value) {
        loadDataBindings();
        DataType dataType = null;
        for (DataBinding binding : bindings.values()) {
            // don't introspect for JavaBeansDatabinding as all javatypes will
            // anyways match to its basetype
            // which is java.lang.Object. Default to this only if no databinding
            // results
            if (!binding.getName().equals(JavaBeansDataBinding.NAME)) {
                dataType = binding.introspect(value);
            }
            if (dataType != null) {
                return dataType;
            }
        }
        return new DataTypeImpl<Class>(JavaBeansDataBinding.NAME, value.getClass(), value.getClass());
    }
}
