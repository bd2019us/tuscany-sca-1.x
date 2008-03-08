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

package org.apache.tuscany.sca.binding.feed;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.tuscany.sca.binding.feed.collection.Collection;
import org.osoa.sca.annotations.Scope;



@Scope("COMPOSITE")
public class CustomerCollectionImpl implements Collection {
	private final Abdera abdera = new Abdera();
    private Map<String, Entry> entries = new HashMap<String, Entry>();

    /**
     * Default constructor that initializes collection with couple customer entries
     */
    public CustomerCollectionImpl() {

        for (int i = 0; i < 4; i++) {
            String id = "urn:uuid:customer-" + UUID.randomUUID().toString();
            
            Entry entry = abdera.getFactory().newEntry();
            entry.setId(id);
            entry.setTitle("customer " + "Jane Doe_" + String.valueOf(i));
            
            Content content = this.abdera.getFactory().newContent();
            content.setContentType(Content.Type.TEXT);
            content.setValue("Jane Doe_" + String.valueOf(i));
            
            entry.setContentElement(content);
            
            entry.addLink("" + id, "edit");
            entry.addLink("" + id, "alternate");
    
            entry.setUpdated(new Date());

            entries.put(id, entry);
            System.out.println(">>> id=" + id);
        }
    }

    public Entry post(Entry entry) {
        System.out.println(">>> ResourceCollectionImpl.post entry=" + entry.getTitle());

        String id = "urn:uuid:customer-" + UUID.randomUUID().toString();
        entry.setId(id);

        entry.addLink("" + id, "edit");
        entry.addLink("" + id, "alternate");

        entry.setUpdated(new Date());

        entries.put(id, entry);

        System.out.println(">>> ResourceCollectionImpl.post return id=" + id);

        return entry;
    }

    public Entry get(String id) {
        System.out.println(">>> ResourceCollectionImpl.get id=" + id);
        return entries.get(id);
    }

    public void put(String id, Entry entry) {
        System.out.println(">>> ResourceCollectionImpl.put id=" + id + " entry=" + entry.getTitle());

        entry.setUpdated(new Date());
        entries.put(id, entry);
    }

    public void delete(String id) {
        System.out.println(">>> ResourceCollectionImpl.delete id=" + id);
        entries.remove(id);
    }

    @SuppressWarnings("unchecked")
    public Feed getFeed() {
        System.out.println(">>> ResourceCollectionImpl.get collection");

        Feed feed = this.abdera.getFactory().newFeed();
        feed.setTitle("customers");
        feed.setSubtitle("This is a sample feed");
        feed.setUpdated(new Date());
        feed.addLink("");
        feed.addLink("","self");
        
        

        for (Entry entry : entries.values()) {
        	feed.addEntry(entry);
        }

        return feed;
    }

}
