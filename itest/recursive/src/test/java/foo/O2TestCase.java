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
package foo;

import junit.framework.TestCase;

import org.apache.tuscany.sca.host.embedded.SCADomain;

public class O2TestCase extends TestCase {

    public void testGet() throws Exception {
        SCADomain sca = SCADomain.newInstance("foo/o2.composite");
        I i = sca.getService(I.class, "O1Component");
        assertEquals("foo.A", i.get(0));
    }
}
