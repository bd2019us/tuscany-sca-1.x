/**
 *
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.tuscany.core.implementation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tuscany.spi.annotation.Monitor;

import org.apache.tuscany.core.util.JavaIntrospectionHelper;

/**
 * Default implementation of the <code>IntrospectionRegistry</code>
 *
 * @version $Rev$ $Date$
 */
public class IntrospectionRegistryImpl implements IntrospectionRegistry {

    private IntrospectionMonitor monitor;
    private List<ImplementationProcessor> cache = new ArrayList<ImplementationProcessor>();

    public IntrospectionRegistryImpl() {
    }

    public IntrospectionRegistryImpl(IntrospectionMonitor monitor) {
        this.monitor = monitor;
    }

    @Monitor
    public void setMonitor(IntrospectionMonitor monitor) {
        this.monitor = monitor;
    }

    public void registerProcessor(ImplementationProcessor processor) {
        monitor.register(processor);
        cache.add(processor);
    }

    public void unregisterProcessor(ImplementationProcessor processor) {
        monitor.unregister(processor);
        cache.remove(processor);
    }

    public PojoComponentType introspect(Class<?> clazz, PojoComponentType type) throws ProcessingException {
        for (ImplementationProcessor processor : cache) {
            processor.visitClass(clazz, type, null);
        }

        for (Constructor constructor : clazz.getConstructors()) {
            for (ImplementationProcessor processor : cache) {
                processor.visitConstructor(constructor, type, null);
            }
        }

//        Constructor componentConstructor = null;
//        Constructor[] constructors = clazz.getConstructors();
//        for (Constructor constructor : constructors) {
//            if (componentConstructor == null) {
//                componentConstructor = constructor;
//            }else{
//                if componentConstructor.getAnnotation()
//            }
//            for (ImplementationProcessor processor : cache) {
//                processor.visitConstructor(constructor, type, null);
//            }
//        }

        Set<Method> methods = JavaIntrospectionHelper.getAllUniquePublicProtectedMethods(clazz);
        for (Method method : methods) {
            for (ImplementationProcessor processor : cache) {
                processor.visitMethod(method, type, null);
            }
        }

        Set<Field> fields = JavaIntrospectionHelper.getAllPublicAndProtectedFields(clazz);
        for (Field field : fields) {
            for (ImplementationProcessor processor : cache) {
                processor.visitField(field, type, null);
            }
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            visitSuperClass(superClass, type);
        }

        for (ImplementationProcessor processor : cache) {
            processor.visitEnd(clazz, type, null);
        }
        return type;
    }

    private void visitSuperClass(Class<?> clazz, PojoComponentType type) throws ProcessingException {
        if (!Object.class.equals(clazz)) {
            for (ImplementationProcessor processor : cache) {
                processor.visitSuperClass(clazz, type, null);
            }
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                visitSuperClass(clazz, type);
            }
        }
    }

    public static interface IntrospectionMonitor {
        void register(ImplementationProcessor processor);

        void unregister(ImplementationProcessor processor);

        void processing(ImplementationProcessor processor);
    }
}
