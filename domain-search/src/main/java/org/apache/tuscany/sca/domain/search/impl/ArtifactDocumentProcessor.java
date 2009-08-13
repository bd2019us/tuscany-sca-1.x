package org.apache.tuscany.sca.domain.search.impl;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.document.Field;
import org.apache.tuscany.sca.contribution.Artifact;
import org.apache.tuscany.sca.contribution.Contribution;
import org.apache.tuscany.sca.domain.search.DocumentMap;
import org.apache.tuscany.sca.domain.search.DocumentProcessor;

public class ArtifactDocumentProcessor implements DocumentProcessor {

	public Document process(DocumentProcessor parentProcessor,
			DocumentMap documents, Object object, Document document,
			String parent) {

		if (object instanceof Artifact) {
			Artifact artifact = (Artifact) object;
			// String uri = artifact.getURI();

			// if (uri != null && uri.length() == 0) {
			// uri = null;
			// }
			//
			// if (uri != null) {
			//
			// if (uri.endsWith(".composite")
			// || uri.endsWith(".component")) {
			//
			// parent += DomainPathAnalyzer.PATH_SEPARATOR
			// + SearchFields.ARTIFACT_FIELD
			// + DomainPathAnalyzer.TYPE_SEPARATOR + uri;
			//
			// if (document == null) {
			// document = documents.get(uri);
			// }
			//
			// document.add(new Field(SearchFields.ARTIFACT_FIELD, uri,
			// Field.Store.YES, Field.Index.ANALYZED));
			//
			// }
			//
			// }

			if (!(object instanceof Contribution)) {

				String location = artifact.getLocation();

				try {

					if (document == null) {
						document = documents.get(SearchFields.ARTIFACT_FIELD + location);
					}

					FileContent fileContent = new WrappedFileContent(new URL(
							location));

					document.add(new Field(SearchFields.ARTIFACT_FIELD,
							fileContent.getName(), Field.Store.YES,
							Field.Index.ANALYZED));

					parent += DomainPathAnalyzer.PATH_SEPARATOR
							+ SearchFields.ARTIFACT_FIELD
							+ DomainPathAnalyzer.TYPE_SEPARATOR
							+ location + DomainPathAnalyzer.URI_SEPARATOR
							+ fileContent.getName();

					// parent += DomainPathAnalyzer.PATH_SEPARATOR
					// + SearchFields.FILE_FIELD
					// + DomainPathAnalyzer.TYPE_SEPARATOR + location +
					// DomainPathAnalyzer.URI_SEPARATOR + fileContent.getName();

					Document fileDoc = parentProcessor.process(parentProcessor,
							documents, fileContent, null, parent);

					fileDoc.add(new Field(SearchFields.PARENT_FIELD, parent,
							Field.Store.YES, Field.Index.ANALYZED));

				} catch (IOException e) {
					// ignore location
				}

			}

			return document == null ? FAKE_DOCUMENT : document;

		}

		throw new IllegalArgumentException();

	}

	public Object getDocumentKey(Object obj) {

		if (obj instanceof Artifact) {
			Artifact artifact = (Artifact) obj;
			String uri = artifact.getLocation();

			if (uri != null && uri.length() == 0) {
				return null;
			}

			return SearchFields.ARTIFACT_FIELD + uri;

		}

		throw new IllegalArgumentException();

	}

}