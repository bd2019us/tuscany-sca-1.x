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
package org.apache.tuscany.container.javascript.rhino;

import junit.framework.TestCase;

public class RhinoSCAConfigTestCase extends TestCase {

    public void testJavaInteface() {
        RhinoScript rs = new RhinoScript("javaInterface", "SCA = { javaInterface : 'Test' }");
        RhinoSCAConfig scaConfig = rs.getSCAConfig();
        assertTrue(scaConfig.hasSCAConfig());
        assertEquals("Test", scaConfig.getJavaInterface());
    }

    public void testWSDLLocation() {
        RhinoScript rs = new RhinoScript("wsdlLocation", "SCA = { wsdlLocation : 'Test' }");
        RhinoSCAConfig scaConfig = rs.getSCAConfig();
        assertTrue(scaConfig.hasSCAConfig());
        assertEquals("Test", scaConfig.getWSDLLocation());
    }

    public void testWSDLNamespace() {
        RhinoScript rs = new RhinoScript("wsdlNamespace", "SCA = { wsdlNamespace : 'Test' }");
        RhinoSCAConfig scaConfig = rs.getSCAConfig();
        assertTrue(scaConfig.hasSCAConfig());
        assertEquals("Test", scaConfig.getWSDLNamespace());
    }

    public void testWSDLPortType() {
        RhinoScript rs = new RhinoScript("wsdlPortType", "SCA = { wsdlPortType : 'Test' }");
        RhinoSCAConfig scaConfig = rs.getSCAConfig();
        assertTrue(scaConfig.hasSCAConfig());
        assertEquals("Test", scaConfig.getWSDLPortType());
    }

    public void testNoConfig() {
        RhinoScript rs = new RhinoScript("javaInterface", "");
        RhinoSCAConfig scaConfig = rs.getSCAConfig();
        assertTrue(!scaConfig.hasSCAConfig());
    }

    public void testNoService() {
        RhinoScript rs = new RhinoScript("javaInterface", "SCA = {}");
        try {
            RhinoSCAConfig scaConfig = rs.getSCAConfig();
            fail("expecting no service exception: " + scaConfig);
        } catch (IllegalArgumentException e) {
        }
    }

    public void testDupicateInteface() {
        RhinoScript rs = new RhinoScript("javaInterface", "SCA = { javaInterface : 'Test', wsdlLocation : 'Test' }");
        try {
            RhinoSCAConfig scaConfig = rs.getSCAConfig();
            fail("expecting multiple service exception: " + scaConfig);
        } catch (IllegalArgumentException e) {
        }
    }

}
