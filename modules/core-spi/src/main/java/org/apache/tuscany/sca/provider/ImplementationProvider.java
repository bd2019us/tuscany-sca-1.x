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

package org.apache.tuscany.sca.provider;

import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;

/**
 * A component implementation can implement this interface in order to tie 
 * into the Tuscany runtime 
 * 
 * @version $Rev$ $Date$
 */
public interface ImplementationProvider {

    /**
     * This method will be invoked when the component implementation
     * is activated.
     */
    void start();

    /**
     * This method will be invoked when the component implementation
     * is deactivated.
     */
    void stop();

    /**
     * Create an invoker for the component implementation in the invocation
     * chain. The invoker will be responsible for calling the implementation
     * logic for the given component.
     * 
     * @param service The component service
     * @param operation The operation that the interceptor will handle
     * @return An invoker that handles the invocation logic, null should be
     *         returned if no invoker is required
     */
    Invoker createInvoker(RuntimeComponentService service, Operation operation);

    /**
     * For bindings that invoke one-way callback operations asynchronously,
     * there is no need to perform a thread switch before calling the invoker.
     * This method indicates whether the binding has this capability.
     * 
     * @return true if the callback invoker is able to invoke one-way operations
     *         asynchronously, false if all invocations are synchronous
     */
    boolean supportsOneWayInvocation();

}
