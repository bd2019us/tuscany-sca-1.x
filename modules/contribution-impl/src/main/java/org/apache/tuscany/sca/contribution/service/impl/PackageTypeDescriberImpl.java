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

package org.apache.tuscany.sca.contribution.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.apache.tuscany.sca.contribution.PackageType;
import org.apache.tuscany.sca.contribution.service.TypeDescriber;
import org.apache.tuscany.sca.contribution.service.util.FileHelper;

/**
 * Implementation of the content describer for contribution packages
 * 
 * @version $Rev$ $Date$
 */
public class PackageTypeDescriberImpl implements TypeDescriber {
    private final Map<String, String> contentTypeRegistry = new HashMap<String, String>();

    public PackageTypeDescriberImpl() {
        super();
        init();
    }

    /**
     * Initialize contentType registry with know types based on known file extensions
     */
    private void init() {
        contentTypeRegistry.put("EAR", PackageType.EAR);
        contentTypeRegistry.put("JAR", PackageType.JAR);
        contentTypeRegistry.put("WAR", PackageType.WAR);
        contentTypeRegistry.put("ZIP", PackageType.ZIP);
    }

    protected String resolveContentyTypeByExtension(URL resourceURL) {
        String artifactExtension = FileHelper.getExtension(resourceURL.getPath());
        if (artifactExtension == null) {
            return null;
        }
        return contentTypeRegistry.get(artifactExtension.toUpperCase());
    }

    /**
     * Build contentType for a specific resource. We first check if the file is a supported one
     * (looking into our registry based on resource extension) If not found, we try to check file
     * contentType Or we return defaultContentType provided
     * 
     * @param resourceURL The artifact URL
     * @param defaultContentType The default content type if we can't find the correct one
     * @return The content type
     */
    public String getType(URL resourceURL, String defaultContentType) {
        URLConnection connection = null;
        String contentType = defaultContentType;
        final String urlProtocol = resourceURL.getProtocol();

        if (urlProtocol.equals("file")) {
            final File fileOrDir = FileHelper.toFile(resourceURL);
            // Allow privileged access to test file. Requires FilePermissions in security policy.
            Boolean isDirectory = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    return fileOrDir.isDirectory();
                }
            });
            if (isDirectory) {
                // Special case : contribution is a folder
                contentType = PackageType.FOLDER;
            }
            
            String type = resolveContentyTypeByExtension(resourceURL);
            if (type != null) {
                return type;
            }
        } else if (urlProtocol.equals("bundle") || urlProtocol.equals("bundleresource")) {
            contentType = PackageType.BUNDLE;
        } else {
            contentType = resolveContentyTypeByExtension(resourceURL);
            if (contentType == null) {
                try {
                    connection = resourceURL.openConnection();
                    connection.setUseCaches(false);
                    contentType = connection.getContentType();

                    if (contentType == null || contentType.equals("content/unknown")) {
                        // here we couldn't figure out from our registry or from URL and it's not a
                        // special file
                        // return defaultContentType if provided
                        contentType = defaultContentType;
                    }
                } catch (IOException io) {
                    // could not access artifact, just ignore and we will return
                    // null contentType
                }
            }
        }
        return contentType == null ? defaultContentType : contentType;
    }

}
