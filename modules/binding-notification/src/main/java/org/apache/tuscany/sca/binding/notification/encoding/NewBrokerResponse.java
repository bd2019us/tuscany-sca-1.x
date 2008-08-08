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
package org.apache.tuscany.sca.binding.notification.encoding;

/**
 * @version $Rev$ $Date$
 */
public class NewBrokerResponse implements EncodingObject {

    private EndProducers endProducers;
    private EndConsumers endConsumers;
    private Brokers brokers;
    private boolean firstBroker;
    
    public EndProducers getEndProducers() {
        return this.endProducers;
    }
    
    public void setEndProducers(EndProducers endProducers) {
        this.endProducers = endProducers;
    }
    
    public EndConsumers getEndConsumers() {
        return this.endConsumers;
    }
    
    public void setEndConsumers(EndConsumers endConsumers) {
        this.endConsumers = endConsumers;
    }
    
    public Brokers getBrokers() {
        return this.brokers;
    }
    
    public void setBrokers(Brokers brokers) {
        this.brokers = brokers;
    }
    
    public boolean isFirstBroker() {
        return this.firstBroker;
    }
    
    public void setFirstBroker(boolean firstBroker) {
        this.firstBroker = firstBroker;
    }
}