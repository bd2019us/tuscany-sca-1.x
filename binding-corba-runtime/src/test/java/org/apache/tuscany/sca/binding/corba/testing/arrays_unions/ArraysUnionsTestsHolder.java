package org.apache.tuscany.sca.binding.corba.testing.arrays_unions;

/**
* org/apache/tuscany/sca/binding/corba/testing/arrays_unions/ArraysUnionsTestsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from arrays_unions.idl
* sobota, 16 sierpie� 2008 00:51:16 CEST
*/

public final class ArraysUnionsTestsHolder implements org.omg.CORBA.portable.Streamable
{
  public org.apache.tuscany.sca.binding.corba.testing.arrays_unions.ArraysUnionsTests value = null;

  public ArraysUnionsTestsHolder ()
  {
  }

  public ArraysUnionsTestsHolder (org.apache.tuscany.sca.binding.corba.testing.arrays_unions.ArraysUnionsTests initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = org.apache.tuscany.sca.binding.corba.testing.arrays_unions.ArraysUnionsTestsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    org.apache.tuscany.sca.binding.corba.testing.arrays_unions.ArraysUnionsTestsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return org.apache.tuscany.sca.binding.corba.testing.arrays_unions.ArraysUnionsTestsHelper.type ();
  }

}
