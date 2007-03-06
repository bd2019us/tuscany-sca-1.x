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
package org.apache.tuscany.core.services.deployment;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.osoa.sca.Constants.SCA_NS;

import java.net.URI;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.tuscany.spi.annotation.Autowire;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.extension.LoaderExtension;
import org.apache.tuscany.spi.loader.InvalidValueException;
import org.apache.tuscany.spi.loader.LoaderException;
import org.apache.tuscany.spi.loader.LoaderRegistry;
import org.apache.tuscany.spi.model.Contribution;
import org.apache.tuscany.spi.model.ContributionImport;
import org.apache.tuscany.spi.model.ModelObject;
import org.osoa.sca.annotations.Constructor;

/**
 * Loader that handles &lt;include&gt; elements.
 * 
 * @version $Rev$ $Date$
 */
public class ContributionLoader extends LoaderExtension<Contribution> {
    private static final QName CONTRIBUTION = new QName(SCA_NS, "contribution");
    private static final QName DEPLOYABLE = new QName(SCA_NS, "deployable");
    private static final QName IMPORT = new QName(SCA_NS, "import");
    private static final QName EXPORT = new QName(SCA_NS, "export");

    @Constructor({"registry"})
    public ContributionLoader(@Autowire LoaderRegistry registry) {
        super(registry);
    }

    public QName getXMLType() {
        return CONTRIBUTION;
    }

    public Contribution load(CompositeComponent parent,
                             ModelObject object,
                             XMLStreamReader reader,
                             DeploymentContext deploymentContext) throws XMLStreamException, LoaderException {

        Contribution contribution = new Contribution();
        while (true) {
            int event = reader.next();
            switch (event) {
                case START_ELEMENT:
                    QName element = reader.getName();
                    if (DEPLOYABLE.equals(element)) {
                        String name = reader.getAttributeValue(null, "composite");
                        if (name == null) {
                            throw new InvalidValueException("Attribute 'composite' is missing");
                        }
                        QName compositeName = null;
                        int index = name.indexOf(':');
                        if (index != -1) {
                            String prefix = name.substring(0, index);
                            String localPart = name.substring(index);
                            String ns = reader.getNamespaceContext().getNamespaceURI(prefix);
                            if (ns == null) {
                                throw new InvalidValueException("Invalid prefix: " + prefix);
                            }
                            compositeName = new QName(ns, localPart, prefix);
                        } else {
                            String prefix = "";
                            String ns = reader.getNamespaceURI();
                            String localPart = name;
                            compositeName = new QName(ns, localPart, prefix);
                        }
                        contribution.getDeployables().add(compositeName);
                    } else if (IMPORT.equals(element)) {
                        String ns = reader.getAttributeValue(null, "namespace");
                        if (ns == null) {
                            throw new InvalidValueException("Attribute 'namespace' is missing");
                        }
                        String location = reader.getAttributeValue(null, "location");
                        ContributionImport contributionImport = new ContributionImport();
                        if (location != null) {
                            contributionImport.setLocation(URI.create(location));
                        }
                        contributionImport.setNamespace(ns);
                        contribution.getImports().add(contributionImport);
                    } else if (EXPORT.equals(element)) {
                        String ns = reader.getAttributeValue(null, "namespace");
                        if (ns == null) {
                            throw new InvalidValueException("Attribute 'namespace' is missing");
                        }
                        contribution.getExports().add(ns);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (CONTRIBUTION.equals(reader.getName())) {
                        return contribution;
                    }
                    break;

            }
        }
    }

}
