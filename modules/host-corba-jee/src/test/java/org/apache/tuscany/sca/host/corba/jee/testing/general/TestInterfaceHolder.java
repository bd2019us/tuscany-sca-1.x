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

package org.apache.tuscany.sca.host.corba.jee.testing.general;

/**
* org/apache/tuscany/sca/host/corba/testing/general/TestInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from general_tests.idl
* roda, 25 czerwiec 2008 16:19:44 CEST
*/

public final class TestInterfaceHolder implements org.omg.CORBA.portable.Streamable {
    public org.apache.tuscany.sca.host.corba.jee.testing.general.TestInterface value = null;

    public TestInterfaceHolder() {
    }

    public TestInterfaceHolder(org.apache.tuscany.sca.host.corba.jee.testing.general.TestInterface initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.apache.tuscany.sca.host.corba.jee.testing.general.TestInterfaceHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.apache.tuscany.sca.host.corba.jee.testing.general.TestInterfaceHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.apache.tuscany.sca.host.corba.jee.testing.general.TestInterfaceHelper.type();
    }

}
