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
package org.apache.tuscany.sca.contribution.updater.impl;

import java.util.List;

import org.apache.tuscany.sca.assembly.Component;
import org.apache.tuscany.sca.assembly.Composite;
import org.apache.tuscany.sca.contribution.DeployedArtifact;
import org.apache.tuscany.sca.runtime.RuntimeComponent;

public class ArtifactsFinder {

    public static Composite findComposite(String compositeURI,
            List<DeployedArtifact> artifacts) {
        for (DeployedArtifact artifact : artifacts) {
            if (artifact.getModel() instanceof Composite) {
                Composite composite = (Composite) artifact.getModel();
                if (composite.getURI().equals(compositeURI))
                    return composite;
            }
        }
        return null;
    }

    public static Component findComponent(Composite composite,
            String componentName) {
        for (Component component : composite.getComponents()) {
            if (component.getName().equals(componentName)) {
                return component;

            }

        }
        return null;
    }

}
