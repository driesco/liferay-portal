package com.liferay.portal.lar.portalDataContext;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.model.ClassedModel;

import java.util.List;
import java.util.Map;

/**
 * @author Daniela Zapata Riesco
 */
public interface PortalDataContext {

	public void addPermissions(String resourceName, long resourcePK)
		throws PortalException, SystemException;

	public void addPermissions(
		String resourceName, long resourcePK, List<KeyValuePair> permissions);

	public void addClassedModel(
			Element element, String path, ClassedModel classedModel,
			String namespace)
		throws PortalException, SystemException;

	public void addZipEntry(String path, Object object) throws SystemException;

	public void addZipEntry(String path, String s) throws SystemException;

	public Object fromXML(String xml);

	public long getCompanyId();

	public Map<String, List<KeyValuePair>> getPermissions();

	public Object getZipEntryAsObject(String path);

	public String getZipEntryAsString(String path);

	public ZipReader getZipReader();

	public ZipWriter getZipWriter();

	public void importClassedModel(
			ClassedModel classedModel, ClassedModel newClassedModel,
			String namespace)
		throws PortalException, SystemException;

	public void importPermissions(Class<?> clazz, long classPK, long newClassPK)
		throws PortalException, SystemException;

	public void importPermissions(
		String resourceObj, long resourcePK, long newResourcePK)
		throws PortalException, SystemException;

	public String toXML(Object object);

}
