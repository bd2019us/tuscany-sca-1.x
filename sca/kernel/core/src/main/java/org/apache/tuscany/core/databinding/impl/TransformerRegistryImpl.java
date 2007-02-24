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
package org.apache.tuscany.core.databinding.impl;

import java.util.ArrayList;
import java.util.List;

import org.osoa.sca.annotations.EagerInit;

import org.apache.tuscany.spi.annotation.Autowire;
import org.apache.tuscany.spi.databinding.DataBinding;
import org.apache.tuscany.spi.databinding.DataBindingRegistry;
import org.apache.tuscany.spi.databinding.Transformer;
import org.apache.tuscany.spi.databinding.TransformerRegistry;

/**
 * @version $Rev$ $Date$
 */
@EagerInit
public class TransformerRegistryImpl implements TransformerRegistry {
    private DataBindingRegistry dataBindingRegistry;
    
    private final DirectedGraph<Object, Transformer> graph = new DirectedGraph<Object, Transformer>();

    public void registerTransformer(String sourceType, String resultType, int weight, Transformer transformer) {
        graph.addEdge(sourceType, resultType, transformer, weight);
    }

    public void registerTransformer(Transformer transformer) {
        graph.addEdge(transformer.getSourceDataBinding(),
            transformer.getTargetDataBinding(),
            transformer,
            transformer.getWeight());
    }

    public boolean unregisterTransformer(String sourceType, String resultType) {
        return graph.removeEdge(sourceType, resultType);
    }

    public Transformer getTransformer(String sourceType, String resultType) {
        DirectedGraph<Object, Transformer>.Edge edge = graph.getEdge(sourceType, resultType);
        return (edge == null) ? null : edge.getValue();
    }

    public List<Transformer> getTransformerChain(String sourceType, String resultType) {
        String source = normalize(sourceType);
        String result = normalize(resultType);
        List<Transformer> transformers = new ArrayList<Transformer>();
        DirectedGraph<Object, Transformer>.Path path = graph.getShortestPath(source, result);
        if (path == null) {
            return null;
        }
        for (DirectedGraph<Object, Transformer>.Edge edge : path.getEdges()) {
            transformers.add(edge.getValue());
        }
        return transformers;
    }

    public String toString() {
        return graph.toString();
    }

    /**
     * @param dataBindingRegistry the dataBindingRegistry to set
     */
    @Autowire
    public void setDataBindingRegistry(DataBindingRegistry dataBindingRegistry) {
        this.dataBindingRegistry = dataBindingRegistry;
    }
    
    /**
     * Normalize the id to a name of a data binding as databindings may have aliases
     * @param id
     * @return
     */
    private String normalize(String id) {
        if (dataBindingRegistry != null) {
            DataBinding dataBinding = dataBindingRegistry.getDataBinding(id);
            return dataBinding == null ? id : dataBinding.getName();
        } else {
            return id;
        }
    }

}
