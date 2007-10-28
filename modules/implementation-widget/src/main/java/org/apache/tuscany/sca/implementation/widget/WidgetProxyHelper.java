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

package org.apache.tuscany.sca.implementation.widget;

import java.util.HashMap;
import java.util.Map;

public class WidgetProxyHelper {
    protected static Map<String, String> proxyFileRegistry = new HashMap<String, String>();
    protected static Map<String, String> proxyClient = new HashMap<String, String>();

    static {
        proxyFileRegistry.put("org.apache.tuscany.sca.binding.feed.impl.AtomBindingImpl", "binding-atom.js");
        proxyClient.put("org.apache.tuscany.sca.binding.feed.impl.AtomBindingImpl", "AtomClient");
        
        proxyFileRegistry.put("org.apache.tuscany.sca.binding.jsonrpc.JSONRPCBinding", "binding-jsonrpc.js");
        proxyClient.put("org.apache.tuscany.sca.binding.jsonrpc.JSONRPCBinding", "JSONRpcClient");
    }
    
    protected WidgetProxyHelper() {
        
    }
    
    public static String getJavaScriptProxyFile(String bindingClass) {
        return proxyFileRegistry.get(bindingClass);
    }
    
    public static String getJavaScriptProxyClient(String bindingClass) {
        return proxyClient.get(bindingClass);
    }
}
