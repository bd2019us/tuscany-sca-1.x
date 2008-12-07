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
package org.apache.tuscany.sca.binding.corba.testing.arrays_unions;


/**
* org/apache/tuscany/sca/binding/corba/testing/arrays_unions/TestStruct.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from arrays_unions.idl
* niedziela, 17 sierpie� 2008 15:45:39 CEST
*/

public final class TestStruct implements org.omg.CORBA.portable.IDLEntity
{
  public String oneDimArray[] = null;
  public int twoDimArray[][] = null;
  public float threeDimArray[][][] = null;

  public TestStruct ()
  {
  } // ctor

  public TestStruct (String[] _oneDimArray, int[][] _twoDimArray, float[][][] _threeDimArray)
  {
    oneDimArray = _oneDimArray;
    twoDimArray = _twoDimArray;
    threeDimArray = _threeDimArray;
  } // ctor

} // class TestStruct
