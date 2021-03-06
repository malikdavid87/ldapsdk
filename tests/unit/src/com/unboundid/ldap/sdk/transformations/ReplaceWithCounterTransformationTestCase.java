/*
 * Copyright 2016-2017 UnboundID Corp.
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2016-2017 UnboundID Corp.
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
package com.unboundid.ldap.sdk.transformations;



import org.testng.annotations.Test;

import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPSDKTestCase;
import com.unboundid.ldap.sdk.schema.Schema;



/**
 * This class provides a set of test cases for replace with counter
 * transformations.
 */
public final class ReplaceWithCounterTransformationTestCase
       extends LDAPSDKTestCase
{
  /**
   * Provides test coverage for the behavior when provided with a null entry.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testNullEntry()
         throws Exception
  {
    final ReplaceWithCounterTransformation t =
         new ReplaceWithCounterTransformation(null, "uid", 0L, 1L, null, null,
              false);

    assertNull(t.transformEntry(null));
  }



  /**
   * Provides test coverage for the transformEntry method with an initial count
   * of 1, and increment of 2, before and after text, and replacement in DNs.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testTransformEntryEverythingOn()
         throws Exception
  {
    final Schema schema = Schema.getDefaultStandardSchema();
    final ReplaceWithCounterTransformation t =
         new ReplaceWithCounterTransformation(schema, "uid", 1L, 2L, "a", "z",
              true);


    // Test with an entry that has the target attribute.
    Entry e = t.transformEntry(new Entry(
         "dn: uid=test.user,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=a1z,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: a1z",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));


    // Test again with the same entry.  The counter should be different this
    // time.
    e = t.transformEntry(new Entry(
         "dn: uid=test.user,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=a3z,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: a3z",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));


    // Test with an entry that has multiple values for the target attribute.
    e = t.transformEntry(new Entry(
         "dn: uid=test.user,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "uid: another.value",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=a5z,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: a5z",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));


    // Test with an entry that has a multivalued RDN.
    e = t.transformEntry(new Entry(
         "dn: uid=test.user+givenName=Test+sn=User,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "uid: another.value",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=a7z+givenName=Test+sn=User,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: a7z",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));


    // Test with an entry that does not have the target attribute.
    e = t.transformEntry(new Entry(
         "dn: cn=Test User,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: cn=Test User,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));
  }



  /**
   * Provides test coverage for the transformEntry method with a bare bones
   * configuration.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testTransformEntryBareBones()
         throws Exception
  {
    final Schema schema = Schema.getDefaultStandardSchema();
    final ReplaceWithCounterTransformation t =
         new ReplaceWithCounterTransformation(schema, "uid", 0L, 1L, null, null,
              false);


    // Test with an entry that has the target attribute.
    Entry e = t.transformEntry(new Entry(
         "dn: uid=test.user,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=test.user,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: 0",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));


    // Test again with the same entry.  The counter should be different this
    // time.
    e = t.transformEntry(new Entry(
         "dn: uid=test.user,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=test.user,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: 1",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));

    // Test with an entry that has multiple values for the target attribute.
    e = t.transformEntry(new Entry(
         "dn: uid=test.user,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "uid: test.user",
         "uid: another.value",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=test.user,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: 2",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));


    // Test with an entry that does not have the target attribute.
    e = t.transformEntry(new Entry(
         "dn: cn=Test User,ou=People,dc=example,dc=com",
         "objectClass: top",
         "objectClass: person",
         "objectClass: organizationalPerson",
         "objectClass: inetOrgPerson",
         "givenName: Test",
         "sn: User",
         "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: cn=Test User,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));
  }



  /**
   * Provides test coverage for the translate method for entries.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testTranslateEntry()
         throws Exception
  {
    final Schema schema = Schema.getDefaultStandardSchema();
    final ReplaceWithCounterTransformation t =
         new ReplaceWithCounterTransformation(schema, "uid", 1L, 2L, "a", "z",
              true);

    final Entry e = t.translate(
         new Entry(
              "dn: uid=test.user,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: test.user",
              "givenName: Test",
              "sn: User",
              "cn: Test User"),
         0);
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=a1z,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: a1z",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));
  }



  /**
   * Provides test coverage for the translateEntryToWrite method.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testTranslateEntryToWrite()
         throws Exception
  {
    final Schema schema = Schema.getDefaultStandardSchema();
    final ReplaceWithCounterTransformation t =
         new ReplaceWithCounterTransformation(schema, "uid", 1L, 2L, "a", "z",
              true);

    final Entry e = t.translateEntryToWrite(
         new Entry(
              "dn: uid=test.user,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: test.user",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));
    assertNotNull(e);
    assertEquals(e,
         new Entry(
              "dn: uid=a1z,ou=People,dc=example,dc=com",
              "objectClass: top",
              "objectClass: person",
              "objectClass: organizationalPerson",
              "objectClass: inetOrgPerson",
              "uid: a1z",
              "givenName: Test",
              "sn: User",
              "cn: Test User"));
  }
}
