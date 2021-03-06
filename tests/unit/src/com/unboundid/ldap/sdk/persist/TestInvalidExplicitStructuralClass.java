/*
 * Copyright 2009-2017 UnboundID Corp.
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2009-2017 UnboundID Corp.
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
package com.unboundid.ldap.sdk.persist;



/**
 * This class provides an object with the {@code LDAPObject} annotation that has
 * an explicitly-defined structural object class which isn't a valid object
 * class name.
 */
@LDAPObject(structuralClass="not_a_valid_ldap_name")
public class TestInvalidExplicitStructuralClass
{
  @LDAPField(attribute="a", inRDN=true, filterUsage=FilterUsage.ALWAYS_ALLOWED)
  private String a;
}
