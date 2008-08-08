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

package org.apache.tuscany.sca.implementation.bpel;

import junit.framework.TestCase;

import org.apache.tuscany.implementation.bpel.example.ping.PingPortType;
import org.apache.tuscany.sca.host.embedded.SCADomain;

/**
 * Tests the BPEL service
 * 
 * @version $Rev$ $Date$
 */
public class BPELPingPongTestCase extends TestCase {

    private SCADomain scaDomain;
    PingPortType bpelPing = null;

    /**
     * @throws java.lang.Exception
     */
    @Override
    protected void setUp() throws Exception {
        //scaDomain = SCADomain.newInstance("ping-pong/ping-pong.composite");
        //bpelPing = scaDomain.getService(PingPortType.class, "BPELPingPong-PingComponent/PingService");
    }

    /**
     * @throws java.lang.Exception
     */
    @Override
    protected void tearDown() throws Exception {
        //scaDomain.close();
    }

    public void testPing() {
        //String response = bpelPing.Ping("Ping");
        //System.out.println("response:" + response);
        //assertNotNull(response);
        // assertEquals("Hello World", response);
    }
}