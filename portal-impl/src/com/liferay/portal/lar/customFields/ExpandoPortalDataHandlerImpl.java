package com.liferay.portal.lar.customFields;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.portalDataContext.BasePortalDataHandler;
import com.liferay.portal.lar.portalDataContext.PortalDataContext;
import com.liferay.portal.lar.portalDataContext.PortalPermissionImporter;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.util.ExpandoConverterUtil;
import com.liferay.util.xml.DocUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniela Zapata Riesco
 */
public class ExpandoPortalDataHandlerImpl
	extends BasePortalDataHandler {

	@Override
	protected String doExportData(
			PortalDataContext portalDataContext)
		throws Exception {

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("expando-tables");

		List<ExpandoTable> expandoTables =
			ExpandoTableLocalServiceUtil.getExpandoTables(
				0, ExpandoTableLocalServiceUtil.getExpandoTablesCount());

		for (ExpandoTable expandoTable : expandoTables) {
			String className = expandoTable.getClassName();
			addExpandoColumnsMap(className, portalDataContext.getCompanyId());
		}

		exportExpandoTables(rootElement, portalDataContext);

		exportPermissions(portalDataContext);

		return document.formattedString();
	}

	@Override
	protected void doImportData(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		for (Element expandoTableElement :
			rootElement.elements("expando-table")) {

			String path = expandoTableElement.attributeValue("path");
			String tableId = expandoTableElement.attributeValue("id");

			String expandoTable =
				portalDataContext.getZipEntryAsString(
					path + StringPool.FORWARD_SLASH + tableId + "-expando.xml");

			importExpandoTable(portalDataContext, expandoTable);
		}
	}

	@Override
	protected void doReadDataPermissions(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		List<String> permissionPaths = new ArrayList<String>();

		for (Element expandoTableElement :
			rootElement.elements("expando-table")) {

			String path = expandoTableElement.attributeValue("path");
			String tableId = expandoTableElement.attributeValue("id");

			String permissionPath = path + "/" + tableId + "-permissions.xml";

			permissionPaths.add(permissionPath);
		}

		_portalPermissionImporter.readPortalDataPermissions(
			portalDataContext, permissionPaths);
	}

	protected void exportExpandoTables(
			Element expandoTablesElement, PortalDataContext portalDataContext)
		throws Exception {

		for (Map.Entry<String, List<ExpandoColumn>> entry :
				_expandoColumnsMap.entrySet()) {

			String className = entry.getKey();
			List<ExpandoColumn> expandoColumns = entry.getValue();
			long tableId = expandoColumns.get(0).getTableId();

			Element expandoTableElementData = expandoTablesElement.addElement(
				"expando-table");

			String path = _ROOT_PATH + getPath(className);

			expandoTableElementData.addAttribute("id", String.valueOf(tableId));
			expandoTableElementData.addAttribute("path", path);

			Document document = SAXReaderUtil.createDocument();

			Element rootElement = document.addElement("expando-tables");

			Element expandoTableElement = rootElement.addElement(
				"expando-table");

			expandoTableElement.addAttribute("class-name", className);

			if (expandoColumns.size() != 0 ) {
				for (ExpandoColumn expandoColumn : expandoColumns) {

					Element expandoColumnElement =
						expandoTableElement.addElement("expando-column");

					expandoColumnElement.addAttribute(
						"column-id", String.valueOf(
						expandoColumn.getColumnId()));

					expandoColumnElement.addAttribute(
						"name", expandoColumn.getName());

					expandoColumnElement.addAttribute(
						"type", String.valueOf(expandoColumn.getType()));

					DocUtil.add(
						expandoColumnElement, "default-data",
						expandoColumn.getDefaultData());

					Element typeSettingsElement =
						expandoColumnElement.addElement("type-settings");

					UnicodeProperties typeSettingsProperties =
						expandoColumn.getTypeSettingsProperties();

					typeSettingsElement.addCDATA(
						typeSettingsProperties.toString());
				}
			}
			else {
				expandoTableElement.addElement("expando-column");
			}

			portalDataContext.addZipEntry(
				path + StringPool.FORWARD_SLASH + tableId + "-expando.xml",
				document.formattedString());
		}
	}

	protected void exportPermissions(PortalDataContext portalDataContext)
		throws Exception {

		for (Map.Entry<String, List<ExpandoColumn>> expandoTableEntry :
			_expandoColumnsMap.entrySet()) {

			List<ExpandoColumn> expandoColumns = expandoTableEntry.getValue();

			long tableId = expandoColumns.get(0).getTableId();

			if (expandoColumns.size() != 0 ) {
				for (ExpandoColumn expandoColumn : expandoColumns) {
					portalDataContext.addPermissions(
						expandoColumn.getModelClassName(),
						expandoColumn.getColumnId());
				}
			}

			Document document = doExportPermissions(portalDataContext);

			portalDataContext.getPermissions().clear();

			String path =_ROOT_PATH + getPath(expandoTableEntry.getKey());

			portalDataContext.addZipEntry(
				path + StringPool.FORWARD_SLASH + tableId + "-permissions.xml",
				document.formattedString());
		}
	}

	protected void importExpandoTable(
			PortalDataContext portalDataContext, String xml)
		throws Exception {

		Document document = SAXReaderUtil.read(xml);

		Element rootElement = document.getRootElement();

		Element expandoTableElement = rootElement.element("expando-table");

		String className = expandoTableElement.attributeValue("class-name");

		ExpandoTable expandoTable =
			ExpandoTableLocalServiceUtil.fetchDefaultTable(
				portalDataContext.getCompanyId(), className);

		if (expandoTable == null) {
			expandoTable = ExpandoTableLocalServiceUtil.addDefaultTable(
				portalDataContext.getCompanyId(), className);
		}

		List<Element> expandoColumnElements = expandoTableElement.elements(
			"expando-column");

		for (Element expandoColumnElement : expandoColumnElements) {
			if (expandoColumnElement.hasContent()) {
				long columnId = GetterUtil.getLong(
					expandoColumnElement.attributeValue("column-id"));
				String name = expandoColumnElement.attributeValue("name");
				int type = GetterUtil.getInteger(
					expandoColumnElement.attributeValue("type"));
				String defaultData = expandoColumnElement.elementText(
					"default-data");
				String typeSettings = expandoColumnElement.elementText(
					"type-settings");

				Serializable defaultDataObject =
					ExpandoConverterUtil.getAttributeFromString(
						type, defaultData);

				ExpandoColumn expandoColumn =
					ExpandoColumnLocalServiceUtil.getColumn(
						expandoTable.getTableId(), name);

				if (expandoColumn != null) {
					ExpandoColumnLocalServiceUtil.updateColumn(
						expandoColumn.getColumnId(), name, type,
						defaultDataObject);
				}
				else {
					expandoColumn = ExpandoColumnLocalServiceUtil.addColumn(
						expandoTable.getTableId(), name, type,
						defaultDataObject);
				}

				ExpandoColumnLocalServiceUtil.updateTypeSettings(
					expandoColumn.getColumnId(), typeSettings);

				portalDataContext.importPermissions(
					expandoColumn.getModelClassName(), columnId,
					expandoColumn.getColumnId());
			}
		}

	}

	protected void importPermissions(
		PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		List<String> permissionPaths = new ArrayList<String>();

		for (Element expandoTableElement :
			rootElement.elements("expando-table")) {

			String path = expandoTableElement.attributeValue("path");
			String tableId = expandoTableElement.attributeValue("id");

			String permissionPath = path + tableId + "-permissions.xml";

			permissionPaths.add(permissionPath);
		}

		_portalPermissionImporter.readPortalDataPermissions(
			portalDataContext, permissionPaths);
	}

	private void addExpandoColumnsMap(String className, long companyId)
		throws SystemException {

		if (!_expandoColumnsMap.containsKey(className)) {

			List<ExpandoColumn> expandoColumns =
				ExpandoColumnLocalServiceUtil.getDefaultTableColumns(
					companyId, className);

			_expandoColumnsMap.put(className, expandoColumns);
		}
	}

	private Document doExportPermissions(PortalDataContext portalDataContext)
		throws Exception {

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("expando-permissions");

		Map<String, List<KeyValuePair>> permissionsMap =
			portalDataContext.getPermissions();

		for (Map.Entry<String, List<KeyValuePair>> entry :
			permissionsMap.entrySet()) {

			String[] permissionParts = StringUtil.split(
				entry.getKey(), CharPool.POUND);

			String resourceName = permissionParts[0];
			long resourcePK = GetterUtil.getLong(permissionParts[1]);

			Element portalDataElement = rootElement.addElement("portal-data");

			portalDataElement.addAttribute("resource-name", resourceName);
			portalDataElement.addAttribute(
				"resource-pk", String.valueOf(resourcePK));

			List<KeyValuePair> permissions = entry.getValue();

			for (KeyValuePair permission : permissions) {
				String roleName = permission.getKey();
				String actions = permission.getValue();

				Element permissionsElement = portalDataElement.addElement(
					"permissions");

				permissionsElement.addAttribute("role-name", roleName);
				permissionsElement.addAttribute("actions", actions);
			}
		}

		return document;
	}

	private String getPath(String className) {
		return className.substring(
			className.lastIndexOf(StringPool.PERIOD) + 1).toLowerCase();
	}

	public static String _ROOT_PATH = "/expando-tables/";

	private Map<String, List<ExpandoColumn>> _expandoColumnsMap =
		new HashMap<String, List<ExpandoColumn>>();

	private PortalPermissionImporter _portalPermissionImporter =
		new PortalPermissionImporter();

}