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
package org.apache.tuscany.sca.assembly;

import org.apache.tuscany.sca.policy.PolicySetAttachPoint;

/**
 * A property allows for the configuration of an implementation with externally
 * set data values. An implementation can have zero or more properties. Each
 * property has a data type, which may be either simple or complex. An
 * implementation may also define a default value for a property.
 * 
 * @version $Rev$ $Date$
 */
public interface Property extends AbstractProperty, PolicySetAttachPoint, Cloneable {
    
    /**
     * Returns a clone of the property.
     * 
     * @return a clone of the property
     * @throws CloneNotSupportedException
     */
    Object clone() throws CloneNotSupportedException;

}
