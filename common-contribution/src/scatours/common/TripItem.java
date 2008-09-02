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

package scatours.common;


public class TripItem {
    
    private String id; 
    private String tripId;
    private String type;
    private String name;
    private String description;
    private String location;
    private String fromDate;
    private String toDate;
    private double price;
    private String currency;
    private String link;
    
    public TripItem() {
    }  
    
    public TripItem(String id, 
                    String tripId,
                    String type, 
                    String name, 
                    String description, 
                    String location, 
                    String fromDate, 
                    String toDate, 
                    double price, 
                    String currency, 
                    String link) {
        this.id = id;
        this.tripId = tripId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.location = location;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
        this.currency = currency;
        this.link = link;
    }  
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTripId() {
        return tripId;
    }
    
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }    
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description; 
    }    
    
    public String getLocation() {
        return location;
    } 
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getFromDate() {
        return fromDate;
    }
    
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    
    public String getToDate() {
        return toDate;
    }
    
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
}