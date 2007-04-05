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

package org.apache.tuscany.assembly.xml.impl;

import java.io.IOException;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.tuscany.assembly.AssemblyFactory;
import org.apache.tuscany.assembly.Composite;
import org.apache.tuscany.assembly.impl.DefaultAssemblyFactory;
import org.apache.tuscany.policy.PolicyFactory;
import org.apache.tuscany.policy.impl.DefaultPolicyFactory;
import org.apache.tuscany.services.spi.contribution.ArtifactResolver;
import org.apache.tuscany.services.spi.contribution.ContributionException;
import org.apache.tuscany.services.spi.contribution.ContributionReadException;
import org.apache.tuscany.services.spi.contribution.StAXArtifactProcessorRegistry;
import org.apache.tuscany.services.spi.contribution.URLArtifactProcessor;

/**
 * A composite processor.
 * 
 * @version $Rev$ $Date$
 */
public class CompositeDocumentProcessor extends BaseArtifactProcessor implements URLArtifactProcessor<Composite> {
    private StAXArtifactProcessorRegistry registry;
    private XMLInputFactory inputFactory;

    /**
     * Construct a new composite processor
     * @param assemblyFactory
     * @param policyFactory
     * @param registry
     */
    public CompositeDocumentProcessor(AssemblyFactory factory, PolicyFactory policyFactory, StAXArtifactProcessorRegistry registry, XMLInputFactory inputFactory) {
        super(factory, policyFactory);
        this.registry = registry;
        this.inputFactory = inputFactory;
    }

    /**
     * Construct a new composite processor.
     * @param registry
     */
    public CompositeDocumentProcessor(StAXArtifactProcessorRegistry registry) {
        this(new DefaultAssemblyFactory(), new DefaultPolicyFactory(), registry, XMLInputFactory.newInstance());
    }

    public Composite read(URL url) throws ContributionReadException {
        try {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(url.openStream());
            Composite composite = (Composite)registry.read(reader);
            return composite;
            
        } catch (XMLStreamException e) {
            throw new ContributionReadException(e);
        } catch (IOException e) {
            throw new ContributionReadException(e);
        }
    }
    
    public void resolve(Composite composite, ArtifactResolver resolver) throws ContributionException {
        registry.resolve(composite, resolver);
    }

    public void optimize(Composite composite) throws ContributionException {
        registry.optimize(composite);
    }

    public String getArtifactType() {
        return ".composite";
    }
    
    public Class<Composite> getModelType() {
        return Composite.class;
    }
}
