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
package org.apache.tuscany.sca.contribution.processor;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.apache.tuscany.sca.contribution.service.ContributionException;

/**
 * Interface for contribution package processors
 * 
 * Package processors understand the format of the contribution and how to get the
 * artifacts in the contribution.
 * 
 * @version $Rev$ $Date$
 */
public interface PackageProcessor {
    
    /**
     * Returns the type of package supported by this package processor.
     * 
     * @return the package type
     */
    String getPackageType();

    /**
     * Returns a list of artifacts in the contribution.
     * 
     * @param packageSourceURL Contribution package location URL
     * @param inputStream Optional content of the package
     * @return List of artifact URIs
     * @throws ContributionException
     * @throws IOException
     */
    List<URI> getArtifacts(URL packageSourceURL, InputStream inputStream) throws ContributionException, IOException;

    /**
     * Return the URL for an artifact in the package.
     * 
     * This is needed for archives such as jar files that have specific URL schemes
     * for the artifacts they contain.
     * 
     * @param packageSourceURL Contribution package location URL
     * @param artifact The relative URI for the artifact
     * @return The artifact URL
     */
    URL getArtifactURL(URL packageSourceURL, URI artifact) throws MalformedURLException;

}
