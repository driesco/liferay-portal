/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the UserNotificationEvent service. Represents a row in the &quot;UserNotificationEvent&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portal.model.impl.UserNotificationEventModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portal.model.impl.UserNotificationEventImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see UserNotificationEvent
 * @see com.liferay.portal.model.impl.UserNotificationEventImpl
 * @see com.liferay.portal.model.impl.UserNotificationEventModelImpl
 * @generated
 */
public interface UserNotificationEventModel extends BaseModel<UserNotificationEvent> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a user notification event model instance should use the {@link UserNotificationEvent} interface instead.
	 */

	/**
	 * Returns the primary key of this user notification event.
	 *
	 * @return the primary key of this user notification event
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this user notification event.
	 *
	 * @param primaryKey the primary key of this user notification event
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this user notification event.
	 *
	 * @return the uuid of this user notification event
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this user notification event.
	 *
	 * @param uuid the uuid of this user notification event
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the user notification event ID of this user notification event.
	 *
	 * @return the user notification event ID of this user notification event
	 */
	public long getUserNotificationEventId();

	/**
	 * Sets the user notification event ID of this user notification event.
	 *
	 * @param userNotificationEventId the user notification event ID of this user notification event
	 */
	public void setUserNotificationEventId(long userNotificationEventId);

	/**
	 * Returns the company ID of this user notification event.
	 *
	 * @return the company ID of this user notification event
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this user notification event.
	 *
	 * @param companyId the company ID of this user notification event
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this user notification event.
	 *
	 * @return the user ID of this user notification event
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this user notification event.
	 *
	 * @param userId the user ID of this user notification event
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this user notification event.
	 *
	 * @return the user uuid of this user notification event
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this user notification event.
	 *
	 * @param userUuid the user uuid of this user notification event
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the type of this user notification event.
	 *
	 * @return the type of this user notification event
	 */
	@AutoEscape
	public String getType();

	/**
	 * Sets the type of this user notification event.
	 *
	 * @param type the type of this user notification event
	 */
	public void setType(String type);

	/**
	 * Returns the timestamp of this user notification event.
	 *
	 * @return the timestamp of this user notification event
	 */
	public long getTimestamp();

	/**
	 * Sets the timestamp of this user notification event.
	 *
	 * @param timestamp the timestamp of this user notification event
	 */
	public void setTimestamp(long timestamp);

	/**
	 * Returns the deliver by of this user notification event.
	 *
	 * @return the deliver by of this user notification event
	 */
	public long getDeliverBy();

	/**
	 * Sets the deliver by of this user notification event.
	 *
	 * @param deliverBy the deliver by of this user notification event
	 */
	public void setDeliverBy(long deliverBy);

	/**
	 * Returns the payload of this user notification event.
	 *
	 * @return the payload of this user notification event
	 */
	@AutoEscape
	public String getPayload();

	/**
	 * Sets the payload of this user notification event.
	 *
	 * @param payload the payload of this user notification event
	 */
	public void setPayload(String payload);

	/**
	 * Returns the archived of this user notification event.
	 *
	 * @return the archived of this user notification event
	 */
	public boolean getArchived();

	/**
	 * Returns <code>true</code> if this user notification event is archived.
	 *
	 * @return <code>true</code> if this user notification event is archived; <code>false</code> otherwise
	 */
	public boolean isArchived();

	/**
	 * Sets whether this user notification event is archived.
	 *
	 * @param archived the archived of this user notification event
	 */
	public void setArchived(boolean archived);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(UserNotificationEvent userNotificationEvent);

	@Override
	public int hashCode();

	@Override
	public CacheModel<UserNotificationEvent> toCacheModel();

	@Override
	public UserNotificationEvent toEscapedModel();

	@Override
	public UserNotificationEvent toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}