/*
 * Copyright 2008-2017 UnboundID Corp.
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2008-2017 UnboundID Corp.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk.unboundidds.controls;



import org.testng.annotations.Test;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSDKTestCase;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.schema.AttributeTypeDefinition;
import com.unboundid.ldap.sdk.schema.ObjectClassDefinition;
import com.unboundid.ldap.sdk.schema.Schema;



/**
 * This class provides a set of test cases for the
 * ExtendedSchemaInfoRequestControl class.
 */
public class ExtendedSchemaInfoRequestControlTestCase
       extends LDAPSDKTestCase
{
  /**
   * Tests the first constructor.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor1()
         throws Exception
  {
    ExtendedSchemaInfoRequestControl c =
         new ExtendedSchemaInfoRequestControl();
    c = new ExtendedSchemaInfoRequestControl(c);

    assertFalse(c.isCritical());

    assertNotNull(c.getControlName());
    assertNotNull(c.toString());
  }



  /**
   * Tests the second constructor with a criticality of TRUE.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor2True()
         throws Exception
  {
    ExtendedSchemaInfoRequestControl c =
         new ExtendedSchemaInfoRequestControl(true);
    c = new ExtendedSchemaInfoRequestControl(c);

    assertTrue(c.isCritical());

    assertNotNull(c.getControlName());
    assertNotNull(c.toString());
  }



  /**
   * Tests the second constructor with a criticality of FALSE.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor2False()
         throws Exception
  {
    ExtendedSchemaInfoRequestControl c =
         new ExtendedSchemaInfoRequestControl(false);
    c = new ExtendedSchemaInfoRequestControl(c);

    assertFalse(c.isCritical());

    assertNotNull(c.getControlName());
    assertNotNull(c.toString());
  }



  /**
   * Tests the third constructor with a generic control that contains a value.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test(expectedExceptions = { LDAPException.class })
  public void testConstructor3WithValue()
         throws Exception
  {
    Control c = new Control(ExtendedSchemaInfoRequestControl.
                                 EXTENDED_SCHEMA_INFO_REQUEST_OID,
                            true, new ASN1OctetString("foo"));
    new ExtendedSchemaInfoRequestControl(c);
  }



  /**
   * Sends a request to the server containing the extended schema info request
   * control.
   * <BR><BR>
   * Access to a Directory Server instance is required for complete processing.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testSendRequestWithExtendedSchemaInfoRequestControl()
         throws Exception
  {
    if (! isDirectoryInstanceAvailable())
    {
      return;
    }

    LDAPConnection conn = getAdminConnection();

    String schemaEntryDN = Schema.getSubschemaSubentryDN(conn, null);
    assertNotNull(schemaEntryDN);

    SearchRequest searchRequest = new SearchRequest(schemaEntryDN,
         SearchScope.BASE, "(objectClass=*)", "*", "+");

    SearchResult result = conn.search(searchRequest);
    assertNotNull(result);
    assertEquals(result.getResultCode(), ResultCode.SUCCESS);
    assertEquals(result.getEntryCount(), 1);

    Schema schema = new Schema(result.getSearchEntries().get(0));
    assertNotNull(schema);

    for (final AttributeTypeDefinition d : schema.getAttributeTypes())
    {
      assertNull(d.getExtensions().get("X-SCHEMA-FILE"));
      assertNull(d.getExtensions().get("X-READ-ONLY"));
    }

    for (final ObjectClassDefinition d : schema.getObjectClasses())
    {
      assertNull(d.getExtensions().get("X-SCHEMA-FILE"));
      assertNull(d.getExtensions().get("X-READ-ONLY"));
    }


    searchRequest.addControl(new ExtendedSchemaInfoRequestControl());

    result = conn.search(searchRequest);
    assertNotNull(result);
    assertEquals(result.getResultCode(), ResultCode.SUCCESS);
    assertEquals(result.getEntryCount(), 1);

    schema = new Schema(result.getSearchEntries().get(0));
    assertNotNull(schema);

    for (final AttributeTypeDefinition d : schema.getAttributeTypes())
    {
      assertNotNull(d.getExtensions().get("X-SCHEMA-FILE"));
      assertNotNull(d.getExtensions().get("X-READ-ONLY"));
    }

    for (final ObjectClassDefinition d : schema.getObjectClasses())
    {
      assertNotNull(d.getExtensions().get("X-SCHEMA-FILE"));
      assertNotNull(d.getExtensions().get("X-READ-ONLY"));
    }

    conn.close();
  }
}
