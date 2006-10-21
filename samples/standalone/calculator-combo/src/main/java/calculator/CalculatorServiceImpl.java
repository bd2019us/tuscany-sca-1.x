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
package calculator;

import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;


/**
 * An implementation of the Calculator service.
 */
@Scope("MODULE")
public class CalculatorServiceImpl implements CalculatorService {

	@Reference
    protected AddService addService;
	
	@Reference
    protected SubtractService subtractService;
	
	@Reference
    protected MultiplyService multiplyService;
	
	@Reference
    protected DivideService divideService;
	
	@Reference
     protected CalculatorService rmiCalculatorService;

    public double add(double n1, double n2) {
        return addService.add(n1, n2);
    }

    public double subtract(double n1, double n2) {
        return subtractService.subtract(n1, n2);
    }

    public double multiply(double n1, double n2) {
        return rmiCalculatorService.multiply(n1, n2);
    }

    public double divide(double n1, double n2) {
        return divideService.divide(n1, n2);
    }


}
