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

package com.liferay.portlet.documentlibrary.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.StagedGroupedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the DLFileEntryType service. Represents a row in the &quot;DLFileEntryType&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.documentlibrary.model.impl.DLFileEntryTypeModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.documentlibrary.model.impl.DLFileEntryTypeImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLFileEntryType
 * @see com.liferay.portlet.documentlibrary.model.impl.DLFileEntryTypeImpl
 * @see com.liferay.portlet.documentlibrary.model.impl.DLFileEntryTypeModelImpl
 * @generated
 */
public interface DLFileEntryTypeModel extends BaseModel<DLFileEntryType>,
	StagedGroupedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a document library file entry type model instance should use the {@link DLFileEntryType} interface instead.
	 */

	/**
	 * Returns the primary key of this document library file entry type.
	 *
	 * @return the primary key of this document library file entry type
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this document library file entry type.
	 *
	 * @param primaryKey the primary key of this document library file entry type
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this document library file entry type.
	 *
	 * @return the uuid of this document library file entry type
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this document library file entry type.
	 *
	 * @param uuid the uuid of this document library file entry type
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the file entry type ID of this document library file entry type.
	 *
	 * @return the file entry type ID of this document library file entry type
	 */
	public long getFileEntryTypeId();

	/**
	 * Sets the file entry type ID of this document library file entry type.
	 *
	 * @param fileEntryTypeId the file entry type ID of this document library file entry type
	 */
	public void setFileEntryTypeId(long fileEntryTypeId);

	/**
	 * Returns the group ID of this document library file entry type.
	 *
	 * @return the group ID of this document library file entry type
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this document library file entry type.
	 *
	 * @param groupId the group ID of this document library file entry type
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this document library file entry type.
	 *
	 * @return the company ID of this document library file entry type
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this document library file entry type.
	 *
	 * @param companyId the company ID of this document library file entry type
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this document library file entry type.
	 *
	 * @return the user ID of this document library file entry type
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this document library file entry type.
	 *
	 * @param userId the user ID of this document library file entry type
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this document library file entry type.
	 *
	 * @return the user uuid of this document library file entry type
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this document library file entry type.
	 *
	 * @param userUuid the user uuid of this document library file entry type
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this document library file entry type.
	 *
	 * @return the user name of this document library file entry type
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this document library file entry type.
	 *
	 * @param userName the user name of this document library file entry type
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this document library file entry type.
	 *
	 * @return the create date of this document library file entry type
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this document library file entry type.
	 *
	 * @param createDate the create date of this document library file entry type
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this document library file entry type.
	 *
	 * @return the modified date of this document library file entry type
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this document library file entry type.
	 *
	 * @param modifiedDate the modified date of this document library file entry type
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the name of this document library file entry type.
	 *
	 * @return the name of this document library file entry type
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this document library file entry type.
	 *
	 * @param name the name of this document library file entry type
	 */
	public void setName(String name);

	/**
	 * Returns the description of this document library file entry type.
	 *
	 * @return the description of this document library file entry type
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this document library file entry type.
	 *
	 * @param description the description of this document library file entry type
	 */
	public void setDescription(String description);

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
	public int compareTo(DLFileEntryType dlFileEntryType);

	@Override
	public int hashCode();

	@Override
	public CacheModel<DLFileEntryType> toCacheModel();

	@Override
	public DLFileEntryType toEscapedModel();

	@Override
	public DLFileEntryType toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}