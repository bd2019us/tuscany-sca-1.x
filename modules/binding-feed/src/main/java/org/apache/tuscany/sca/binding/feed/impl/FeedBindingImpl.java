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

package org.apache.tuscany.sca.binding.feed.impl;

import java.util.Collections;
import java.util.List;

import org.apache.tuscany.sca.binding.feed.FeedBinding;
import org.apache.tuscany.sca.policy.Intent;
import org.apache.tuscany.sca.policy.PolicySet;

/**
 * Implementation of the Feed binding model.
 */
public class FeedBindingImpl implements FeedBinding {

    private String name;
    private String uri;
    private String feedType;

    public String getName() {
        return name;
    }

    public String getURI() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public List<PolicySet> getPolicySets() {
        // The sample binding does not support policies
        return Collections.emptyList();
    }

    public List<Intent> getRequiredIntents() {
        // The binding does not support policies
        return Collections.emptyList();
    }

    public List<Object> getExtensions() {
        // The binding does not support extensions
        return Collections.emptyList();
    }

    public boolean isUnresolved() {
        return false;
    }

    public void setUnresolved(boolean unresolved) {
        // The binding is always resolved
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String value) {
        feedType = value;
    }

}