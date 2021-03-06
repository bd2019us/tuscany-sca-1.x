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

package org.apache.tuscany.sca.binding.dwr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.tuscany.sca.assembly.Binding;
import org.apache.tuscany.sca.extension.helper.ComponentLifecycle;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.interfacedef.java.JavaInterface;
import org.apache.tuscany.sca.interfacedef.java.impl.JavaInterfaceUtil;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;
import org.apache.tuscany.sca.runtime.RuntimeWire;

/**
 * DWR Service.
 *
 * @version $Rev$ $Date$
 */
public class DWRService implements ComponentLifecycle {

    private RuntimeComponent rc;
    private RuntimeComponentService rcs;
    private Binding binding;
    private ServletHost servletHost;
    
    static final String SERVLET_PATH = "/SCADomain/*";

    public DWRService(RuntimeComponent rc, RuntimeComponentService rcs, Binding binding, DWRBinding ab, ServletHost servletHost) {
        this.rc = rc;
        this.rcs = rcs;
        this.binding = binding;
        this.servletHost = servletHost;
    }

    public void start() {
        
        DWRServlet servlet = (DWRServlet) servletHost.getServletMapping(SERVLET_PATH);
        if (servlet == null) {
            servlet = new DWRServlet();
            servletHost.addServletMapping(SERVLET_PATH, servlet);
        }
        
        // Create a Java proxy to the target service
        Class<?> type = ((JavaInterface)rcs.getInterfaceContract().getInterface()).getJavaClass();
        Object proxy = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RuntimeWire wire = rcs.getRuntimeWire(binding);
                Operation op = JavaInterfaceUtil.findOperation(method, rcs.getInterfaceContract().getInterface().getOperations());
                return wire.invoke(op, args);
            }});

        servlet.addService(binding.getName(), type, proxy);
    }

    public void stop() {
        servletHost.removeServletMapping(SERVLET_PATH);
    }

}
