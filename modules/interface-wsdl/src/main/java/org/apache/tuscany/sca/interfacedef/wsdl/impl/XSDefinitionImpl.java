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

package org.apache.tuscany.sca.interfacedef.wsdl.impl;

import org.apache.tuscany.sca.interfacedef.wsdl.XSDefinition;
import org.apache.ws.commons.schema.XmlSchema;

/**
 * Represents a XML schema definition.
 *
 * @version $Rev$ $Date$
 */
public class XSDefinitionImpl implements XSDefinition {
    
    private XmlSchema definition;
    private String namespace;
    private boolean unresolved;
    
    protected XSDefinitionImpl() {
    }

    public XmlSchema getSchema() {
        return definition;
    }

    public void setSchema(XmlSchema definition) {
        this.definition = definition;
    }

    public boolean isUnresolved() {
        return unresolved;
    }

    public void setUnresolved(boolean undefined) {
        this.unresolved = undefined;
    }
    
    public String getNamespace() {
        if (isUnresolved()) {
            return namespace;
        } else if (definition != null) {
            return definition.getTargetNamespace();
        } else {
            return null;
        }
    }
    
    public void setNamespace(String namespace) {
        if (!isUnresolved()) {
            throw new IllegalStateException();
        } else {
            this.namespace = namespace;
        }
    }
    
    @Override
    public int hashCode() {
        return String.valueOf(getNamespace()).hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof XSDefinition) {
            if (getNamespace() != null) {
                return getNamespace().equals(((XSDefinition)obj).getNamespace());
            } else {
                return ((XSDefinition)obj).getNamespace() == null;
            }
        } else {
            return false;
        }
    }
}