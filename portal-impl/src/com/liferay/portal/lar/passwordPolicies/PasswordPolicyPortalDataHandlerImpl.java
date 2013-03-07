package com.liferay.portal.lar.passwordPolicies;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.lar.portalDataContext.BasePortalDataHandler;
import com.liferay.portal.lar.portalDataContext.PortalDataContext;
import com.liferay.portal.lar.portalDataContext.PortalPermissionImporter;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.service.PasswordPolicyLocalServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daniela Zapata Riesco
 */
public class PasswordPolicyPortalDataHandlerImpl
	extends BasePortalDataHandler {

	@Override
	protected String doExportData(
			PortalDataContext portalDataContext)
		throws Exception {

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("password-policies");

		List<PasswordPolicy> passwordPolicies =
			PasswordPolicyLocalServiceUtil.getPasswordPolicies(
				0, PasswordPolicyLocalServiceUtil.getPasswordPoliciesCount());

		for (PasswordPolicy passwordPolicy : passwordPolicies) {
			exportPasswordPolicy(
				portalDataContext, rootElement, passwordPolicy);

			portalDataContext.addPermissions(
				passwordPolicy.getModelClassName(),
				passwordPolicy.getPasswordPolicyId());
		}

		exportPermissions(portalDataContext);

		return document.formattedString();
	}

	@Override
	protected  void doImportData(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		for (Element passwordPolicyElement :
				rootElement.elements("password-policy")) {

			String dataPath = passwordPolicyElement.attributeValue("data-path");

			PasswordPolicy passwordPolicy =
				(PasswordPolicy)portalDataContext.getZipEntryAsObject(dataPath);

			importPasswordPolicy(portalDataContext, passwordPolicy);
		}
	}

	@Override
	protected void doReadDataPermissions(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		List<Element> passwordPolicyElements = rootElement.elements(
			"password-policy");

		List<String> permissionPaths = new ArrayList<String>();

		for (Element passwordPolicyElement : passwordPolicyElements) {
			String permissionPath = passwordPolicyElement.attributeValue(
				"permission-path");

			permissionPaths.add(permissionPath);
		}

		_portalPermissionImporter.readPortalDataPermissions(
			portalDataContext, permissionPaths);
	}

	protected void exportPasswordPolicy(
			PortalDataContext portalDataContext,
			Element passwordPoliciesElement, PasswordPolicy passwordPolicy)
		throws Exception {

		String path = getPasswordPolicyPath(passwordPolicy);

		Element passwordPolicyElement = passwordPoliciesElement.addElement(
			"password-policy");

		portalDataContext.addClassedModel(
			passwordPolicyElement, path, passwordPolicy, _NAMESPACE);
	}

	protected void exportPermissions(PortalDataContext portalDataContext)
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

	protected String getPasswordPolicyPath(PasswordPolicy passwordPolicy) {
		return _ROOT_PATH + passwordPolicy.getPasswordPolicyId();
	}

	protected void importPasswordPolicy(
			PortalDataContext portalDataContext, PasswordPolicy passwordPolicy)
		throws Exception {

		PasswordPolicy importedPasswordPolicy = null;

		PasswordPolicy existingPasswordPolicy =
			PasswordPolicyLocalServiceUtil.getPasswordPolicy(
				passwordPolicy.getUuid());

		if (existingPasswordPolicy == null) {
			importedPasswordPolicy =
				PasswordPolicyLocalServiceUtil.addPasswordPolicy(
					passwordPolicy.getUserId(),
					passwordPolicy.getDefaultPolicy(), passwordPolicy.getName(),
					passwordPolicy.getDescription(),
					passwordPolicy.getChangeable(),
					passwordPolicy.getChangeRequired(),
					passwordPolicy.getMinAge(),passwordPolicy.getCheckSyntax(),
					passwordPolicy.getAllowDictionaryWords(),
					passwordPolicy.getMinAlphanumeric(),
					passwordPolicy.getMinLength(),
					passwordPolicy.getMinLowerCase(),
					passwordPolicy.getMinNumbers(),
					passwordPolicy.getMinSymbols(),
					passwordPolicy.getMinUpperCase(), passwordPolicy.getRegex(),
					passwordPolicy.getHistory(),
					passwordPolicy.getHistoryCount(),
					passwordPolicy.getExpireable(), passwordPolicy.getMaxAge(),
					passwordPolicy.getWarningTime(),
					passwordPolicy.getGraceLimit(), passwordPolicy.getLockout(),
					passwordPolicy.getMaxFailure(),
					passwordPolicy.getLockoutDuration(),
					passwordPolicy.getResetFailureCount(),
					passwordPolicy.getResetTicketMaxAge());

			importedPasswordPolicy.setUuid(passwordPolicy.getUuid());

			PasswordPolicyLocalServiceUtil.updatePasswordPolicy(
				importedPasswordPolicy);
		}
		else {
			importedPasswordPolicy =
				PasswordPolicyLocalServiceUtil.updatePasswordPolicy(
					existingPasswordPolicy.getPasswordPolicyId(),
					passwordPolicy.getName(), passwordPolicy.getDescription(),
					passwordPolicy.getChangeable(),
					passwordPolicy.getChangeRequired(),
					passwordPolicy.getMinAge(), passwordPolicy.getCheckSyntax(),
					passwordPolicy.getAllowDictionaryWords(),
					passwordPolicy.getMinAlphanumeric(),
					passwordPolicy.getMinLength(),
					passwordPolicy.getMinLowerCase(),
					passwordPolicy.getMinNumbers(),
					passwordPolicy.getMinSymbols(),
					passwordPolicy.getMinUpperCase(), passwordPolicy.getRegex(),
					passwordPolicy.getHistory(),
					passwordPolicy.getHistoryCount(),
					passwordPolicy.getExpireable(),
					passwordPolicy.getMaxAge(), passwordPolicy.getWarningTime(),
					passwordPolicy.getGraceLimit(), passwordPolicy.getLockout(),
					passwordPolicy.getMaxFailure(),
					passwordPolicy.getLockoutDuration(),
					passwordPolicy.getResetFailureCount(),
					passwordPolicy.getResetTicketMaxAge());
		}

		portalDataContext.importClassedModel(
			passwordPolicy, importedPasswordPolicy, _NAMESPACE);
	}

	private static final String _NAMESPACE = "passwordPolicies";

	private static final String _ROOT_PATH = "/password-policies/";

	private PortalPermissionImporter _portalPermissionImporter =
		new PortalPermissionImporter();

}
