package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.lar.UserIdStrategy;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.PrimitiveLongList;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcedModel;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.Team;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockPermissionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.TeamLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author Daniela Zapata Riesco
 */
public class PortalDataContextImpl implements PortalDataContext {

	public PortalDataContextImpl(
			long companyId,  ZipWriter zipWriter)
		throws PortletDataException {

		_companyId = companyId;
		_zipReader = null;
		_zipWriter = zipWriter;

		initXStream();
	}

	public PortalDataContextImpl(
		long companyId,  ZipReader zipReader)
		throws PortletDataException {

		_companyId = companyId;
		_zipReader = zipReader;
		_zipWriter = null;

		initXStream();
	}

	public void addClassedModel(
			Element element, String path, ClassedModel classedModel,
			String namespace)
		throws PortalException, SystemException {

		String dataPath = path + ".xml";
		String permissionPath = path + "-permissions.xml";

		element.addAttribute("data-path", dataPath);
		element.addAttribute("permission-path", permissionPath);

		addZipEntry(dataPath, classedModel);
	}

	public void addPermissions(String resourceName, long resourcePK)
		throws PortalException, SystemException {

		List<KeyValuePair> permissions = new ArrayList<KeyValuePair>();

		List<Role> roles = RoleLocalServiceUtil.getRoles(_companyId);

		PrimitiveLongList roleIds = new PrimitiveLongList(roles.size());
		Map<Long, String> roleIdsToNames = new HashMap<Long, String>();

		for (Role role : roles) {
			int type = role.getType();

			if ((type == RoleConstants.TYPE_REGULAR) ||
				((type == RoleConstants.TYPE_ORGANIZATION) &&
					(type == RoleConstants.TYPE_SITE))) {

				String name = role.getName();

				roleIds.add(role.getRoleId());
				roleIdsToNames.put(role.getRoleId(), name);
			}
			else if ((type == RoleConstants.TYPE_PROVIDER) && role.isTeam()) {
				Team team = TeamLocalServiceUtil.getTeam(role.getClassPK());

				String name =
					"ROLE_TEAM_,*" + team.getName();

				roleIds.add(role.getRoleId());
				roleIdsToNames.put(role.getRoleId(), name);
			}
		}

		List<String> actionIds = ResourceActionsUtil.getModelResourceActions(
			resourceName);

		Map<Long, Set<String>> roleIdsToActionIds = getActionIds(
			_companyId, roleIds.getArray(), resourceName, resourcePK,
			actionIds);

		for (Map.Entry<Long, String> entry : roleIdsToNames.entrySet()) {
			long roleId = entry.getKey();
			String name = entry.getValue();

			Set<String> availableActionIds = roleIdsToActionIds.get(roleId);

			if (availableActionIds == null) {
				availableActionIds = Collections.emptySet();
			}

			KeyValuePair permission = new KeyValuePair(
				name, StringUtil.merge(availableActionIds));

			permissions.add(permission);
		}

		_permissionsMap.put(
			getPrimaryKeyString(resourceName, resourcePK), permissions);
	}

	public void addPermissions(
		String resourceName, long resourcePK, List<KeyValuePair> permissions) {

		_permissionsMap.put(
			getPrimaryKeyString(resourceName, resourcePK), permissions);
	}

	public void addZipEntry(String path, Object object) throws SystemException {
		addZipEntry(path, toXML(object));
	}

	public void addZipEntry(String path, String s) throws SystemException {

		try {
			ZipWriter zipWriter = getZipWriter();

			zipWriter.addEntry(path, s);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	public Object fromXML(String xml) {
		if (Validator.isNull(xml)) {
			return null;
		}

		return _xStream.fromXML(xml);
	}

	public long getCompanyId() {
		return _companyId;
	}

	public Map<String, List<KeyValuePair>> getPermissions() {
		return _permissionsMap;
	}

	public Object getZipEntryAsObject(String path) {
		return fromXML(getZipEntryAsString(path));
	}

	public String getZipEntryAsString(String path) {
		if (!Validator.isFilePath(path, false)) {
			return null;
		}
		return getZipReader().getEntryAsString(path);
	}

	public ZipReader getZipReader() {
		return  _zipReader;
	}

	public ZipWriter getZipWriter() {
		return _zipWriter;
	}

	public void importClassedModel(
			ClassedModel classedModel, ClassedModel newClassedModel,
			String namespace)
		throws  PortalException, SystemException {

		Class<?> clazz = classedModel.getModelClass();
		long classPK = getClassPK(classedModel);

		long newClassPK = getClassPK(newClassedModel);

		Map<Long, Long> newPrimaryKeysMap =
			(Map<Long, Long>)getNewPrimaryKeysMap(clazz);

		newPrimaryKeysMap.put(classPK, newClassPK);

		importPermissions(clazz, classPK, newClassPK);
	}

	public Map<?, ?> getNewPrimaryKeysMap(Class<?> clazz) {
		return getNewPrimaryKeysMap(clazz.getName());
	}

	public Map<?, ?> getNewPrimaryKeysMap(String className) {
		Map<?, ?> map = _newPrimaryKeysMaps.get(className);

		if (map == null) {
			map = new HashMap<Object, Object>();

			_newPrimaryKeysMaps.put(className, map);
		}

		return map;
	}

	public void importPermissions(Class<?> clazz, long classPK, long newClassPK)
		throws PortalException, SystemException {

		importPermissions(clazz.getName(), classPK, newClassPK);
	}

	public void importPermissions(
			String resourceName, long resourcePK, long newResourcePK)
		throws PortalException, SystemException {

		List<KeyValuePair> permissions = _permissionsMap.get(
			getPrimaryKeyString(resourceName, resourcePK));

		if (permissions == null) {
			return;
		}

		Map<Long, String[]> roleIdsToActionIds = new HashMap<Long, String[]>();

		for (KeyValuePair permission : permissions) {
			String roleName = permission.getKey();

			Role role = null;

			Team team = null;

			try {
				role = RoleLocalServiceUtil.getRole(_companyId, roleName);
			}
			catch (NoSuchRoleException nsre) {
				if (_log.isWarnEnabled()) {
					_log.warn("Role " + roleName + " does not exist");
				}

				continue;
			}

			String[] actionIds = StringUtil.split(permission.getValue());

			roleIdsToActionIds.put(role.getRoleId(), actionIds);
		}

		if (roleIdsToActionIds.isEmpty()) {
			return;
		}

			ResourcePermissionLocalServiceUtil.setResourcePermissions(
				_companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(newResourcePK), roleIdsToActionIds);
	}

	public String toXML(Object object) {
		return _xStream.toXML(object);
	}

	protected Map<Long, Set<String>> getActionIds(
		long companyId, long[] roleIds, String className, long primKey,
		List<String> actionIds)
		throws PortalException, SystemException {

		if (ResourceBlockLocalServiceUtil.isSupported(className)) {
			return ResourceBlockPermissionLocalServiceUtil.
				getAvailableResourceBlockPermissionActionIds(
					roleIds, className, primKey, actionIds);
		}
		else {
			return ResourcePermissionLocalServiceUtil.
				getAvailableResourcePermissionActionIds(
					companyId, className, ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(primKey), roleIds, actionIds);
		}
	}

	protected long getClassPK(ClassedModel classedModel) {
		if (classedModel instanceof ResourcedModel) {
			ResourcedModel resourcedModel = (ResourcedModel)classedModel;

			return resourcedModel.getResourcePrimKey();
		}
		else {
			return (Long)classedModel.getPrimaryKeyObj();
		}
	}

	protected String getPrimaryKeyString(String className, long classPK) {
		return getPrimaryKeyString(className, String.valueOf(classPK));
	}

	protected String getPrimaryKeyString(String className, String primaryKey) {
		return className.concat(StringPool.POUND).concat(primaryKey);
	}

	protected void initXStream() {
		_xStream = new XStream();
	}

	private static Log _log = LogFactoryUtil.getLog(
		(PortalDataContextImpl.class));

	private long _companyId;
	private String _dataStrategy;
	private Date _endDate;
	private Map<String, List<ExpandoColumn>> _expandoColumnsMap =
		new HashMap<String, List<ExpandoColumn>>();
	private Map<String, Map<?, ?>> _newPrimaryKeysMaps =
		new HashMap<String, Map<?, ?>>();
	private Map<String, String[]> _parameterMap;
	private Map<String, List<KeyValuePair>> _permissionsMap =
		new HashMap<String, List<KeyValuePair>>();
	private Set<String> _primaryKeys;
	private String _scopeType;
	private Date _startDate;
	private UserIdStrategy _userIdStrategy;
	private XStream _xStream;
	private ZipReader _zipReader;
	private ZipWriter _zipWriter;
}
