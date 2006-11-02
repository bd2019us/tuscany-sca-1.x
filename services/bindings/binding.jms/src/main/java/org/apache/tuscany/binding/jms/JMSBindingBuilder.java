/**
 *
 * Copyright 2006 The Apache Software Foundation
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
package org.apache.tuscany.binding.jms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.axiom.om.OMElement;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.component.Reference;
import org.apache.tuscany.spi.component.Service;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.extension.BindingBuilderExtension;
import org.apache.tuscany.spi.model.BoundReferenceDefinition;
import org.apache.tuscany.spi.model.BoundServiceDefinition;
import org.apache.tuscany.spi.model.ServiceContract;

import commonj.sdo.helper.TypeHelper;

/**
 * Builds a Service or Reference for JMS binding.
 *
 * @version $Rev: 449970 $ $Date: 2006-09-26 06:05:35 -0400 (Tue, 26 Sep 2006) $
 */

public class JMSBindingBuilder extends BindingBuilderExtension<JMSBinding> {

	private static final String DEFAULT_JMS_RESOURCE_FACTORY = "org.apache.tuscany.binding.jms.SimpleJMSResourceFactory";
	private static final String DEFAULT_OPERATION_SELECTOR = "org.apache.tuscany.binding.jms.DefaultOperationSelector";
	private static final String OM_DATA_BINDING = OMElement.class.getName();
	
    protected Class<JMSBinding> getBindingType() {
        return JMSBinding.class;
    }

    @SuppressWarnings({"unchecked"})
    public Service build(CompositeComponent parent,
                           BoundServiceDefinition<JMSBinding> serviceDefinition,
                           DeploymentContext deploymentContext) {

    	JMSBinding jmsBinding = serviceDefinition.getBinding();
        Class<?> interfaze = serviceDefinition.getServiceContract().getInterfaceClass();
        
        ServiceContract serviceContract =  serviceDefinition.getServiceContract();
        serviceContract.setDataBinding(OM_DATA_BINDING);
        
        JMSResourceFactory jmsResourceFactory = getJMSResourceFactory(jmsBinding);
        OperationSelector opSec = getOperationSelector(jmsBinding);
        
        Service service = new JMSService(serviceDefinition.getName(),parent, wireService, jmsBinding, jmsResourceFactory,opSec,interfaze);
        service.setBindingServiceContract(serviceContract);
        
        return service;
    }

	@SuppressWarnings({"unchecked"})
    public Reference build(CompositeComponent parent,
                              BoundReferenceDefinition<JMSBinding> referenceDefinition,
                              DeploymentContext deploymentContext) {
    	
        String name = referenceDefinition.getName();
        Class<?> interfaze = referenceDefinition.getServiceContract().getInterfaceClass();
        ServiceContract serviceContract = referenceDefinition.getServiceContract();
        serviceContract.setDataBinding(OM_DATA_BINDING);
        
        JMSBinding jmsBinding = referenceDefinition.getBinding();
        JMSResourceFactory jmsResourceFactory = getJMSResourceFactory(jmsBinding);
        OperationSelector opSec = getOperationSelector(jmsBinding);
        
        Reference reference = new JMSReference(name,parent,wireService, jmsBinding, jmsResourceFactory,opSec,interfaze);
        reference.setBindingServiceContract(serviceContract);
        return reference;

    }

    private JMSResourceFactory getJMSResourceFactory(JMSBinding jmsBinding) {
    	String className = jmsBinding.getJmsResourceFactoryName();   
    	if (className != null && !className.equals("")){
			try {
	    		Class factoryClass = Class.forName(className != null ? className : DEFAULT_JMS_RESOURCE_FACTORY);
				Constructor constructor = factoryClass.getDeclaredConstructor(new Class[]{JMSBinding.class});
				return (JMSResourceFactory) constructor.newInstance(jmsBinding);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return new SimpleJMSResourceFactory(jmsBinding);
    	}else{
    		return new SimpleJMSResourceFactory(jmsBinding);
    	}	
		
	}
    

	private OperationSelector getOperationSelector(JMSBinding jmsBinding) {
		String className = jmsBinding.getOperationSelectorName();    	
		
		if (className != null && !className.equals("")){
			try {
	    		Class factoryClass = Class.forName(className != null ? className : DEFAULT_OPERATION_SELECTOR);
				Constructor constructor = factoryClass.getDeclaredConstructor(new Class[]{JMSBinding.class});
				return (OperationSelector) constructor.newInstance(jmsBinding);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return new DefaultOperationSelector(jmsBinding);
		}else{
			return new DefaultOperationSelector(jmsBinding);
		}
	}
}
