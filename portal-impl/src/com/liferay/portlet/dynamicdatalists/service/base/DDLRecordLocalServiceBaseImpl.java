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

package com.liferay.portlet.dynamicdatalists.service.base;

import com.liferay.counter.service.CounterLocalService;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.WorkflowInstanceLinkLocalService;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.WorkflowInstanceLinkPersistence;

import com.liferay.portlet.asset.service.AssetEntryLocalService;
import com.liferay.portlet.asset.service.AssetEntryService;
import com.liferay.portlet.asset.service.persistence.AssetEntryFinder;
import com.liferay.portlet.asset.service.persistence.AssetEntryPersistence;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordLocalService;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordService;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalService;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetService;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordFinder;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordPersistence;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordSetFinder;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordSetPersistence;
import com.liferay.portlet.dynamicdatalists.service.persistence.DDLRecordVersionPersistence;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalService;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureService;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructureFinder;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMStructurePersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the d d l record local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portlet.dynamicdatalists.service.impl.DDLRecordLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.dynamicdatalists.service.impl.DDLRecordLocalServiceImpl
 * @see com.liferay.portlet.dynamicdatalists.service.DDLRecordLocalServiceUtil
 * @generated
 */
public abstract class DDLRecordLocalServiceBaseImpl extends BaseLocalServiceImpl
	implements DDLRecordLocalService, IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portlet.dynamicdatalists.service.DDLRecordLocalServiceUtil} to access the d d l record local service.
	 */

	/**
	 * Adds the d d l record to the database. Also notifies the appropriate model listeners.
	 *
	 * @param ddlRecord the d d l record
	 * @return the d d l record that was added
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public DDLRecord addDDLRecord(DDLRecord ddlRecord)
		throws SystemException {
		ddlRecord.setNew(true);

		return ddlRecordPersistence.update(ddlRecord);
	}

	/**
	 * Creates a new d d l record with the primary key. Does not add the d d l record to the database.
	 *
	 * @param recordId the primary key for the new d d l record
	 * @return the new d d l record
	 */
	@Override
	public DDLRecord createDDLRecord(long recordId) {
		return ddlRecordPersistence.create(recordId);
	}

	/**
	 * Deletes the d d l record with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param recordId the primary key of the d d l record
	 * @return the d d l record that was removed
	 * @throws PortalException if a d d l record with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public DDLRecord deleteDDLRecord(long recordId)
		throws PortalException, SystemException {
		return ddlRecordPersistence.remove(recordId);
	}

	/**
	 * Deletes the d d l record from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ddlRecord the d d l record
	 * @return the d d l record that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public DDLRecord deleteDDLRecord(DDLRecord ddlRecord)
		throws SystemException {
		return ddlRecordPersistence.remove(ddlRecord);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(DDLRecord.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return ddlRecordPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.dynamicdatalists.model.impl.DDLRecordModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return ddlRecordPersistence.findWithDynamicQuery(dynamicQuery, start,
			end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.dynamicdatalists.model.impl.DDLRecordModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return ddlRecordPersistence.findWithDynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery)
		throws SystemException {
		return ddlRecordPersistence.countWithDynamicQuery(dynamicQuery);
	}

	@Override
	public DDLRecord fetchDDLRecord(long recordId) throws SystemException {
		return ddlRecordPersistence.fetchByPrimaryKey(recordId);
	}

	/**
	 * Returns the d d l record with the primary key.
	 *
	 * @param recordId the primary key of the d d l record
	 * @return the d d l record
	 * @throws PortalException if a d d l record with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DDLRecord getDDLRecord(long recordId)
		throws PortalException, SystemException {
		return ddlRecordPersistence.findByPrimaryKey(recordId);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException, SystemException {
		return ddlRecordPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns the d d l record matching the UUID and group.
	 *
	 * @param uuid the d d l record's UUID
	 * @param groupId the primary key of the group
	 * @return the matching d d l record
	 * @throws PortalException if a matching d d l record could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public DDLRecord getDDLRecordByUuidAndGroupId(String uuid, long groupId)
		throws PortalException, SystemException {
		return ddlRecordPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the d d l records.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.dynamicdatalists.model.impl.DDLRecordModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of d d l records
	 * @param end the upper bound of the range of d d l records (not inclusive)
	 * @return the range of d d l records
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<DDLRecord> getDDLRecords(int start, int end)
		throws SystemException {
		return ddlRecordPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of d d l records.
	 *
	 * @return the number of d d l records
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int getDDLRecordsCount() throws SystemException {
		return ddlRecordPersistence.countAll();
	}

	/**
	 * Updates the d d l record in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param ddlRecord the d d l record
	 * @return the d d l record that was updated
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public DDLRecord updateDDLRecord(DDLRecord ddlRecord)
		throws SystemException {
		return ddlRecordPersistence.update(ddlRecord);
	}

	/**
	 * Returns the d d l record local service.
	 *
	 * @return the d d l record local service
	 */
	public DDLRecordLocalService getDDLRecordLocalService() {
		return ddlRecordLocalService;
	}

	/**
	 * Sets the d d l record local service.
	 *
	 * @param ddlRecordLocalService the d d l record local service
	 */
	public void setDDLRecordLocalService(
		DDLRecordLocalService ddlRecordLocalService) {
		this.ddlRecordLocalService = ddlRecordLocalService;
	}

	/**
	 * Returns the d d l record remote service.
	 *
	 * @return the d d l record remote service
	 */
	public DDLRecordService getDDLRecordService() {
		return ddlRecordService;
	}

	/**
	 * Sets the d d l record remote service.
	 *
	 * @param ddlRecordService the d d l record remote service
	 */
	public void setDDLRecordService(DDLRecordService ddlRecordService) {
		this.ddlRecordService = ddlRecordService;
	}

	/**
	 * Returns the d d l record persistence.
	 *
	 * @return the d d l record persistence
	 */
	public DDLRecordPersistence getDDLRecordPersistence() {
		return ddlRecordPersistence;
	}

	/**
	 * Sets the d d l record persistence.
	 *
	 * @param ddlRecordPersistence the d d l record persistence
	 */
	public void setDDLRecordPersistence(
		DDLRecordPersistence ddlRecordPersistence) {
		this.ddlRecordPersistence = ddlRecordPersistence;
	}

	/**
	 * Returns the d d l record finder.
	 *
	 * @return the d d l record finder
	 */
	public DDLRecordFinder getDDLRecordFinder() {
		return ddlRecordFinder;
	}

	/**
	 * Sets the d d l record finder.
	 *
	 * @param ddlRecordFinder the d d l record finder
	 */
	public void setDDLRecordFinder(DDLRecordFinder ddlRecordFinder) {
		this.ddlRecordFinder = ddlRecordFinder;
	}

	/**
	 * Returns the d d l record set local service.
	 *
	 * @return the d d l record set local service
	 */
	public DDLRecordSetLocalService getDDLRecordSetLocalService() {
		return ddlRecordSetLocalService;
	}

	/**
	 * Sets the d d l record set local service.
	 *
	 * @param ddlRecordSetLocalService the d d l record set local service
	 */
	public void setDDLRecordSetLocalService(
		DDLRecordSetLocalService ddlRecordSetLocalService) {
		this.ddlRecordSetLocalService = ddlRecordSetLocalService;
	}

	/**
	 * Returns the d d l record set remote service.
	 *
	 * @return the d d l record set remote service
	 */
	public DDLRecordSetService getDDLRecordSetService() {
		return ddlRecordSetService;
	}

	/**
	 * Sets the d d l record set remote service.
	 *
	 * @param ddlRecordSetService the d d l record set remote service
	 */
	public void setDDLRecordSetService(DDLRecordSetService ddlRecordSetService) {
		this.ddlRecordSetService = ddlRecordSetService;
	}

	/**
	 * Returns the d d l record set persistence.
	 *
	 * @return the d d l record set persistence
	 */
	public DDLRecordSetPersistence getDDLRecordSetPersistence() {
		return ddlRecordSetPersistence;
	}

	/**
	 * Sets the d d l record set persistence.
	 *
	 * @param ddlRecordSetPersistence the d d l record set persistence
	 */
	public void setDDLRecordSetPersistence(
		DDLRecordSetPersistence ddlRecordSetPersistence) {
		this.ddlRecordSetPersistence = ddlRecordSetPersistence;
	}

	/**
	 * Returns the d d l record set finder.
	 *
	 * @return the d d l record set finder
	 */
	public DDLRecordSetFinder getDDLRecordSetFinder() {
		return ddlRecordSetFinder;
	}

	/**
	 * Sets the d d l record set finder.
	 *
	 * @param ddlRecordSetFinder the d d l record set finder
	 */
	public void setDDLRecordSetFinder(DDLRecordSetFinder ddlRecordSetFinder) {
		this.ddlRecordSetFinder = ddlRecordSetFinder;
	}

	/**
	 * Returns the d d l record version persistence.
	 *
	 * @return the d d l record version persistence
	 */
	public DDLRecordVersionPersistence getDDLRecordVersionPersistence() {
		return ddlRecordVersionPersistence;
	}

	/**
	 * Sets the d d l record version persistence.
	 *
	 * @param ddlRecordVersionPersistence the d d l record version persistence
	 */
	public void setDDLRecordVersionPersistence(
		DDLRecordVersionPersistence ddlRecordVersionPersistence) {
		this.ddlRecordVersionPersistence = ddlRecordVersionPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the user finder.
	 *
	 * @return the user finder
	 */
	public UserFinder getUserFinder() {
		return userFinder;
	}

	/**
	 * Sets the user finder.
	 *
	 * @param userFinder the user finder
	 */
	public void setUserFinder(UserFinder userFinder) {
		this.userFinder = userFinder;
	}

	/**
	 * Returns the workflow instance link local service.
	 *
	 * @return the workflow instance link local service
	 */
	public WorkflowInstanceLinkLocalService getWorkflowInstanceLinkLocalService() {
		return workflowInstanceLinkLocalService;
	}

	/**
	 * Sets the workflow instance link local service.
	 *
	 * @param workflowInstanceLinkLocalService the workflow instance link local service
	 */
	public void setWorkflowInstanceLinkLocalService(
		WorkflowInstanceLinkLocalService workflowInstanceLinkLocalService) {
		this.workflowInstanceLinkLocalService = workflowInstanceLinkLocalService;
	}

	/**
	 * Returns the workflow instance link persistence.
	 *
	 * @return the workflow instance link persistence
	 */
	public WorkflowInstanceLinkPersistence getWorkflowInstanceLinkPersistence() {
		return workflowInstanceLinkPersistence;
	}

	/**
	 * Sets the workflow instance link persistence.
	 *
	 * @param workflowInstanceLinkPersistence the workflow instance link persistence
	 */
	public void setWorkflowInstanceLinkPersistence(
		WorkflowInstanceLinkPersistence workflowInstanceLinkPersistence) {
		this.workflowInstanceLinkPersistence = workflowInstanceLinkPersistence;
	}

	/**
	 * Returns the asset entry local service.
	 *
	 * @return the asset entry local service
	 */
	public AssetEntryLocalService getAssetEntryLocalService() {
		return assetEntryLocalService;
	}

	/**
	 * Sets the asset entry local service.
	 *
	 * @param assetEntryLocalService the asset entry local service
	 */
	public void setAssetEntryLocalService(
		AssetEntryLocalService assetEntryLocalService) {
		this.assetEntryLocalService = assetEntryLocalService;
	}

	/**
	 * Returns the asset entry remote service.
	 *
	 * @return the asset entry remote service
	 */
	public AssetEntryService getAssetEntryService() {
		return assetEntryService;
	}

	/**
	 * Sets the asset entry remote service.
	 *
	 * @param assetEntryService the asset entry remote service
	 */
	public void setAssetEntryService(AssetEntryService assetEntryService) {
		this.assetEntryService = assetEntryService;
	}

	/**
	 * Returns the asset entry persistence.
	 *
	 * @return the asset entry persistence
	 */
	public AssetEntryPersistence getAssetEntryPersistence() {
		return assetEntryPersistence;
	}

	/**
	 * Sets the asset entry persistence.
	 *
	 * @param assetEntryPersistence the asset entry persistence
	 */
	public void setAssetEntryPersistence(
		AssetEntryPersistence assetEntryPersistence) {
		this.assetEntryPersistence = assetEntryPersistence;
	}

	/**
	 * Returns the asset entry finder.
	 *
	 * @return the asset entry finder
	 */
	public AssetEntryFinder getAssetEntryFinder() {
		return assetEntryFinder;
	}

	/**
	 * Sets the asset entry finder.
	 *
	 * @param assetEntryFinder the asset entry finder
	 */
	public void setAssetEntryFinder(AssetEntryFinder assetEntryFinder) {
		this.assetEntryFinder = assetEntryFinder;
	}

	/**
	 * Returns the d d m structure local service.
	 *
	 * @return the d d m structure local service
	 */
	public DDMStructureLocalService getDDMStructureLocalService() {
		return ddmStructureLocalService;
	}

	/**
	 * Sets the d d m structure local service.
	 *
	 * @param ddmStructureLocalService the d d m structure local service
	 */
	public void setDDMStructureLocalService(
		DDMStructureLocalService ddmStructureLocalService) {
		this.ddmStructureLocalService = ddmStructureLocalService;
	}

	/**
	 * Returns the d d m structure remote service.
	 *
	 * @return the d d m structure remote service
	 */
	public DDMStructureService getDDMStructureService() {
		return ddmStructureService;
	}

	/**
	 * Sets the d d m structure remote service.
	 *
	 * @param ddmStructureService the d d m structure remote service
	 */
	public void setDDMStructureService(DDMStructureService ddmStructureService) {
		this.ddmStructureService = ddmStructureService;
	}

	/**
	 * Returns the d d m structure persistence.
	 *
	 * @return the d d m structure persistence
	 */
	public DDMStructurePersistence getDDMStructurePersistence() {
		return ddmStructurePersistence;
	}

	/**
	 * Sets the d d m structure persistence.
	 *
	 * @param ddmStructurePersistence the d d m structure persistence
	 */
	public void setDDMStructurePersistence(
		DDMStructurePersistence ddmStructurePersistence) {
		this.ddmStructurePersistence = ddmStructurePersistence;
	}

	/**
	 * Returns the d d m structure finder.
	 *
	 * @return the d d m structure finder
	 */
	public DDMStructureFinder getDDMStructureFinder() {
		return ddmStructureFinder;
	}

	/**
	 * Sets the d d m structure finder.
	 *
	 * @param ddmStructureFinder the d d m structure finder
	 */
	public void setDDMStructureFinder(DDMStructureFinder ddmStructureFinder) {
		this.ddmStructureFinder = ddmStructureFinder;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.liferay.portlet.dynamicdatalists.model.DDLRecord",
			ddlRecordLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.portlet.dynamicdatalists.model.DDLRecord");
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	protected Class<?> getModelClass() {
		return DDLRecord.class;
	}

	protected String getModelClassName() {
		return DDLRecord.class.getName();
	}

	/**
	 * Performs an SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = ddlRecordPersistence.getDataSource();

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = DDLRecordLocalService.class)
	protected DDLRecordLocalService ddlRecordLocalService;
	@BeanReference(type = DDLRecordService.class)
	protected DDLRecordService ddlRecordService;
	@BeanReference(type = DDLRecordPersistence.class)
	protected DDLRecordPersistence ddlRecordPersistence;
	@BeanReference(type = DDLRecordFinder.class)
	protected DDLRecordFinder ddlRecordFinder;
	@BeanReference(type = DDLRecordSetLocalService.class)
	protected DDLRecordSetLocalService ddlRecordSetLocalService;
	@BeanReference(type = DDLRecordSetService.class)
	protected DDLRecordSetService ddlRecordSetService;
	@BeanReference(type = DDLRecordSetPersistence.class)
	protected DDLRecordSetPersistence ddlRecordSetPersistence;
	@BeanReference(type = DDLRecordSetFinder.class)
	protected DDLRecordSetFinder ddlRecordSetFinder;
	@BeanReference(type = DDLRecordVersionPersistence.class)
	protected DDLRecordVersionPersistence ddlRecordVersionPersistence;
	@BeanReference(type = CounterLocalService.class)
	protected CounterLocalService counterLocalService;
	@BeanReference(type = ResourceLocalService.class)
	protected ResourceLocalService resourceLocalService;
	@BeanReference(type = UserLocalService.class)
	protected UserLocalService userLocalService;
	@BeanReference(type = UserService.class)
	protected UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = UserFinder.class)
	protected UserFinder userFinder;
	@BeanReference(type = WorkflowInstanceLinkLocalService.class)
	protected WorkflowInstanceLinkLocalService workflowInstanceLinkLocalService;
	@BeanReference(type = WorkflowInstanceLinkPersistence.class)
	protected WorkflowInstanceLinkPersistence workflowInstanceLinkPersistence;
	@BeanReference(type = AssetEntryLocalService.class)
	protected AssetEntryLocalService assetEntryLocalService;
	@BeanReference(type = AssetEntryService.class)
	protected AssetEntryService assetEntryService;
	@BeanReference(type = AssetEntryPersistence.class)
	protected AssetEntryPersistence assetEntryPersistence;
	@BeanReference(type = AssetEntryFinder.class)
	protected AssetEntryFinder assetEntryFinder;
	@BeanReference(type = DDMStructureLocalService.class)
	protected DDMStructureLocalService ddmStructureLocalService;
	@BeanReference(type = DDMStructureService.class)
	protected DDMStructureService ddmStructureService;
	@BeanReference(type = DDMStructurePersistence.class)
	protected DDMStructurePersistence ddmStructurePersistence;
	@BeanReference(type = DDMStructureFinder.class)
	protected DDMStructureFinder ddmStructureFinder;
	@BeanReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
	private String _beanIdentifier;
}