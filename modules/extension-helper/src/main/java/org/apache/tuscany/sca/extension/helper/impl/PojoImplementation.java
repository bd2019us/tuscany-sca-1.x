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

package org.apache.tuscany.sca.extension.helper.impl;

import java.lang.reflect.Method;

import org.apache.tuscany.sca.contribution.resolver.ModelResolver;
import org.apache.tuscany.sca.extension.helper.utils.DynamicImplementation;

/**
 * Enables Implementation extensions to use a simple POJO
 * for the implementation object instead of requiring
 * implementing the Implementation interface. 
 *
 * @version $Rev$ $Date$
 */
public class PojoImplementation<Implementation> extends DynamicImplementation {
    
    Object userImpl;
    
    public PojoImplementation(Object userImpl) {
        this.userImpl = userImpl;
    }

    public Object getUserImpl() {
        return userImpl;
    }
    
    public void resolve(ModelResolver resolver) {
    	
    	try {
  		    Method resolveMethod;
  		    if (userImpl != null &&
  				  (resolveMethod = userImpl.getClass().getMethod("resolve", ModelResolver.class)) != null) {
  			    resolveMethod.invoke(userImpl, resolver);
  		  }
  	    } catch (Exception e) {
  	    }
    }

}
