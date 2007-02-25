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
package org.apache.tuscany.container.script;

import java.lang.reflect.InvocationTargetException;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;

/**
 * An invokable instance of a script
 * 
 * Basically just a wrapper around a BSF engine with an optional script class object.
 */
public class ScriptInstance {

    protected BSFEngine bsfEngine;
    protected Object clazz;

    public ScriptInstance(BSFEngine bsfEngine, Object clazz) {
        this.bsfEngine = bsfEngine;
        this.clazz = clazz;
    }

    public Object invokeTarget(String operationName, Object[] args) throws InvocationTargetException {
        try {
            return bsfEngine.call(clazz, operationName, args);
        } catch (BSFException e) {
            throw new InvocationTargetException(e.getTargetException() != null ? e.getTargetException() : e);
         } catch (Exception e) {
            throw new InvocationTargetException(e);
        }
    }
}

