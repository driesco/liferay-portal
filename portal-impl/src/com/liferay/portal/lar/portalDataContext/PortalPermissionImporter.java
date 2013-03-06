package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniela Zapata Riesco
 */
public class PortalPermissionImporter {

	public void readPortalDataPermissions(
			PortalDataContext portalDataContext,
			List<String> permissionPaths)
		throws Exception {

		for (String permissionPath : permissionPaths) {

			String xml = portalDataContext.getZipEntryAsString(permissionPath);

			if (xml == null) {
				return;
			}

			Document document = SAXReaderUtil.read(xml);

			Element rootElement = document.getRootElement();

			Element portalDataElement = rootElement.element("portal-data");

			String resourceName = portalDataElement.attributeValue(
				"resource-name");
			long resourcePK = GetterUtil.getLong(
				portalDataElement.attributeValue("resource-pk"));

			List<KeyValuePair> permissions = new ArrayList<KeyValuePair>();

			List<Element> permissionsElements = portalDataElement.elements(
				"permissions");

			for (Element permissionsElement : permissionsElements) {
				String roleName = permissionsElement.attributeValue(
					"role-name");
				String actions = permissionsElement.attributeValue("actions");

				KeyValuePair permission = new KeyValuePair(roleName, actions);

				permissions.add(permission);
			}

			portalDataContext.addPermissions(
				resourceName, resourcePK, permissions);
		}
	}

}

