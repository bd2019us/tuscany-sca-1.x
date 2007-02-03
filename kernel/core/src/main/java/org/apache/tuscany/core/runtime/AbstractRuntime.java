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
package org.apache.tuscany.core.runtime;

import java.net.URL;
import java.net.URI;
import javax.xml.stream.XMLInputFactory;

import org.apache.tuscany.core.bootstrap.Bootstrapper;
import org.apache.tuscany.core.bootstrap.DefaultBootstrapper;
import org.apache.tuscany.core.implementation.system.model.SystemCompositeImplementation;
import org.apache.tuscany.core.monitor.NullMonitorFactory;
import org.apache.tuscany.core.component.ComponentManager;
import org.apache.tuscany.core.component.ComponentManagerImpl;
import org.apache.tuscany.host.MonitorFactory;
import org.apache.tuscany.host.RuntimeInfo;
import org.apache.tuscany.host.management.ManagementService;
import org.apache.tuscany.host.runtime.InitializationException;
import org.apache.tuscany.host.runtime.TuscanyRuntime;
import org.apache.tuscany.spi.bootstrap.ComponentNames;
import org.apache.tuscany.spi.bootstrap.RuntimeComponent;
import org.apache.tuscany.spi.builder.BuilderException;
import org.apache.tuscany.spi.component.AtomicComponent;
import org.apache.tuscany.spi.component.ComponentException;
import org.apache.tuscany.spi.component.ComponentRegistrationException;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.component.SCAObject;
import org.apache.tuscany.spi.component.TargetResolutionException;
import org.apache.tuscany.spi.deployer.Deployer;
import org.apache.tuscany.spi.loader.LoaderException;
import org.apache.tuscany.spi.model.ComponentDefinition;
import org.apache.tuscany.spi.services.management.TuscanyManagementService;
import org.apache.tuscany.spi.wire.WireService;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractRuntime implements TuscanyRuntime {
    private final XMLInputFactory xmlFactory;
    private URL systemScdl;
    private String applicationName;
    private URL applicationScdl;
    private ClassLoader hostClassLoader;
    private ClassLoader applicationClassLoader;
    private RuntimeInfo runtimeInfo;
    private MonitorFactory monitorFactory;
    private ManagementService<?> managementService;
    private ComponentManager componentManager;

    private RuntimeComponent runtime;
    private CompositeComponent systemComponent;
    private CompositeComponent tuscanySystem;
    private Deployer deployer;
    private WireService wireService;

    protected AbstractRuntime() {
        this(new NullMonitorFactory());
    }

    protected AbstractRuntime(MonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
        xmlFactory = XMLInputFactory.newInstance("javax.xml.stream.XMLInputFactory", getClass().getClassLoader());
    }

    public URL getSystemScdl() {
        return systemScdl;
    }

    public void setSystemScdl(URL systemScdl) {
        this.systemScdl = systemScdl;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public URL getApplicationScdl() {
        return applicationScdl;
    }

    public void setApplicationScdl(URL applicationScdl) {
        this.applicationScdl = applicationScdl;
    }

    public ClassLoader getApplicationClassLoader() {
        return applicationClassLoader;
    }

    public void setApplicationClassLoader(ClassLoader applicationClassLoader) {
        this.applicationClassLoader = applicationClassLoader;
    }

    public ClassLoader getHostClassLoader() {
        return hostClassLoader;
    }

    public void setHostClassLoader(ClassLoader hostClassLoader) {
        this.hostClassLoader = hostClassLoader;
    }

    public RuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
        this.runtimeInfo = runtimeInfo;
    }

    public MonitorFactory getMonitorFactory() {
        return monitorFactory;
    }

    public void setMonitorFactory(MonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
    }

    public ManagementService<?> getManagementService() {
        return managementService;
    }

    public void setManagementService(ManagementService<?> managementService) {
        this.managementService = managementService;
    }

    protected XMLInputFactory getXMLFactory() {
        return xmlFactory;
    }

    protected RuntimeComponent getRuntime() {
        return runtime;
    }

    protected CompositeComponent getSystemComponent() {
        return systemComponent;
    }

    protected CompositeComponent getTuscanySystem() {
        return tuscanySystem;
    }

    protected Deployer getDeployer() {
        return deployer;
    }

    protected WireService getWireService() {
        return wireService;
    }

    public void initialize() throws InitializationException {
        Bootstrapper bootstrapper = createBootstrapper();
        runtime = bootstrapper.createRuntime();
        runtime.start();

        systemComponent = runtime.getSystemComponent();
        registerSystemComponents();
        try {
            componentManager.register(systemComponent);
            componentManager.register(runtime.getRootComponent());
        } catch (ComponentRegistrationException e) {
            throw new InitializationException(e);
        }
        systemComponent.start();

        // deploy the system scdl
        try {
            tuscanySystem = deploySystemScdl(bootstrapper.createDeployer(),
                                             systemComponent,
                                             ComponentNames.TUSCANY_SYSTEM,
                                             getSystemScdl(),
                                             getClass().getClassLoader());
        } catch (LoaderException e) {
            throw new InitializationException(e);
        } catch (BuilderException e) {
            throw new InitializationException(e);
        } catch (ComponentException e) {
            throw new InitializationException(e);
        }
        tuscanySystem.start();

        this.deployer = locateDeployer();
        this.wireService = locateWireService();
    }

    public void destroy() {
        this.wireService = null;
        this.deployer = null;
        if (tuscanySystem != null) {
            tuscanySystem.stop();
            tuscanySystem = null;
        }
        if (systemComponent != null) {
            systemComponent.stop();
            systemComponent = null;
        }
        if (runtime != null) {
            runtime.stop();
            runtime = null;
        }
    }


    protected Bootstrapper createBootstrapper() {
        TuscanyManagementService tms = (TuscanyManagementService) getManagementService();
        componentManager = new ComponentManagerImpl(tms);
        return new DefaultBootstrapper(getMonitorFactory(), xmlFactory, componentManager, tms);
    }

    protected void registerSystemComponents() throws InitializationException {
        try {
            systemComponent.registerJavaObject(RuntimeInfo.COMPONENT_NAME, RuntimeInfo.class, runtimeInfo);
            systemComponent.registerJavaObject("MonitorFactory", MonitorFactory.class, getMonitorFactory());
            systemComponent.registerJavaObject("ComponentManager", ComponentManager.class, componentManager);
        } catch (ComponentRegistrationException e) {
            throw new InitializationException(e);
        }
    }

    protected Deployer locateDeployer() throws InitializationException {
        SCAObject deployerComponent = tuscanySystem.getSystemChild(ComponentNames.TUSCANY_DEPLOYER);
        if (!(deployerComponent instanceof AtomicComponent)) {
            throw new InitializationException("Deployer must be an atomic component");
        }
        try {
            return (Deployer) ((AtomicComponent) deployerComponent).getTargetInstance();
        } catch (TargetResolutionException e) {
            throw new InitializationException(e);
        }
    }

    protected WireService locateWireService() throws InitializationException {
        SCAObject wireServiceComponent = tuscanySystem.getSystemChild(ComponentNames.TUSCANY_WIRE_SERVICE);
        if (!(wireServiceComponent instanceof AtomicComponent)) {
            throw new InitializationException("WireService must be an atomic component");
        }
        try {
            return (WireService) ((AtomicComponent) wireServiceComponent).getTargetInstance();
        } catch (TargetResolutionException e) {
            throw new InitializationException(e);
        }
    }

    protected CompositeComponent deploySystemScdl(Deployer deployer,
                                                  CompositeComponent parent,
                                                  URI name,
                                                  URL systemScdl,
                                                  ClassLoader systemClassLoader)
        throws LoaderException, BuilderException, ComponentException {

        SystemCompositeImplementation impl = new SystemCompositeImplementation();
        impl.setScdlLocation(systemScdl);
        impl.setClassLoader(systemClassLoader);
        ComponentDefinition<SystemCompositeImplementation> definition =
            new ComponentDefinition<SystemCompositeImplementation>(name, impl);

        return (CompositeComponent) deployer.deploy(parent, definition);
    }
}
