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

package org.apache.tuscany.databinding.impl;

import org.apache.tuscany.databinding.Transformer;
import org.apache.tuscany.databinding.idl.Input2InputTransformer;
import org.osoa.sca.annotations.Service;

/**
 * This is a special transformer to convert java parameters by the inputType
 */
@Service(Transformer.class)
public class JavaParametersTransformer extends Input2InputTransformer<Object> {

    private static final String IDL_INPUT = "idl:input";

    public JavaParametersTransformer() {
        super(null);
    }

    @Override
    public String getSourceBinding() {
        return IDL_INPUT;
    }

    @Override
    public String getTargetBinding() {
        return IDL_INPUT;
    }

    /**
     * @see org.apache.tuscany.databinding.Transformer#getWeight()
     */
    public int getWeight() {
        return 10;
    }

}
