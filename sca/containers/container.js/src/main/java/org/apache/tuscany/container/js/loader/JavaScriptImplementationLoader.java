/**
 *
 * Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.tuscany.container.js.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;

import org.osoa.sca.annotations.Destroy;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Scope;

import org.apache.tuscany.common.resource.ResourceLoader;
import org.apache.tuscany.container.js.assembly.JavaScriptAssemblyFactory;
import org.apache.tuscany.container.js.assembly.JavaScriptImplementation;
import org.apache.tuscany.container.js.assembly.impl.JavaScriptAssemblyFactoryImpl;
import org.apache.tuscany.core.config.ConfigurationLoadException;
import org.apache.tuscany.core.config.MissingResourceException;
import org.apache.tuscany.core.config.InvalidRootElementException;
import org.apache.tuscany.core.config.SidefileLoadException;
import org.apache.tuscany.core.loader.StAXElementLoader;
import org.apache.tuscany.core.loader.StAXLoaderRegistry;
import org.apache.tuscany.core.loader.assembly.AssemblyConstants;
import org.apache.tuscany.core.system.annotation.Autowire;
import org.apache.tuscany.model.assembly.ComponentType;

/**
 * @version $Rev$ $Date$
 */
@Scope("MODULE")
public class JavaScriptImplementationLoader implements StAXElementLoader<JavaScriptImplementation> {
    public static final QName IMPLEMENTATION_JS = new QName("http://org.apache.tuscany/xmlns/js/0.9", "implementation.js");

    private static final JavaScriptAssemblyFactory factory = new JavaScriptAssemblyFactoryImpl();

    protected StAXLoaderRegistry registry;

    private XMLInputFactory xmlFactory;

    public JavaScriptImplementationLoader() {
        // todo make this a reference to a system service
        xmlFactory = XMLInputFactory.newInstance();
    }

    @Autowire
    public void setRegistry(StAXLoaderRegistry registry) {
        this.registry = registry;
    }

    @Init(eager = true)
    public void start() {
        registry.registerLoader(this);
    }

    @Destroy
    public void stop() {
        registry.unregisterLoader(this);
    }

    public QName getXMLType() {
        return IMPLEMENTATION_JS;
    }

    public Class<JavaScriptImplementation> getModelType() {
        return JavaScriptImplementation.class;
    }

    public JavaScriptImplementation load(XMLStreamReader reader, ResourceLoader resourceLoader) throws XMLStreamException, ConfigurationLoadException {
        String scriptFile = reader.getAttributeValue(null, "scriptFile");
        String style = reader.getAttributeValue(null, "style");
        String script = loadScript(scriptFile, resourceLoader);
        ComponentType componentType = loadComponentType(scriptFile, resourceLoader);

        JavaScriptImplementation jsImpl = factory.createJavaScriptImplementation();
        jsImpl.setComponentType(componentType);
        jsImpl.setScriptFile(scriptFile);
        jsImpl.setStyle(style);
        jsImpl.setScript(script);
        jsImpl.setResourceLoader(resourceLoader);
        return jsImpl;
    }

    protected String loadScript(String scriptFile, ResourceLoader resourceLoader) throws ConfigurationLoadException {
        URL url = resourceLoader.getResource(scriptFile);
        if (url == null) {
            throw new ConfigurationLoadException(scriptFile);
        }
        InputStream inputStream;
        try {
            inputStream = url.openStream();
        } catch (IOException e) {
            throw new ConfigurationLoadException(scriptFile, e);
        }
        try {
            StringBuilder sb = new StringBuilder(1024);
            int n;
            while ((n = inputStream.read()) != -1) {
                sb.append((char) n);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new ConfigurationLoadException(scriptFile, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    protected ComponentType loadComponentType(String scriptFile, ResourceLoader resourceLoader) throws SidefileLoadException, MissingResourceException{
        String sidefile = scriptFile.substring(0, scriptFile.lastIndexOf('.')) + ".componentType";
        URL componentTypeFile = resourceLoader.getResource(sidefile);
        if (componentTypeFile == null) {
            throw new MissingResourceException(sidefile);
        }

        try {
            XMLStreamReader reader;
            InputStream is;
                is = componentTypeFile.openStream();
            try {
                reader = xmlFactory.createXMLStreamReader(is);
                try {
                    reader.nextTag();
                    if (!AssemblyConstants.COMPONENT_TYPE.equals(reader.getName())) {
                        InvalidRootElementException e = new InvalidRootElementException(AssemblyConstants.COMPONENT_TYPE, reader.getName());
                        e.setResourceURI(componentTypeFile.toString());
                        throw e;
                    }
                    return (ComponentType) registry.load(reader, resourceLoader);
                } finally {
                    try {
                        reader.close();
                    } catch (XMLStreamException e) {
                        // ignore
                    }
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        } catch (IOException e) {
            SidefileLoadException sfe = new SidefileLoadException(e.getMessage());
            sfe.setResourceURI(componentTypeFile.toString());
            throw sfe;
        } catch (XMLStreamException e) {
            SidefileLoadException sfe = new SidefileLoadException(e.getMessage());
            sfe.setResourceURI(componentTypeFile.toString());
            throw sfe;
        } catch (ConfigurationLoadException e) {
            SidefileLoadException sfe = new SidefileLoadException(e.getMessage());
            sfe.setResourceURI(componentTypeFile.toString());
            throw sfe;
        }
    }
}
