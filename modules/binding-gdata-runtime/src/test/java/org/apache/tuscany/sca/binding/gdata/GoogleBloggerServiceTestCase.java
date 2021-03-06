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
import org.junit.Ignore;
import org.junit.Test;

import com.google.gdata.client.Query;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.PlainTextConstruct;

public class GoogleBloggerServiceTestCase {

    private static SCADomain scaDomainConsumer = null;
    private static CustomerClient testService = null;    
    
    @BeforeClass
    public static void setUp() throws Exception {
        //Initialize the GData client service (Reference Binding test)
        if (internetConnected()) {
            scaDomainConsumer = SCADomain.newInstance("org/apache/tuscany/sca/binding/gdata/ConsumerGoogleBlogger.composite");
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
        //System.out.println("feed title: " + feed.getTitle().getPlainText());        
        Assert.assertEquals("gdata binding tuscany test", feed.getTitle().getPlainText());
     }
    
    
    @Test
    public void testClientGetEntry() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        String entryID = "8308734583601887890";
        Entry blogEntry = testService.clientGetEntry(entryID);
        //System.out.println("Entry ID: " + blogEntry.getId());
        Assert.assertTrue(blogEntry.getId().endsWith(entryID));
        //System.out.println("------------------------------------------------------------\n\n");
    }
    
    
    @Test
    public void testClientPut() throws Exception {  
        if (testService == null) {
            // no internet connection
            return;
        }
        String entryID = "2889832689497686762";          
        String newBlogEntryTitle = "updatedTitleByTestCase2";
        testService.clientPut(entryID, newBlogEntryTitle);      //update the title
        Thread.sleep(Constants.SLEEP_INTERVAL);            
        Entry updatedEntry = testService.clientGetEntry(entryID);         
        Assert.assertEquals(newBlogEntryTitle, updatedEntry.getTitle().getPlainText());
    }
    
    

    @Ignore // TUSCANY-3991
    @Test
    public void testClientPost() throws Exception {
        if (testService == null) {
            // no internet connection
            return;
        }
        String blogEntryTitle = "titleByBloogerTestcase000";
        Entry newEntry = new Entry();
        newEntry.setTitle(new PlainTextConstruct(blogEntryTitle));
        newEntry.setContent(new PlainTextConstruct("contentByBloggerTestCase000"));
        Entry postedEntry = testService.clientPost(newEntry);        
        Assert.assertEquals(blogEntryTitle, postedEntry.getTitle().getPlainText());
    }

    
    @Ignore // TUSCANY-3991 
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
        newEntry.setTitle(new PlainTextConstruct("blogEntryShouldNotApear"));
        newEntry.setContent(new PlainTextConstruct("contentByBloggerShouldNotAppear"));
        Entry postedEntry = testService.clientPost(newEntry);
        Thread.sleep(Constants.SLEEP_INTERVAL);        
        int idStartPosition = postedEntry.getId().lastIndexOf("-");
        String postedEntryID = postedEntry.getId().substring(idStartPosition+1);        
        //System.out.println("postedEntryID: " + postedEntryID );
        
        //Before deletion
        Entry entry00 = testService.clientGetEntry(postedEntryID);
        //System.out.println("Before Deleteion: " + entry00.getId());
        
        //Delete this entry
        testService.clientDelete(postedEntryID);

        //Worked: this newly posted entry did not appear in the blogspot website,
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
        Query myQuery = new Query(new URL("http://haibotuscany.blogspot.com/feeds/posts/default"));
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
