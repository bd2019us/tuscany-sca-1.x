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
package org.apache.tuscany.spi.wire;

import org.apache.tuscany.spi.component.TargetException;
import org.apache.tuscany.spi.model.ServiceContract;

/**
 * The base wire type used to connect references and services
 *
 * @version $$Rev$$ $$Date$$
 */
public interface RuntimeWire<T> {

    /**
     * Returns the non-proxied target instance for this wire
     */
    T getTargetService() throws TargetException;

    ServiceContract getServiceContract();

    void setServiceContract(ServiceContract contract);

    /**
     * Adds an interface type generated proxies implement
     */
    void addInterface(Class<?> claz);

    /**
     * Returns true if the wire and all of its interceptors and handlers can be optimized
     */
    boolean isOptimizable();

}
