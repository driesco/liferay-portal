package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Daniela Zapata Riesco
 */
public class PortalPermissionExporter {

	protected void exportPortalDataPermissions(
			PortalDataContext portalDataContext)
		throws Exception {

		Map<String, List<KeyValuePair>> permissionsMap =
			portalDataContext.getPermissions();

		for (Map.Entry<String, List<KeyValuePair>> entry :
			permissionsMap.entrySet()) {

			Document document = SAXReaderUtil.createDocument();

			Element rootElement =
				document.addElement("password-policies-permissions");

			String[] permissionParts = StringUtil.split(
				entry.getKey(), CharPool.POUND);

			String resourceName = permissionParts[0];
			long resourcePK = GetterUtil.getLong(permissionParts[1]);

			String path =
				"/password-policies/" +  resourcePK + "-permissions.xml";

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

			portalDataContext.addZipEntry(path, document.formattedString());
		}
	}

}