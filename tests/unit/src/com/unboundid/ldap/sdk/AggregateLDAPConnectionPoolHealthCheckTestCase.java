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



import java.util.Collection;

import org.testng.annotations.Test;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;



/**
 * This class provides a set of test cases for the aggregate LDAP connection
 * pool health check.
 */
public final class AggregateLDAPConnectionPoolHealthCheckTestCase
       extends LDAPSDKTestCase
{
  /**
   * Tests the behavior in which the provided collection of health checks is
   * {@code null}.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testNullAggregate()
         throws Exception
  {
    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              (Collection<? extends LDAPConnectionPoolHealthCheck>) null);

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    hc.ensureNewConnectionValid(conn);

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    hc.ensureConnectionValidAfterAuthentication(conn, bindResult);

    hc.ensureConnectionValidForCheckout(conn);

    hc.ensureConnectionValidForRelease(conn);

    hc.ensureConnectionValidForContinuedUse(conn);

    final LDAPException exception =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");
    hc.ensureConnectionValidAfterException(conn, exception);

    conn.close();
  }



  /**
   * Tests the behavior in which the provided collection of health checks is
   * empty.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testEmptyAggregate()
         throws Exception
  {
    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck();

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    hc.ensureNewConnectionValid(conn);

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    hc.ensureConnectionValidAfterAuthentication(conn, bindResult);

    hc.ensureConnectionValidForCheckout(conn);

    hc.ensureConnectionValidForRelease(conn);

    hc.ensureConnectionValidForContinuedUse(conn);

    final LDAPException exception =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");
    hc.ensureConnectionValidAfterException(conn, exception);

    conn.close();
  }



  /**
   * Tests the behavior in which the aggregate health check wraps only a single
   * health check that doesn't throw any exception.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testSuccessfulAggregateOfOne()
         throws Exception
  {
    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              new TestLDAPConnectionPoolHealthCheck());

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    hc.ensureNewConnectionValid(conn);

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    hc.ensureConnectionValidAfterAuthentication(conn, bindResult);

    hc.ensureConnectionValidForCheckout(conn);

    hc.ensureConnectionValidForRelease(conn);

    hc.ensureConnectionValidForContinuedUse(conn);

    final LDAPException exception =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");
    hc.ensureConnectionValidAfterException(conn, exception);

    conn.close();
  }



  /**
   * Tests the behavior in which the aggregate health check wraps only a single
   * health check that always throws an exception.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testFailedAggregateOfOne()
         throws Exception
  {
    final LDAPException ldapException =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");

    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              new TestLDAPConnectionPoolHealthCheck(ldapException,
                   ldapException, ldapException, ldapException, ldapException,
                   ldapException));

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    try
    {
      hc.ensureNewConnectionValid(conn);
      fail("Expected a new connection exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    try
    {
      hc.ensureConnectionValidAfterAuthentication(conn, bindResult);
      fail("Expected a post-authentication exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForCheckout(conn);
      fail("Expected a checkout exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForRelease(conn);
      fail("Expected a release exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForContinuedUse(conn);
      fail("Expected a continued use exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidAfterException(conn, ldapException);
      fail("Expected a post-exception exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    conn.close();
  }



  /**
   * Tests the behavior in which the aggregate health check wraps multiple
   * health checks, but none of them throw any exceptions.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testSuccessfulAggregateOfMultiple()
         throws Exception
  {
    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              new TestLDAPConnectionPoolHealthCheck(),
              new TestLDAPConnectionPoolHealthCheck(),
              new TestLDAPConnectionPoolHealthCheck());

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    hc.ensureNewConnectionValid(conn);

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    hc.ensureConnectionValidAfterAuthentication(conn, bindResult);

    hc.ensureConnectionValidForCheckout(conn);

    hc.ensureConnectionValidForRelease(conn);

    hc.ensureConnectionValidForContinuedUse(conn);

    final LDAPException exception =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");
    hc.ensureConnectionValidAfterException(conn, exception);

    conn.close();
  }



  /**
   * Tests the behavior in which the aggregate health check wraps multiple
   * health checks in which the first check throws an exception.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testAggregateOfMultipleFirstFails()
         throws Exception
  {
    final LDAPException ldapException =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");

    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              new TestLDAPConnectionPoolHealthCheck(ldapException,
                   ldapException, ldapException, ldapException, ldapException,
                   ldapException),
              new TestLDAPConnectionPoolHealthCheck(),
              new TestLDAPConnectionPoolHealthCheck());

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    try
    {
      hc.ensureNewConnectionValid(conn);
      fail("Expected a new connection exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    try
    {
      hc.ensureConnectionValidAfterAuthentication(conn, bindResult);
      fail("Expected a post-authentication exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForCheckout(conn);
      fail("Expected a checkout exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForRelease(conn);
      fail("Expected a release exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForContinuedUse(conn);
      fail("Expected a continued use exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidAfterException(conn, ldapException);
      fail("Expected a post-exception exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    conn.close();
  }



  /**
   * Tests the behavior in which the aggregate health check wraps multiple
   * health checks in which the last check throws an exception.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testAggregateOfMultipleLastFails()
         throws Exception
  {
    final LDAPException ldapException =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");

    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              new TestLDAPConnectionPoolHealthCheck(),
              new TestLDAPConnectionPoolHealthCheck(),
              new TestLDAPConnectionPoolHealthCheck(ldapException,
                   ldapException, ldapException, ldapException, ldapException,
                   ldapException));

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    try
    {
      hc.ensureNewConnectionValid(conn);
      fail("Expected a new connection exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    try
    {
      hc.ensureConnectionValidAfterAuthentication(conn, bindResult);
      fail("Expected a post-authentication exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForCheckout(conn);
      fail("Expected a checkout exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForRelease(conn);
      fail("Expected a release exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForContinuedUse(conn);
      fail("Expected a continued use exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidAfterException(conn, ldapException);
      fail("Expected a post-exception exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    conn.close();
  }



  /**
   * Tests the behavior in which the aggregate health check wraps multiple
   * health checks in which all checks throw an exception.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testAggregateOfMultipleAllFail()
         throws Exception
  {
    final LDAPException ldapException =
         new LDAPException(ResultCode.UNWILLING_TO_PERFORM, "Not gonna do it");

    final AggregateLDAPConnectionPoolHealthCheck hc =
         new AggregateLDAPConnectionPoolHealthCheck(
              new TestLDAPConnectionPoolHealthCheck(ldapException,
                   ldapException, ldapException, ldapException, ldapException,
                   ldapException),
              new TestLDAPConnectionPoolHealthCheck(ldapException,
                   ldapException, ldapException, ldapException, ldapException,
                   ldapException),
              new TestLDAPConnectionPoolHealthCheck(ldapException,
                   ldapException, ldapException, ldapException, ldapException,
                   ldapException));

    assertNotNull(hc.toString());

    final InMemoryDirectoryServer ds = getTestDS();
    final LDAPConnection conn = ds.getConnection();

    try
    {
      hc.ensureNewConnectionValid(conn);
      fail("Expected a new connection exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    final BindResult bindResult = new BindResult(1, ResultCode.SUCCESS, null,
         null, null, null);
    try
    {
      hc.ensureConnectionValidAfterAuthentication(conn, bindResult);
      fail("Expected a post-authentication exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForCheckout(conn);
      fail("Expected a checkout exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForRelease(conn);
      fail("Expected a release exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidForContinuedUse(conn);
      fail("Expected a continued use exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    try
    {
      hc.ensureConnectionValidAfterException(conn, ldapException);
      fail("Expected a post-exception exception");
    }
    catch (final LDAPException le)
    {
      // This was expected.
    }

    conn.close();
  }
}
