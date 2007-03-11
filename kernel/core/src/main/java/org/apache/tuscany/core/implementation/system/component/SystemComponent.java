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
package org.apache.tuscany.core.implementation.system.component;

import java.net.URI;

import org.apache.tuscany.core.component.InstanceFactoryProvider;
import org.apache.tuscany.core.implementation.PojoComponent;
import org.apache.tuscany.spi.component.ScopeContainer;

/**
 * @version $Rev$ $Date$
 * @param <T> the implementation class for the defined component
 */
public class SystemComponent<T> extends PojoComponent<T> {
    public SystemComponent(URI componentId,
                           InstanceFactoryProvider<T> provider,
                           ScopeContainer scopeContainer,
                           int initLevel,
                           int maxIdleTime,
                           int maxAge) {
        super(componentId, provider, scopeContainer, initLevel, maxIdleTime, maxAge);
    }

}
