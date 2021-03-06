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

package org.apache.tuscany.sca.binding.gdata;

import java.net.Socket;
import java.net.URL;

import junit.framework.Assert;

import org.apache.tuscany.sca.host.embedded.SCADomain;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gdata.client.Query;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.PlainTextConstruct;

public class GoogleContactsServiceTestCase {

    private static SCADomain scaDomainConsumer = null;
    private static CustomerClient testService = null;    
    
    public GoogleContactsServiceTestCase(){

    }
    
    @BeforeClass
    public static void setUp() throws Exception {
        //Initialize the GData client service (Reference Binding test)
        if (internetConnected()) {
            scaDomainConsumer = SCADomain.newInstance("org/apache/tuscany/sca/binding/gdata/ConsumerGoogleContacts.composite");
            testService = scaDomainConsumer.getService(CustomerClient.class, "CustomerClient");  
        }
    }

    @AfterClass
    public static void tearDown(){
        if (scaDomainConsumer != null) {
            scaDomainConsumer.close();
        }
    }        
    
    @Test
    public void testClientGetFeed() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        Feed feed = testService.clientGetFeed();
        System.out.println("feed title: " + feed.getTitle().getPlainText());        
        Assert.assertEquals("Haibo Zhao's Contacts", feed.getTitle().getPlainText());
     }
    
    
    @Test
    public void testClientGetEntry() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        String entryID = "12feeeb38ab87365";
        Entry contactEntry = testService.clientGetEntry(entryID);
        //System.out.println("Entry ID: " + contactEntry.getId());
        Assert.assertTrue(contactEntry.getId().endsWith(entryID));
    }
    
    
    @Test
    public void testClientPut() throws Exception {  
        if (testService == null) {
            // no internet connection
            return;
        }
        String entryID = "12feeeb38ab87365";          
        String newBlogEntryTitle = "updatedTitleByGoogleContactsConsumerTestCase";
        testService.clientPut(entryID, newBlogEntryTitle);      //update the title
        Thread.sleep(Constants.SLEEP_INTERVAL);            
        Entry updatedEntry = testService.clientGetEntry(entryID);         
        Assert.assertEquals(newBlogEntryTitle, updatedEntry.getTitle().getPlainText());
    }
    
    

    @Test
    public void testClientPost() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        String blogEntryTitle = "titleByGoogleContactsTestcase";
        Entry newEntry = new Entry();
        newEntry.setTitle(new PlainTextConstruct(blogEntryTitle));
        newEntry.setContent(new PlainTextConstruct("contentByGoogleContactsTestCase"));
        Entry postedEntry = testService.clientPost(newEntry);        
        Assert.assertEquals(blogEntryTitle, postedEntry.getTitle().getPlainText());
    }

    
    @Test
    public void testClientDelete() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        
        //This test case might fail
        //because Google blogger service has limitation on new posts allowed everyday/every hour?
        
        //System.out.println("testClientDelete");
        //We create a new post, and then delete it
        Entry newEntry = new Entry();
        newEntry.setTitle(new PlainTextConstruct("contactEntryShouldNotApear"));
        newEntry.setContent(new PlainTextConstruct("contactByBloggerShouldNotAppear"));
        Entry postedEntry = testService.clientPost(newEntry);
        Thread.sleep(Constants.SLEEP_INTERVAL); 
        
        //System.out.println("ID: " + postedEntry.getId());
        
        int idStartPosition = postedEntry.getId().lastIndexOf("/");
        String postedEntryID = postedEntry.getId().substring(idStartPosition+1);        
        //System.out.println("postedEntryID: " + postedEntryID );
        
        //Before deletion
        for (int i = 0; i < 5; i++) { // make 5 attempts because of timing issues 
            try {
                Entry entry00 = testService.clientGetEntry(postedEntryID);
                //System.out.println("Before Deletion: " + entry00.getId());
                break;  // get was successful
            } catch (Exception e) {
                if (i < 4) {
                    System.out.println("Get failed, retrying...");
                } else {
                    throw e;  // final attempt, give up
                }
            }
        }
        
        //Delete this entry
        for (int i = 0; i < 5; i++) { // make 5 attempts because of timing issues 
            try {
                testService.clientDelete(postedEntryID);
                break;  // delete was successful
            } catch (Exception e) {
                if (i < 4) {
                    System.out.println("Delete failed, retrying...");
                } else {
                    throw e;  // final attempt, give up
                }
            }
        }

        //Worked: this newly posted entry did not appear in the contact list
        //But we need a Junit assertion here
        //Link:  http://haibotuscany.blogspot.com/feeds/posts/default/
        //FIXME: Need an assertion here
        //Assert(....);
    }
    
    
    @Test
    public void testClientQuery() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        Query myQuery = new Query(new URL("http://www.google.com/m8/feeds/contacts/default/base"));
        myQuery.setMaxResults(100);
        //myQuery.setUpdatedMin(startTime);
        myQuery.setUpdatedMax(DateTime.now());
        Feed resultFeed = testService.clientQuery(myQuery);        
        //System.out.println("Query result feed title: " + resultFeed.getTitle().getPlainText());    
        //System.out.println("Query result entry number: "+ resultFeed.getEntries().size());
        //assertEquals("gdata binding tuscany test", resultFeed.getTitle().getPlainText());
     }

    private static boolean internetConnected() {
        try {
            // see whether an internet connection is available 
            Socket testInternet = new Socket("tuscany.apache.org", 80);
            testInternet.close();

            // internet connection available
            return true;

        } catch (Exception e) {
            // no internet connection
            System.out.println("Unable to run test because no internet connection available");
            return false;
        }
    }

}
