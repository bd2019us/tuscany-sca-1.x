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

package org.apache.tuscany.databinding.idl;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.tuscany.databinding.Mediator;
import org.apache.tuscany.databinding.PullTransformer;
import org.apache.tuscany.databinding.TransformationContext;
import org.apache.tuscany.databinding.Transformer;
import org.apache.tuscany.databinding.extension.TransformerExtension;
import org.apache.tuscany.idl.wsdl.WSDLOperation;
import org.apache.tuscany.spi.annotation.Autowire;
import org.apache.tuscany.spi.model.DataType;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.osoa.sca.annotations.Service;

/**
 * This is a special transformer to transform the input from one IDL to the other one
 */
@Service(Transformer.class)
public abstract class Input2InputTransformer<T> extends TransformerExtension<Object[], Object[]> implements
        PullTransformer<Object[], Object[]> {

    protected WrapperHandler<T> wrapperHandler;

    protected Mediator mediator;

    /**
     * @param wrapperHandler
     */
    protected Input2InputTransformer(WrapperHandler<T> wrapperHandler) {
        super();
        this.wrapperHandler = wrapperHandler;
    }

    /**
     * @param mediator the mediator to set
     */
    @Autowire
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * @see org.apache.tuscany.databinding.extension.TransformerExtension#getSourceType()
     */
    @Override
    protected Class getSourceType() {
        return Object[].class;
    }

    /**
     * @see org.apache.tuscany.databinding.extension.TransformerExtension#getTargetType()
     */
    @Override
    protected Class getTargetType() {
        return Object[].class;
    }

    /**
     * @see org.apache.tuscany.databinding.Transformer#getWeight()
     */
    public int getWeight() {
        return 10;
    }

    @SuppressWarnings("unchecked")
    public Object[] transform(Object[] source, TransformationContext context) {
        DataType<List<DataType<?>>> sourceType = context.getSourceDataType();
        WSDLOperation sourceOp = (WSDLOperation) sourceType.getMetadata(WSDLOperation.class.getName());
        boolean sourceWrapped = (sourceOp != null && sourceOp.isWrapperStyle());

        DataType<List<DataType<QName>>> targetType = context.getTargetDataType();
        WSDLOperation targetOp = (WSDLOperation) targetType.getMetadata(WSDLOperation.class.getName());
        boolean targetWrapped = (targetOp != null && targetOp.isWrapperStyle());

        if ((!sourceWrapped) && targetWrapped) {
            // Unwrapped --> Wrapped
            WSDLOperation.Wrapper wrapper = targetOp.getWrapper();
            T targetWrapper = wrapperHandler.create(wrapper.getInputWrapperElement());
            if (source == null) {
                return new Object[] { targetWrapper };
            }
            List<DataType<QName>> argTypes = wrapper.getUnwrappedInputType().getLogical();

            for (int i = 0; i < source.length; i++) {
                XmlSchemaElement argElement = wrapper.getInputChildElements().get(i);
                DataType<QName> argType = argTypes.get(i);
                XmlSchemaType argXSDType = argElement.getSchemaType();
                boolean isSimpleType = (argXSDType instanceof XmlSchemaSimpleType);
                Object child = source[i];
                if (!isSimpleType) {
                    child = mediator.mediate(source[i], sourceType.getLogical().get(i), argType);
                    wrapperHandler.setChild(targetWrapper, i, argElement, child);
                } else {
                    wrapperHandler.setChild(targetWrapper, i, argElement, child);
                }
            }
            return new Object[] { targetWrapper };
        } else if (sourceWrapped && (!targetWrapped)) {
            // Wrapped to Unwrapped
            T sourceWrapper = (T) source[0];
            List<XmlSchemaElement> childElements = sourceOp.getWrapper().getInputChildElements();
            Object[] newArgs = new Object[childElements.size()];
            for (int i = 0; i < childElements.size(); i++) {
                XmlSchemaElement childElement = childElements.get(i);
                if (childElement.getSchemaType() instanceof XmlSchemaSimpleType) {
                    Object child = wrapperHandler.getChild(sourceWrapper, i, childElement);
                    newArgs[i] = child;
                } else {
                    Object child = wrapperHandler.getChild(sourceWrapper, i, childElement);
                    DataType<QName> childType = sourceOp.getWrapper().getUnwrappedInputType().getLogical().get(i);
                    newArgs[i] = mediator.mediate(child, childType, targetType.getLogical().get(i));
                }
            }
            return newArgs;
        } else {
            Object[] newArgs = new Object[source.length];
            for (int i = 0; i < source.length; i++) {
                Object child =
                        mediator.mediate(source[i], sourceType.getLogical().get(i), targetType.getLogical().get(i));
                newArgs[i] = child;
            }
            return newArgs;
        }
    }

}
