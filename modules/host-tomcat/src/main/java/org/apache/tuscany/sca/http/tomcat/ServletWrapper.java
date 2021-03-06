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
package org.apache.tuscany.sca.http.tomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.catalina.core.StandardWrapper;

/**
 * A Servlet wrapper.
 * 
 * @version $Rev$ $Date$
 */
public class ServletWrapper extends StandardWrapper {
    private static final long serialVersionUID = 1L;

    private final Servlet servlet;

    public ServletWrapper(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    public synchronized Servlet loadServlet() {
        return servlet;
    }

    @Override
    public Servlet getServlet() {
        return servlet;
    }
    
    public void initServlet() throws ServletException {
        servlet.init(facade);
    }
    
    public void destroyServlet() {
        servlet.destroy();
    }

}
