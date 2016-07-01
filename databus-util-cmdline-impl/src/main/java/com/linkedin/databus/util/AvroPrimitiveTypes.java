/*
 * $Id: AvroPrimitiveTypes.java 151262 2010-11-17 23:00:29Z jwesterm $
 */
package com.linkedin.databus.util;
/*
 *
 * Copyright 2013 LinkedIn Corp. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/


/**
 * @author Jemiah Westerman<jwesterman@linkedin.com>
 * @version $Revision: 151262 $
 */
public enum AvroPrimitiveTypes
{
  INTEGER("int","java.lang.Integer"),
  BIGINT("long","java.lang.Long"),
  LONG("long","java.lang.Long"),
  RAW("bytes","java.lang.Byte"),
  FLOAT("float","java.lang.Float"),
  DOUBLE("double","java,lang.Double"),
  CLOB("string","java.lang.String"),
  VARCHAR("string","java.lang.String"),
  VARCHAR2("string","java.lang.String"),
  NVARCHAR("string","java.lang.String"),
  NVARCHAR2("string","java.lang.String"),
  TIMESTAMP("long","java.lang.Long"),
  CHAR("string","java.lang.String"),
  DATE("long","java.util.Date"),
  BLOB("bytes","java.lang.Byte"), 
  ARRAY("array","java.lang.Array"),
  TABLE("record","java.lang.Object"),
  INT("int","java.lang.Integer"),
  XMLTYPE("string","java.lang.String");

  private final String _avroType;
  private final String _javaObjectType; 
  private AvroPrimitiveTypes(String avroType,String javaObjectType)
  {
    _avroType = avroType;
    _javaObjectType=javaObjectType;
  }
  public String getAvroType()
  {
    return _avroType;
  }
  public String getJavaObjType()
  {
    return _javaObjectType;
  }
}
