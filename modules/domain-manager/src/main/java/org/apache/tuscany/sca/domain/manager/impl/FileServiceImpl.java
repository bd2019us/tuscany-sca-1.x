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

package org.apache.tuscany.sca.domain.manager.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tuscany.sca.data.collection.Item;
import org.apache.tuscany.sca.data.collection.LocalItemCollection;
import org.apache.tuscany.sca.data.collection.NotFoundException;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Property;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;
import org.osoa.sca.annotations.Service;

/**
 * Implementation of a servlet component supporting file upload/download.
 * 
 * @version $Rev$ $Date$
 */
@Scope("COMPOSITE")
@Service(Servlet.class)
public class FileServiceImpl extends HttpServlet {
	private static final long serialVersionUID = -4560385595481971616L;

	private static final Logger logger = Logger.getLogger(FileServiceImpl.class
			.getName());

	@Property
	public String directoryName;

	@Reference
	public DomainManagerConfiguration domainManagerConfiguration;

	@Reference
	public LocalItemCollection contributionCollection;

	private ServletFileUpload upload;

	/**
	 * Initialize the component.
	 */
	@Init
	public void initialize() throws IOException {
		upload = new ServletFileUpload(new DiskFileItemFactory());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// Upload files
		String rootDirectory = domainManagerConfiguration.getRootDirectory();
		try {
			for (FileItem item : (List<FileItem>) upload.parseRequest(request)) {
				if (!item.isFormField()) {
					File directory = new File(rootDirectory + "/"
							+ directoryName);
					if (!directory.exists()) {
						directory.mkdirs();
					}
					logger.fine("post " + item.getName());
					item.write(new File(directory, item.getName()));
				}
			}

			// Redirect to the admin page
			response.sendRedirect("/ui/files");
		} catch (Exception e) {
			throw new IOException(e.toString());
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Download a file
		String requestURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");
		String path = requestURI.substring(request.getServletPath().length());
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		logger.fine("get " + path);

		try {

			// Analyze the given path
			String artifactURI;
			if (path.startsWith("contribution=")) {
				int semicolonIndex = path.indexOf(';');
				String contributionName = path.substring("contribution="
						.length(), semicolonIndex);
				artifactURI = path.substring(semicolonIndex + 1);

				try {
					Item item = this.contributionCollection
							.get(contributionName);

					if (item == null) {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);

						return;

					}

					path = item.getAlternate();

				} catch (NotFoundException e) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);

					return;

				}
				
				if (path.endsWith(".jar") || path.endsWith(".zip")) {
					path = "jar:" + (path.startsWith("file:") ? "" : "file:") + path + '!' + (artifactURI.startsWith("/") ? "" : "/") + artifactURI;
					
				} else {
					path += (path.endsWith("/") ? "" : "/") + artifactURI;
				}

			} else {
				artifactURI = null;
			}

			URI uri = URI.create(path);
			String scheme = uri.getScheme();
			if (scheme == null) {

				if (artifactURI != null) {
					uri = URI.create("file:" + uri.toString());

				} else {

					// If no scheme is specified then the path identifies file
					// inside our directory
					String rootDirectory = domainManagerConfiguration
							.getRootDirectory();
					uri = new File(rootDirectory + "/" + directoryName, path)
							.toURI();

				}
				
			} else if (!scheme.equals("file") && !scheme.equals("jar")) {

				// If the scheme does not identify a local file, just redirect
				// to the server
				// hosting the file
				response.sendRedirect(path);
			}
			
			// Read the file and write to response
			URLConnection connection = uri.toURL().openConnection();
			connection.setUseCaches(false);
			connection.connect();
			InputStream is = connection.getInputStream();
			ServletOutputStream os = response.getOutputStream();
			byte[] buffer = new byte[4096];
			for (;;) {
				int n = is.read(buffer);
				if (n < 0) {
					break;
				}
				os.write(buffer, 0, n);
			}
			is.close();
			os.flush();

		} catch (FileNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
