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
package org.apache.tuscany.binding.rmi;

import helloworld.HelloWorldRmiService;
import junit.framework.Assert;

import org.apache.tuscany.host.embedded.SCARuntimeActivator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osoa.sca.ComponentContext;
import org.osoa.sca.ServiceReference;

public class BindingTestCase {
    private static HelloWorldRmiService helloWorldRmiService;
 
    @Test
    public void testRmiService() {
        System.out.println(helloWorldRmiService.sayRmiHello("Tuscany World!"));
        Assert.assertEquals("Hello from the RMI Service to - Tuscany World! thro the RMI Reference",
                helloWorldRmiService.sayRmiHello("Tuscany World!"));
        
        System.out.println(helloWorldRmiService.sayRmiHi("Tuscany World!", "Apache World"));
        
        Assert.assertEquals("Hi from Apache World in RMI Service to - Tuscany World! thro the RMI Reference",
                            helloWorldRmiService.sayRmiHi("Tuscany World!", "Apache World"));
    }


    
    @BeforeClass
    public static void init() throws Exception {
        SCARuntimeActivator.start("META-INF/sca/RMIBindingTest.composite");
        ComponentContext context = SCARuntimeActivator.getComponentContext("HelloWorldRmiServiceComponent");
        ServiceReference<HelloWorldRmiService> serviceReference = context.createSelfReference(HelloWorldRmiService.class);
        helloWorldRmiService = serviceReference.getService();
  }
    
    @AfterClass
    public static void destroy() throws Exception {
        SCARuntimeActivator.stop();
    }

}
