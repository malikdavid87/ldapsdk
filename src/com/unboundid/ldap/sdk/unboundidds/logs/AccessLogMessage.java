/*
 * Copyright 2009-2017 UnboundID Corp.
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
package com.unboundid.ldap.sdk.unboundidds.logs;



import com.unboundid.util.NotExtensible;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * This class provides a data structure that holds information about a log
 * message that may appear in the Directory Server access log.
 * <BR>
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class is part of the Commercial Edition of the UnboundID
 *   LDAP SDK for Java.  It is not available for use in applications that
 *   include only the Standard Edition of the LDAP SDK, and is not supported for
 *   use in conjunction with non-UnboundID products.
 * </BLOCKQUOTE>
 */
@NotExtensible()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public abstract class AccessLogMessage
       extends LogMessage
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 111497572975341652L;



  // The connection ID for this access log message.
  private final Long connectionID;

  // The Directory Server instance name for this access log message.
  private final String instanceName;

  // The server product name for this access log message.
  private final String productName;

  // The startup ID for this access log message;
  private final String startupID;



  /**
   * Creates a new access log message from the provided log message.
   *
   * @param  m  The log message to be parsed as an access log message.
   */
  protected AccessLogMessage(final LogMessage m)
  {
    super(m);

    productName  = getNamedValue("product");
    instanceName = getNamedValue("instanceName");
    startupID    = getNamedValue("startupID");
    connectionID = getNamedValueAsLong("conn");
  }



  /**
   * Parses the provided string as an access log message.
   *
   * @param  s  The string to parse as an access log message.
   *
   * @return  The parsed access log message.
   *
   * @throws  LogException  If an error occurs while trying to parse the log
   *                        message.
   */
  public static AccessLogMessage parse(final String s)
         throws LogException
  {
    return AccessLogReader.parse(s);
  }




  /**
   * Retrieves the server product name for this access log message.
   *
   * @return  The server product name for this access log message, or
   *          {@code null} if it is not included in the log message.
   */
  public final String getProductName()
  {
    return productName;
  }



  /**
   * Retrieves the Directory Server instance name for this access log message.
   *
   * @return  The Directory Server instance name for this access log message, or
   *          {@code null} if it is not included in the log message.
   */
  public final String getInstanceName()
  {
    return instanceName;
  }



  /**
   * Retrieves the Directory Server startup ID for this access log message.
   *
   * @return  The Directory Server startup ID for this access log message, or
   *          {@code null} if it is not included in the log message.
   */
  public final String getStartupID()
  {
    return startupID;
  }



  /**
   * Retrieves the connection ID for the connection with which this access log
   * message is associated.
   *
   * @return  The connection ID for the connection with which this access log
   *          message is associated, or {@code null} if it is not included in
   *          the log message.
   */
  public final Long getConnectionID()
  {
    return connectionID;
  }



  /**
   * Retrieves the message type for this access log message.
   *
   * @return  The message type for this access log message.
   */
  public abstract AccessLogMessageType getMessageType();
}
