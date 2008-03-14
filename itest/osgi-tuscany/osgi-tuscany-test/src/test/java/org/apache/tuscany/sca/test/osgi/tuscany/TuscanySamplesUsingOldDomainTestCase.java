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
package org.apache.tuscany.sca.test.osgi.tuscany;


import org.apache.tuscany.sca.test.osgi.harness.OSGiTuscanyTestHarness;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * Test Tuscany running in an OSGi container
 * 
 * Run samples which use old domain code
 */
public class TuscanySamplesUsingOldDomainTestCase {
    
    
    private static String[] SAMPLES = {
        
        "binding-notification-consumer",
        "binding-notification-producer",
        "calculator",           
        "calculator-rmi-reference",
        "calculator-rmi-service",
        "helloworld-ws-service",
        "helloworld-ws-service-secure",
        "implementation-composite",
        "implementation-notification", 
        "loanapplication", 
        "osgi-supplychain",
        "simple-bigbank", 
        "simple-callback",
        "simple-callback-ws",
        "supplychain"
           

        // FIXME: Policy security Jaas callback classloading
        // "calculator-implementation-policies",  
        
        // FIXME: Groovy classloading TUSCANY-2083
        // "calculator-script",  
        
        // FIXME: SCATestCaseRunner classloading TUSCANY-2081
        // "helloworld-ws-sdo",
        // "quote-xquery",
            
    };
    

    private OSGiTuscanyTestHarness testHarness;

    @Before
    public void setUp() throws Exception {
        
        testHarness = new OSGiTuscanyTestHarness();
        testHarness.setUp();
    }
    

    @After
    public void tearDown() throws Exception {

        if (testHarness != null) {
            testHarness.tearDown();
            testHarness = null;
        }
    }
    

    @Test
    public void runTestsUsingOldDomainCode() throws Exception {
        
        for (String testDir : SAMPLES) {
            testHarness.runTest("../../../samples/" + testDir);
        }
    }
    
    
}
