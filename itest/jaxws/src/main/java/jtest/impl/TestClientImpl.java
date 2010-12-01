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

package jtest.impl;

import java.util.ArrayList;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Service;

import jtest.AbstractException;
import jtest.TestClient;
import jtest.TestConcrete1;
import jtest.TestConcrete2;
import jtest.TestService;

/**
 * The test client implementation
 */
@Service(TestClient.class)
public class TestClientImpl implements TestClient {

    @Reference
    protected TestService ref;

    public void runAbstractTypeTest() {
        TestConcrete1 data1 = new TestConcrete1();
        data1.firstName = "Bill";
        data1.lastName = "Brown";
        ref.sendAbstract(data1);
        TestConcrete2 data2 = new TestConcrete2();
        data2.firstName = "Sam";
        data2.lastName = "Smith";
        ref.sendAbstract(data2);
    }

    public void runAbstractExceptionTest() {
        try {
            ref.throwAbstract();
        } catch (AbstractException e) {
            System.out.println("Caught exception " + e);
            System.out.println(e.getGreeting());
        }
    }

    public void runListTypeTest() {
        ArrayList<String> data = new ArrayList<String>();
        data.add("Hello,");
        data.add("World!");
        ref.sendList(data);
    }
}