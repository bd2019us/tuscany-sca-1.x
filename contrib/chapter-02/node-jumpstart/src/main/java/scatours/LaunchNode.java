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

package scatours;

import org.apache.tuscany.sca.node.SCAClient;
import org.apache.tuscany.sca.node.SCAContribution;
import org.apache.tuscany.sca.node.SCANode;
import org.apache.tuscany.sca.node.SCANodeFactory;

import com.goodvaluetrips.Trips;

public class LaunchNode {

    public static void main(String[] args) throws Exception {
        try {
            SCAContribution gvtContribution = 
              new SCAContribution("goodvaluetrips", 
                                  "../goodvaluetrips-contribution/target/classes");
            SCANode node = SCANodeFactory.newInstance().
               createSCANode("trips.composite", 
                             gvtContribution);
            node.start();

            Trips tripProvider = ((SCAClient)node).getService(Trips.class, 
                                                             "TripProvider/Trips");
            
            System.out.println("Trip boooking code = " + 
                               tripProvider.checkAvailability("FS1APR4", 2));

            node.stop();
            
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}