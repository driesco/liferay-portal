package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.xml.Element;

/**
 * @author Daniela Zapata Riesco
 */
public interface PortalDataHandler {

	public String exportData(PortalDataContext portalDataContext)
		throws PortalException;

	public void importData(PortalDataContext portalDataContext, Element element)
		throws PortalException;

	public void readDataPermissions(
			PortalDataContext portalDataContext, Element rootElement)
		throws Exception;

}
