/*
 * Copyright 2014-2017 UnboundID Corp.
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
package com.unboundid.ldap.sdk.unboundidds.extensions;



import com.unboundid.asn1.ASN1Element;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Debug;
import com.unboundid.util.NotMutable;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.Validator;

import static com.unboundid.ldap.sdk.unboundidds.extensions.ExtOpMessages.*;



/**
 * This class provides an implementation of a get changelog batch change
 * selection criteria value that indicates that the server should only return
 * changes that are associated with a specified notification destination, as
 * specified by the entryUUID for the notification destination to target.
 * <BR>
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class NotificationDestinationChangeSelectionCriteria
       extends ChangelogBatchChangeSelectionCriteria
{
  /**
   * The inner BER type that should be used for encoded elements that represent
   * a notification destination get changelog batch selection criteria value.
   */
  static final byte TYPE_SELECTION_CRITERIA_NOTIFICATION_DESTINATION =
       (byte) 0x84;



  // The entryUUID for the for the notification destination to target.
  private final String destinationEntryUUID;



  /**
   * Creates a new notification destination change selection criteria value with
   * the specified destination entryUUID.
   *
   * @param  destinationEntryUUID  The entryUUID for the notification
   *                               destination to target.  It must not be
   *                               {@code null}.
   */
  public NotificationDestinationChangeSelectionCriteria(
              final String destinationEntryUUID)
  {
    Validator.ensureNotNull(destinationEntryUUID);

    this.destinationEntryUUID = destinationEntryUUID;
  }



  /**
   * Decodes the provided ASN.1 element, which is the inner element of a
   * changelog batch change selection criteria element, as an all attributes
   * change selection criteria value.
   *
   * @param  innerElement  The inner element of a changelog batch change
   *                       selection criteria element to be decoded.
   *
   * @return  The decoded all attributes change selection criteria value.
   *
   * @throws  LDAPException  If a problem is encountered while trying to decode
   *                         the provided element as the inner element of an all
   *                         attributes change selection criteria value.
   */
  static NotificationDestinationChangeSelectionCriteria decodeInnerElement(
              final ASN1Element innerElement)
         throws LDAPException
  {
    try
    {
      return new NotificationDestinationChangeSelectionCriteria(
           ASN1OctetString.decodeAsOctetString(innerElement).stringValue());
    }
    catch (final Exception e)
    {
      Debug.debugException(e);
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_NOT_DEST_CHANGE_SELECTION_CRITERIA_DECODE_ERROR.get(
                StaticUtils.getExceptionMessage(e)),
           e);
    }
  }



  /**
   * Retrieves the entryUUID for the target notification destination.
   *
   * @return  The entryUUID for the target notification destination.
   */
  public String getDestinationEntryUUID()
  {
    return destinationEntryUUID;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public ASN1Element encodeInnerElement()
  {
    return new ASN1OctetString(TYPE_SELECTION_CRITERIA_NOTIFICATION_DESTINATION,
         destinationEntryUUID);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(final StringBuilder buffer)
  {
    buffer.append("NotificationDestinationChangeSelectionCriteria(" +
         "destinationEntryUUID='");
    buffer.append(destinationEntryUUID);
    buffer.append("')");
  }
}
