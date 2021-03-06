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
package org.apache.tuscany.sca.interfacedef.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.tuscany.sca.interfacedef.ConversationSequence;
import org.apache.tuscany.sca.interfacedef.DataType;
import org.apache.tuscany.sca.interfacedef.Interface;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.interfacedef.ParameterMode;
import org.apache.tuscany.sca.interfacedef.util.WrapperInfo;
import org.apache.tuscany.sca.interfacedef.util.XMLType;
import org.apache.tuscany.sca.policy.Intent;
import org.apache.tuscany.sca.policy.IntentAttachPointType;
import org.apache.tuscany.sca.policy.PolicySet;

/**
 * Represents an operation on a service interface.
 *
 * @version $Rev$ $Date$
 */
public class OperationImpl implements Operation {

    private String name;
    private boolean unresolved;
    private DataType outputType;
    private DataType<List<DataType>> inputType;
    private List<DataType> faultTypes;
    private Interface interfaze;
    private ConversationSequence conversationSequence = ConversationSequence.CONVERSATION_NONE;
    private List<ParameterMode> parameterModes = new ArrayList<ParameterMode>();
    private boolean nonBlocking;
    // TODO - WI
    //private WrapperInfo wrapper;
    private boolean inputWrapperStyle;
    private boolean outputWrapperStyle;
    private WrapperInfo inputWrapper;
    private WrapperInfo outputWrapper;
    private boolean dynamic;
    private Map<QName, List<DataType<XMLType>>> faultBeans;

    private List<PolicySet> applicablePolicySets = new ArrayList<PolicySet>();
    private List<PolicySet> policySets = new ArrayList<PolicySet>();
    private List<Intent> requiredIntents = new ArrayList<Intent>();
    private IntentAttachPointType type;

    /**
     * @param name
     */
    public OperationImpl() {
        inputType = new DataTypeImpl<List<DataType>>("idl:input", Object[].class, new ArrayList<DataType>());
        faultTypes = new ArrayList<DataType>();
        faultBeans = new HashMap<QName, List<DataType<XMLType>>>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUnresolved() {
        return unresolved;
    }

    public void setUnresolved(boolean undefined) {
        this.unresolved = undefined;
    }

    /**
     * @return the faultTypes
     */
    public List<DataType> getFaultTypes() {
        return faultTypes;
    }

    /**
     * @param faultTypes the faultTypes to set
     */
    public void setFaultTypes(List<DataType> faultTypes) {
        this.faultTypes = faultTypes;
    }

    /**
     * @return the inputType
     */
    public DataType<List<DataType>> getInputType() {
        return inputType;
    }

    /**
     * @param inputType the inputType to set
     */
    public void setInputType(DataType<List<DataType>> inputType) {
        this.inputType = inputType;
    }

    /**
     * @return the outputType
     */
    public DataType getOutputType() {
        return outputType;
    }

    /**
     * @param outputType the outputType to set
     */
    public void setOutputType(DataType outputType) {
        this.outputType = outputType;
    }

    /**
     * @return the interface
     */
    public Interface getInterface() {
        return interfaze;
    }

    /**
     * @param interfaze the interface to set
     */
    public void setInterface(Interface interfaze) {
        this.interfaze = interfaze;
    }

    /**
     * @return the conversationSequence
     */
    public ConversationSequence getConversationSequence() {
        return conversationSequence;
    }

    /**
     * @param conversationSequence the conversationSequence to set
     */
    public void setConversationSequence(ConversationSequence conversationSequence) {
        this.conversationSequence = conversationSequence;
    }

    /**
     * @return the nonBlocking
     */
    public boolean isNonBlocking() {
        return nonBlocking;
    }

    /**
     * @param nonBlocking the nonBlocking to set
     */
    public void setNonBlocking(boolean nonBlocking) {
        this.nonBlocking = nonBlocking;
    }

    /**
     * @return the wrapperInfo
     */
    // TODO - WI
    //public WrapperInfo getWrapper() {
    //    return wrapper;
    //}

    /**
     * @param wrapperInfo the wrapperInfo to set
     */
    // TODO - WI
    //public void setWrapper(WrapperInfo wrapperInfo) {
    //    this.wrapper = wrapperInfo;
    //}

    public WrapperInfo getInputWrapper() {
        return inputWrapper;
    }

    public void setInputWrapper(WrapperInfo inputWrapper) {
        this.inputWrapper = inputWrapper;
    }

    public WrapperInfo getOutputWrapper() {
        return outputWrapper;
    }

    public void setOutputWrapper(WrapperInfo outputWrapper) {
        this.outputWrapper = outputWrapper;
    }

    /**
     * @return the inputWrapperStyle
     */
    public boolean isInputWrapperStyle() {
        return inputWrapperStyle;
    }

    /**
     * @param inputWrapperStyle the wrapperStyle to set
     */
    public void setInputWrapperStyle(boolean wrapperStyle) {
        this.inputWrapperStyle = wrapperStyle;
    }

    /**
     * @return the outputWrapperStyle
     */
    public boolean isOutputWrapperStyle() {
        return outputWrapperStyle;
    }

    /**
     * @param outputWrapperStyle the wrapperStyle to set
     */
    public void setOutputWrapperStyle(boolean wrapperStyle) {
        this.outputWrapperStyle = wrapperStyle;
    }

    // TODO - WI
    // These are deprecated and are a little awkward now that
    // we split input wrapper from output wrapper
    public String getDataBinding() {
        return inputWrapper != null ? inputWrapper.getDataBinding() : null;
    }

    public void setDataBinding(String dataBinding) {
        if (inputWrapper != null) {
            inputWrapper.setDataBinding(dataBinding);
        }
        if (outputWrapper != null) {
            outputWrapper.setDataBinding(dataBinding);
        }
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean b) {
        this.dynamic = b;
    }

    public Map<QName, List<DataType<XMLType>>> getFaultBeans() {
        return faultBeans;
    }

    public void setFaultBeans(Map<QName, List<DataType<XMLType>>> faultBeans) {
        this.faultBeans = faultBeans;
    }

    @Override
    public OperationImpl clone() throws CloneNotSupportedException {
        OperationImpl copy = (OperationImpl) super.clone();

        final List<DataType> clonedFaultTypes = new ArrayList<DataType>(this.faultTypes.size());
        for (DataType t : this.faultTypes) {
            clonedFaultTypes.add((DataType) t.clone());
        }
        copy.faultTypes = clonedFaultTypes;

        List<DataType> clonedLogicalTypes = new ArrayList<DataType>();
        for (DataType t : inputType.getLogical()) {
            DataType type = (DataType) t.clone();
            clonedLogicalTypes.add(type);
        }
        DataType<List<DataType>> clonedInputType =
            new DataTypeImpl<List<DataType>>(inputType.getPhysical(), clonedLogicalTypes);
        clonedInputType.setDataBinding(inputType.getDataBinding());
        copy.inputType = clonedInputType;

        if (this.outputType != null) {
            copy.outputType = (DataType) this.outputType.clone();
        }

        return copy;
    }

    public List<PolicySet> getApplicablePolicySets() {
        return applicablePolicySets;
    }

    public List<PolicySet> getPolicySets() {
        return policySets;
    }

    public List<Intent> getRequiredIntents() {
        return requiredIntents;
    }

    public IntentAttachPointType getType() {
        return type;
    }

    public void setType(IntentAttachPointType type) {
        this.type = type;
    }

    public List<ParameterMode> getParameterModes() {
        return parameterModes;
    }

}
