/*
 * Copyright 2007-2017 UnboundID Corp.
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2007-2017 UnboundID Corp.
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
package com.unboundid.ldif;



import org.testng.annotations.Test;

import com.unboundid.ldap.sdk.ChangeType;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.ModifyDNRequest;
import com.unboundid.ldap.sdk.RDN;
import com.unboundid.util.ByteStringBuffer;
import com.unboundid.util.LDAPSDKUsageException;



/**
 * This class provides a set of test cases for the LDIFModifyDNChangeRecord
 * class.
 */
public class LDIFModifyDNChangeRecordTestCase
       extends LDIFTestCase
{
  /**
   * Tests the first constructor with a non-{@code null} newSuperior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor1WithNewSuperior()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    assertNotNull(r.getDN());
    assertEquals(r.getDN(), "ou=People,dc=example,dc=com");
    assertEquals(r.getParsedDN(), new DN("ou=People,dc=example,dc=com"));

    assertNotNull(r.getNewRDN());
    assertEquals(r.getNewRDN(), "ou=Users");
    assertEquals(r.getParsedNewRDN(), new RDN("ou=Users"));

    assertTrue(r.deleteOldRDN());

    assertNotNull(r.getNewSuperiorDN());
    assertEquals(r.getNewSuperiorDN(), "o=example.com");
    assertEquals(r.getParsedNewSuperiorDN(), new DN("o=example.com"));

    assertEquals(r.getNewDN(), new DN("ou=Users,o=example.com"));

    ModifyDNRequest modifyDNRequest = r.toModifyDNRequest();
    assertEquals(modifyDNRequest.getDN(), "ou=People,dc=example,dc=com");

    String[] ldifLines = r.toLDIF();
    assertNotNull(ldifLines);
    assertEquals(ldifLines.length, 5);

    assertEquals(r.getChangeType(), ChangeType.MODIFY_DN);

    r.hashCode();

    ByteStringBuffer byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer);
    assertNotNull(byteBuffer.toString());

    byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer, 10);
    assertNotNull(byteBuffer.toString());

    StringBuilder stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer);
    assertNotNull(r.toString());

    stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer, 10);
    assertNotNull(r.toString());

    assertNotNull(r.toLDIFString());
    assertNotNull(r.toLDIFString(10));
    assertNotNull(r.toString());
  }



  /**
   * Tests the first constructor with a {@code null} newSuperior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor1WithoutNewSuperior()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      false, null);

    assertNotNull(r.getDN());
    assertEquals(r.getDN(), "ou=People,dc=example,dc=com");
    assertEquals(r.getParsedDN(), new DN("ou=People,dc=example,dc=com"));

    assertNotNull(r.getNewRDN());
    assertEquals(r.getNewRDN(), "ou=Users");
    assertEquals(r.getParsedNewRDN(), new RDN("ou=Users"));

    assertFalse(r.deleteOldRDN());

    assertNull(r.getNewSuperiorDN());
    assertNull(r.getParsedNewSuperiorDN());

    assertEquals(r.getNewDN(), new DN("ou=Users,dc=example,dc=com"));

    ModifyDNRequest modifyDNRequest = r.toModifyDNRequest();
    assertEquals(modifyDNRequest.getDN(), "ou=People,dc=example,dc=com");

    String[] ldifLines = r.toLDIF();
    assertNotNull(ldifLines);
    assertEquals(ldifLines.length, 4);

    assertEquals(r.getChangeType(), ChangeType.MODIFY_DN);

    r.hashCode();

    ByteStringBuffer byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer);
    assertNotNull(byteBuffer.toString());

    byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer, 10);
    assertNotNull(byteBuffer.toString());

    StringBuilder stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer);
    assertNotNull(r.toString());

    stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer, 10);
    assertNotNull(r.toString());

    assertNotNull(r.toLDIFString());
    assertNotNull(r.toLDIFString(10));
    assertNotNull(r.toString());
  }



  /**
   * Tests the first constructor with a {@code null} DN.
   */
  @Test(expectedExceptions = { LDAPSDKUsageException.class })
  public void testConstructor1NullDN()
  {
    new LDIFModifyDNChangeRecord(null, "ou=Users", true, null);
  }



  /**
   * Tests the first constructor with a {@code null} new RDN.
   */
  @Test(expectedExceptions = { LDAPSDKUsageException.class })
  public void testConstructor1NullNewRDN()
  {
    new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", null, true,
                                 null);
  }



  /**
   * Tests the second constructor with a newSuperior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor2WithNewSuperior()
         throws Exception
  {
    ModifyDNRequest modifyDNRequest =
         new ModifyDNRequest("ou=People,dc=example,dc=com", "ou=Users", false,
                             "o=example.com");

    LDIFModifyDNChangeRecord r = new LDIFModifyDNChangeRecord(modifyDNRequest);

    assertNotNull(r.getDN());
    assertEquals(r.getDN(), "ou=People,dc=example,dc=com");
    assertEquals(r.getParsedDN(), new DN("ou=People,dc=example,dc=com"));

    assertNotNull(r.getNewRDN());
    assertEquals(r.getNewRDN(), "ou=Users");
    assertEquals(r.getParsedNewRDN(), new RDN("ou=Users"));

    assertFalse(r.deleteOldRDN());

    assertNotNull(r.getNewSuperiorDN());
    assertEquals(r.getNewSuperiorDN(), "o=example.com");
    assertEquals(r.getParsedNewSuperiorDN(), new DN("o=example.com"));

    assertEquals(r.getNewDN(), new DN("ou=Users,o=example.com"));

    modifyDNRequest = r.toModifyDNRequest();
    assertEquals(modifyDNRequest.getDN(), "ou=People,dc=example,dc=com");

    String[] ldifLines = r.toLDIF();
    assertNotNull(ldifLines);
    assertEquals(ldifLines.length, 5);

    assertEquals(r.getChangeType(), ChangeType.MODIFY_DN);

    r.hashCode();

    ByteStringBuffer byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer);
    assertNotNull(byteBuffer.toString());

    byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer, 10);
    assertNotNull(byteBuffer.toString());

    StringBuilder stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer);
    assertNotNull(r.toString());

    stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer, 10);
    assertNotNull(r.toString());

    assertNotNull(r.toLDIFString());
    assertNotNull(r.toLDIFString(10));
    assertNotNull(r.toString());
  }



  /**
   * Tests the second constructor without a newSuperior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testConstructor2WithoutNewSuperior()
         throws Exception
  {
    ModifyDNRequest modifyDNRequest =
         new ModifyDNRequest("ou=People,dc=example,dc=com", "ou=Users", true);

    LDIFModifyDNChangeRecord r = new LDIFModifyDNChangeRecord(modifyDNRequest);

    assertNotNull(r.getDN());
    assertEquals(r.getDN(), "ou=People,dc=example,dc=com");
    assertEquals(r.getParsedDN(), new DN("ou=People,dc=example,dc=com"));

    assertNotNull(r.getNewRDN());
    assertEquals(r.getNewRDN(), "ou=Users");
    assertEquals(r.getParsedNewRDN(), new RDN("ou=Users"));

    assertTrue(r.deleteOldRDN());

    assertNull(r.getNewSuperiorDN());
    assertNull(r.getParsedNewSuperiorDN());

    assertEquals(r.getNewDN(), new DN("ou=Users,dc=example,dc=com"));

    modifyDNRequest = r.toModifyDNRequest();
    assertEquals(modifyDNRequest.getDN(), "ou=People,dc=example,dc=com");

    String[] ldifLines = r.toLDIF();
    assertNotNull(ldifLines);
    assertEquals(ldifLines.length, 4);

    assertEquals(r.getChangeType(), ChangeType.MODIFY_DN);

    r.hashCode();

    ByteStringBuffer byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer);
    assertNotNull(byteBuffer.toString());

    byteBuffer = new ByteStringBuffer();
    r.toLDIF(byteBuffer, 10);
    assertNotNull(byteBuffer.toString());

    StringBuilder stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer);
    assertNotNull(r.toString());

    stringBuffer = new StringBuilder();
    r.toLDIFString(stringBuffer, 10);
    assertNotNull(r.toString());

    assertNotNull(r.toLDIFString());
    assertNotNull(r.toLDIFString(10));
    assertNotNull(r.toString());
  }



  /**
   * Tests the second constructor with a {@code null} modify DN request.
   */
  @Test(expectedExceptions = { LDAPSDKUsageException.class,
                               NullPointerException.class  })
  public void testConstructor2NullModifyDNRequest()
  {
    new LDIFModifyDNChangeRecord((ModifyDNRequest) null);
  }



  /**
   * Tests the {@code getNewDN} method for an entry that does not have a
   * parent and for which there is no new superior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testGetNewDNNoParentNoNewSuperior()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("o=example.com", "o=example.net", true,
                                      null);

    assertEquals(r.getNewDN(), new DN("o=example.net"));
  }



  /**
   * Tests the {@code getNewDN} method for an entry that does not have a
   * parent but for which there is a new superior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testGetNewDNNoParentWithNewSuperior()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("o=example.com", "dc=example", true,
                                      "dc=com");

    assertEquals(r.getNewDN(), new DN("dc=example,dc=com"));
  }



  /**
   * Tests the {@code hashCode} method for a change record with an invalid DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testHashCodeInvalidDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("invalid", "ou=Users", true, null);

    r.hashCode();
  }



  /**
   * Tests the {@code hashCode} method for a change record with an invalid new
   * RDN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testHashCodeInvalidNewRDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "invalid",
                                      true, null);

    r.hashCode();
  }



  /**
   * Tests the {@code hashCode} method for a change record with an invalid new
   * superior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testHashCodeInvalidNewSuperiorDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "invalid");

    r.hashCode();
  }



  /**
   * Tests the {@code equals} method with a {@code null} object.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsNull()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    assertFalse(r.equals(null));
  }



  /**
   * Tests the {@code equals} method with the same object.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsIdentity()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    assertTrue(r.equals(r));
  }



  /**
   * Tests the {@code equals} method with an equivalent object.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsEquivalent()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    assertTrue(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with a different DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsDifferentDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=Persons,dc=example,dc=com",
                                      "ou=Users", true, "o=example.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with an invalid DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsInvalidDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("invalid",
                                      "ou=Users", true, "o=example.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with a different new RDN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsDifferentNewRDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com",
                                      "ou=Persons", true, "o=example.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with an invalid new RDN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsInvalidNewRDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "invalid",
                                      true, "o=example.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with a different delete old RDN value.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsDifferentDeleteOldRDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      false, "o=example.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with a different new superior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsDifferentNewSuperiorDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example2.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with an invalid new superior DN.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsInvalidNewSuperiorDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "invalid");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method in which the does not have a new superior
   * DN but the second does.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsFirstMissingNewSuperiorDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, null);

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method in which the first has a new superior DN
   * but the second does not.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsSecondMissingNewSuperiorDN()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFModifyDNChangeRecord r2 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, null);

    assertFalse(r1.equals(r2));
  }



  /**
   * Tests the {@code equals} method with an object that is not a change record.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsNotChangeRecord()
         throws Exception
  {
    LDIFModifyDNChangeRecord r =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    assertFalse(r.equals("not change record"));
  }



  /**
   * Tests the {@code equals} method with an object that is a change record but
   * not a modify DN change record.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEqualsNotModifyDNChangeRecord()
         throws Exception
  {
    LDIFModifyDNChangeRecord r1 =
         new LDIFModifyDNChangeRecord("ou=People,dc=example,dc=com", "ou=Users",
                                      true, "o=example.com");

    LDIFDeleteChangeRecord r2 = new LDIFDeleteChangeRecord("dc=example,dc=com");

    assertFalse(r1.equals(r2));
  }
}
