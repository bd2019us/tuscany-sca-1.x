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
package org.apache.tuscany.service.persistence.store;

import java.util.UUID;

import org.apache.tuscany.spi.component.SCAObject;

/**
 * Implementations provide a persistent store for runtime data such as conversational state. A persistent store could be
 * implemented in a durable fashion using JDBC or a journaling system, or using a non-durable mechanism such as an
 * in-memory map.
 *
 * @version $Rev$ $Date$
 */
public interface Store {

    /* Used to indicate an entry should not expire */
    long NEVER = -1;

    /**
     * Adds the given record to the store. Implementations may choose different strategies for writing data such as
     * write-through or write-behind.
     *
     * @param owner
     * @param id         the unique id of the record
     * @param object     the object representing the data to write
     * @param expiration the time in milliseconds when the entry expires
     * @throws StoreWriteException if an error occurs during the write operation
     */
    void appendRecord(SCAObject owner, UUID id, Object object, long expiration) throws StoreWriteException;

    void updateRecord(SCAObject owner, UUID id, Object object) throws StoreWriteException;

    /**
     * Returns the deserialized object in the store corresponding to the given id
     *
     * @return the deserialized object or null if one is not found
     */
    Object readRecord(SCAObject owner, UUID id) throws StoreReadException;

    /**
     * Removes all records from the store
     */
    void removeRecords() throws StoreWriteException;

    /**
     * Initiates a recovery operation, for example during restart after a crash
     *
     * @param listener the listener to receive recovery callback events
     */
    void recover(RecoveryListener listener) throws StoreReadException;

}
