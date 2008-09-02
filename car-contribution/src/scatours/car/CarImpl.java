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
package scatours.car;

import java.util.ArrayList;
import java.util.List;

import org.osoa.sca.annotations.Callback;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Scope;
import org.osoa.sca.annotations.Service;

import scatours.common.Search;
import scatours.common.SearchCallback;
import scatours.common.TripItem;
import scatours.common.TripLeg;

/**
 * An implementation of the Hotel service
 */
@Scope("STATELESS")
@Service(interfaces={Search.class})
public class CarImpl implements Search {
    
    private List<CarInfo> cars = new ArrayList<CarInfo>();
    
    @Callback
    protected SearchCallback searchCallback; 

    @Init
    public void init() {
        cars.add(new CarInfo("Premier Cars", 
                             "BMW 5 Series", 
                             "ANU", 
                             "06/12/08",
                             "5",
                             100.00,
                             "USD",
                             "http://localhost:8085/tbd" ));
        cars.add(new CarInfo("Premier Cars", 
                             "Ford Focus", 
                             "ANU", 
                             "06/12/08",
                             "4",
                             60.00,
                             "USD",
                             "http://localhost:8085/tbd" ));
    }
    
    public TripItem[] searchSynch(TripLeg tripLeg) {
        List<TripItem> items = new ArrayList<TripItem>();
        
        // find available hotels
        for(CarInfo car : cars){
            if (car.getLocation().equals(tripLeg.getToLocation())){
                TripItem item = new TripItem("",
                                             "",
                                             "Car",
                                             car.getName(), 
                                             car.getDescription(), 
                                             car.getLocation(), 
                                             tripLeg.getFromDate(),
                                             tripLeg.getToDate(),
                                             car.getPricePerDay(),
                                             car.getCurrency(),
                                             car.getLink());
                items.add(item);
            }
        }
        
        return items.toArray(new TripItem[items.size()]);
    }
    
    public void searchAsynch(TripLeg tripLeg) {
        
        // return available hotels
        searchCallback.searchResults(searchSynch(tripLeg));  
    }
}