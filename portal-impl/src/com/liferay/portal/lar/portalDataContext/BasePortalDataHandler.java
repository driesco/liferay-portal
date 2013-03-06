package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.xml.Element;

/**
 * @author Daniela Zapata Riesco
 */
public class BasePortalDataHandler implements PortalDataHandler {


	public String exportData(PortalDataContext portalDataContext)
		throws PortalException {

		long startTime = 0;

		if (_log.isInfoEnabled()) {
			_log.info("Exporting portal " +
				PortalUUIDUtil.getPortalUUID().toString());

			startTime = System.currentTimeMillis();
		}

		try {
			return doExportData(portalDataContext);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			if (_log.isInfoEnabled()) {
				long duration = System.currentTimeMillis() - startTime;

				_log.info("Exported portal in " + Time.getDuration(duration));
			}
		}
	}

	public void importData(
			PortalDataContext portalDataContext, Element rootElement)
		throws PortalException {

		long startTime = 0;

		if (_log.isInfoEnabled()) {
			_log.info("Importing portlet " +
				PortalUUIDUtil.getPortalUUID().toString());

			startTime = System.currentTimeMillis();
		}

		try {
			doImportData(portalDataContext, rootElement);
		}
		catch (Exception e) {
			throw new PortalException(e);
		}
		finally {
			if (_log.isInfoEnabled()) {
				long duration = System.currentTimeMillis() - startTime;

				_log.info("Exported portal in " + Time.getDuration(duration));
			}
		}

	}

	public void readDataPermissions(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		doReadDataPermissions(portalDataContext, rootElement);
	}

	protected String doExportData(PortalDataContext portalDataContext)
		throws Exception {

		return null;
	}

	protected void doImportData(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		return;
	}

	protected void doReadDataPermissions(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception {

		return;
	}

	private static Log _log = LogFactoryUtil.getLog(
		(BasePortalDataHandler.class));
}
