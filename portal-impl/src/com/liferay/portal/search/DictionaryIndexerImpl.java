/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search;

import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.CustomEntryIndexer;
import com.liferay.portal.kernel.search.DictionaryIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.NGramHolder;
import com.liferay.portal.kernel.search.NGramHolderBuilderUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PortletKeys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author David Mendez Gonzalez
 * @author Daniela Zapata Riesco
 */
public class DictionaryIndexerImpl implements DictionaryIndexer {

	@Override
	public String getUID(
		long companyId, long[] groupIds, Locale locale, String keywords) {

		List<String> keys = new ArrayList<String>();

		keys.add(FILTER_TYPE_DICTIONARY);

		if (companyId > 0) {
			keys.add(String.valueOf(companyId));
		}

		keys.add(locale.toString());

		for (long groupId : groupIds) {
			keys.add(String.valueOf(groupId));
		}

		String lowerCaseKeywords = keywords.toLowerCase();
		keys.add(lowerCaseKeywords);

		String[] keyArray = new String[keys.size()];
		keyArray = keys.toArray(keyArray);

		return (String)_cacheKeyGenerator.getCacheKey(keyArray);
	}

	@Override
	public void indexDictionary(
			long companyId, long[] groupIds, Locale locale,
			InputStream inputStream)
		throws SearchException {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Loading dictionary words from file for locale '" +
					locale + "'");
		}

		Set<Document> documents = new HashSet<Document>();

		BufferedReader bufferedReader = null;

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, StringPool.UTF8);

			bufferedReader = new BufferedReader(inputStreamReader);

			String line = bufferedReader.readLine();

			if (line == null) {
				return;
			}

			if (line.charAt(0) == CustomEntryIndexer.UNICODE_BYTE_ORDER_MARK) {
				line = line.substring(1);
			}

			int lineCounter = 0;

			do {
				lineCounter++;

				String[] term = StringUtil.split(line, StringPool.SPACE);

				if (term.length > 0) {
					float weight = 0;

					if (term.length > 1) {
						try {
							weight = Float.parseFloat(term[1]);
						}
						catch (NumberFormatException e) {
							if (_log.isWarnEnabled()) {
								_log.warn(
									"Invalid weight for term: " + term[0]);
							}
						}
					}

					documents.add(
						getDictionaryEntryDocument(
							companyId, groupIds, locale, term[0], weight));

					line = bufferedReader.readLine();

					if ((lineCounter == _batchSize) || (line == null)) {
						SearchEngineUtil.addDocuments(
							SearchEngineUtil.getDefaultSearchEngineId(),
							companyId, documents);

						documents.clear();

						lineCounter = 0;
					}
				}
			}
			while (line != null);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to index dictionaries", e);
			}

			throw new SearchException(e.getMessage(), e);
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException ioe) {
					if (_log.isDebugEnabled()) {
						_log.debug("Unable to close dictionary file", ioe);
					}
				}
			}
		}
	}

	@Override
	public void indexDictionary(
			SearchContext searchContext, InputStream inputStream)
		throws SearchException {

		indexDictionary(
			searchContext.getCompanyId(), searchContext.getGroupIds(),
			searchContext.getLocale(), inputStream);
	}

	public void setBatchSize(int batchSize) {
		_batchSize = batchSize;
	}

	public void setCacheKeyGenerator(CacheKeyGenerator cacheKeyGenerator) {
		_cacheKeyGenerator = cacheKeyGenerator;
	}

	public void setDocument(Document document) {
		_document = document;
	}

	protected void addNGram(Document document, String text)
		throws SearchException {

		NGramHolder nGramHolder = NGramHolderBuilderUtil.buildNGramHolder(text);

		Map<String, List<String>> nGrams = nGramHolder.getNGrams();
		Map<String, String> nGramEnds = nGramHolder.getNGramEnds();
		Map<String, String> nGramStarts = nGramHolder.getNGramStarts();

		addNGramField(document, nGramEnds);
		addNGramField(document, nGramStarts);
		addNGramFields(document, nGrams);
	}

	protected void addNGramField(
		Document document, Map<String, String> nGrams) {

		for (Map.Entry<String, String> nGramEntry : nGrams.entrySet()) {
			document.addKeyword(nGramEntry.getKey(), nGramEntry.getValue());
		}
	}

	protected void addNGramFields(
		Document document, Map<String, List<String>> nGrams) {

		for (Map.Entry<String, List<String>> nGramEntry : nGrams.entrySet()) {
			String fieldName = nGramEntry.getKey();

			for (String nGramValue : nGramEntry.getValue()) {
				document.addKeyword(fieldName, nGramValue);
			}
		}
	}

	protected Document getDictionaryEntryDocument(
			long companyId, long[] groupIds, Locale locale, String word,
			float weight)
		throws SearchException {

		Document document = (Document)_document.clone();

		document.addKeyword(
			Field.UID, getUID(companyId, groupIds, locale, word));

		document.addKeyword(Field.COMPANY_ID, companyId);
		document.addKeyword(Field.GROUP_ID, groupIds);
		document.addKeyword(Field.LANGUAGE_ID, locale.toString());
		document.addKeyword(Field.PORTLET_ID, PortletKeys.SEARCH);
		document.addKeyword(Field.TYPE, FILTER_TYPE_DICTIONARY);

		document.addKeyword(Field.SPELL_CHECK_WORD, word);
		document.addKeyword(Field.SPELL_CHECK_WEIGHT, String.valueOf(weight));

		addNGram(document, word);

		return document;
	}

	private static final int _DEFAULT_BATCH_SIZE = 1000;

	private static Log _log = LogFactoryUtil.getLog(
		DictionaryIndexerImpl.class);

	private static CacheKeyGenerator _cacheKeyGenerator;

	private int _batchSize = _DEFAULT_BATCH_SIZE;
	private Document _document;

}