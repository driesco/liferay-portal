package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipReaderFactoryUtil;
import com.liferay.portal.lar.passwordPolicies.PasswordPoliciesPortalDataHandlerImplNew;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniela Zapata Riesco
 */
public class PortalImporter {

	public void  importPortalInfo(File file, long companyId)
		throws Exception {

		doImportPortalInfo(file, companyId);
	}

	protected void doImportPortalInfo(File file, long companyId)
		throws Exception {

		ZipReader zipReader = ZipReaderFactoryUtil.getZipReader(file);

		PortalDataContext portalDataContext =
			new PortalDataContextImpl(companyId, zipReader);

		// Read paths

		String xml = portalDataContext.getZipEntryAsString("/portal-data.xml");

		Element rootElement = null;

		try {
			Document document = SAXReaderUtil.read(xml);

			rootElement = document.getRootElement();
		}
		catch (Exception e) {
			throw new IOException("Unable to read /portal-data.xml");
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Importing portal data");
		}

		importPortalData(portalDataContext, rootElement);
	}

	protected void importPortalData(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		PortalDataHandler portalDataHandler =
			new PasswordPoliciesPortalDataHandlerImplNew();

		portalDataHandler.readDataPermissions(portalDataContext, rootElement);

		portalDataHandler.importData(portalDataContext, rootElement);
	}

	private static Log _log = LogFactoryUtil.getLog(
		(PortalImporter.class));

}
