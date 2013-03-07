package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.lar.passwordPolicies.PasswordPolicyPortalDataHandlerImpl;

import java.io.File;

/**
 * @author Daniela Zapata Riesco
 */
public class PortalExporter {

	public File exportPortalInfoAsFile(long companyId) throws Exception {

		return doExportPortalInfoAsFile(companyId);
	}

	protected File doExportPortalInfoAsFile(long companyId) throws Exception {

		ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

		PortalDataContext portalDataContext = new PortalDataContextImpl(
			companyId, zipWriter);

		exportPortal(portalDataContext);

		return zipWriter.getFile();
	}

	protected void exportPortal(PortalDataContext portalDataContext)
		throws Exception {

		exportPortalData(portalDataContext);
	}

	protected void exportPortalData(PortalDataContext portalDataContext)
		throws Exception {

		PortalDataHandler portalDataHandler =
			new PasswordPolicyPortalDataHandlerImpl();

		String data = null;

		String path = "/portal-data.xml";

		try {
			data = portalDataHandler.exportData(portalDataContext);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		if (Validator.isNull(data)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Not exporting data because null data was returned");
			}

			return;
		}

		portalDataContext.addZipEntry(path, data);
	}

	private static Log _log = LogFactoryUtil.getLog(
		(PortalExporter.class));

	private PortalPermissionExporter _portalPermissionExporter =
		new PortalPermissionExporter();

}
