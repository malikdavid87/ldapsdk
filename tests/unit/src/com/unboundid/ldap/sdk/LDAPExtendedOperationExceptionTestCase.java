/*
 * Copyright 2015-2017 UnboundID Corp.
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2015-2017 UnboundID Corp.
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
package com.unboundid.ldap.sdk;



import org.testng.annotations.Test;

import com.unboundid.asn1.ASN1OctetString;



/**
 * This class provides a set of test cases for the LDAP extended operation
 * exception class.
 */
public final class LDAPExtendedOperationExceptionTestCase
       extends LDAPSDKTestCase
{
  /**
   * Tests an exception with an extended result that doesn't have an OID or
   * value.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testResultWithoutOIDOrValue()
         throws Exception
  {
    final ExtendedResult r = new ExtendedResult(1,
         ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it", null, null, null,
         null, null);

    final LDAPExtendedOperationException e =
         new LDAPExtendedOperationException(r);

    assertNotNull(e.getExtendedResult());

    assertNotNull(e.getResultCode());
    assertEquals(e.getResultCode(), ResultCode.UNWILLING_TO_PERFORM);

    assertNotNull(e.getDiagnosticMessage());
    assertEquals(e.getDiagnosticMessage(), "Not gonna do it");

    assertNull(e.getMatchedDN());

    assertNotNull(e.getReferralURLs());
    assertEquals(e.getReferralURLs().length, 0);

    assertNotNull(e.getResponseControls());
    assertEquals(e.getResponseControls().length, 0);

    assertNull(e.getResponseOID());

    assertNull(e.getResponseValue());

    assertNotNull(e.toLDAPResult());
    assertTrue(e.toLDAPResult() instanceof ExtendedResult);
  }



  /**
   * Tests an exception with an extended result that has an OID and a value.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testResultWithOIDAndValue()
         throws Exception
  {
    final String[] referralURLs =
    {
      "ldap://ds1.example.com/dc=example,dc=com",
      "ldap://ds2.example.com/dc=example,dc=com"
    };

    final Control[] controls =
    {
      new Control("1.2.3.4"),
      new Control("1.2.3.5")
    };

    final ExtendedResult r = new ExtendedResult(1, ResultCode.NO_SUCH_OBJECT,
         "The entry is missing", "dc=example,dc=com", referralURLs,
         "1.2.3.6", new ASN1OctetString("value"), controls);

    final LDAPExtendedOperationException e =
         new LDAPExtendedOperationException(r);

    assertNotNull(e.getExtendedResult());

    assertNotNull(e.getResultCode());
    assertEquals(e.getResultCode(), ResultCode.NO_SUCH_OBJECT);

    assertNotNull(e.getDiagnosticMessage());
    assertEquals(e.getDiagnosticMessage(), "The entry is missing");

    assertNotNull(e.getMatchedDN());
    assertDNsEqual(e.getMatchedDN(), "dc=example,dc=com");

    assertNotNull(e.getReferralURLs());
    assertEquals(e.getReferralURLs().length, 2);

    assertNotNull(e.getResponseControls());
    assertEquals(e.getResponseControls().length, 2);

    assertNotNull(e.getResponseOID());
    assertEquals(e.getResponseOID(), "1.2.3.6");

    assertNotNull(e.getResponseValue());
    assertEquals(e.getResponseValue(), new ASN1OctetString("value"));

    assertNotNull(e.toLDAPResult());
    assertTrue(e.toLDAPResult() instanceof ExtendedResult);
  }
}
