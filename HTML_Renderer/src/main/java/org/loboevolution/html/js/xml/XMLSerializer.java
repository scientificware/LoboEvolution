/*
GNU GENERAL LICENSE
Copyright (C) 2014 - 2018 Lobo Evolution

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public
License as published by the Free Software Foundation; either
verion 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General License for more details.

You should have received a copy of the GNU General Public
along with this program.  If not, see <http://www.gnu.org/licenses/>.


Contact info: ivan.difrancesco@yahoo.it
*/
package org.loboevolution.html.js.xml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.loboevolution.js.AbstractScriptableDelegate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

public class XMLSerializer extends AbstractScriptableDelegate {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(XMLSerializer.class);

	/**
	 * The subtree rooted by the specified element is serialized to a string.
	 * 
	 * @param root
	 *            the root of the subtree to be serialized (this may be any
	 *            node, even a document)
	 * @return the serialized string
	 */
	public String serializeToString(Node root) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			if (root instanceof Document) {
				return documentToString((Document) root);
			} else if (root instanceof DocumentFragment) {
				if (root.getOwnerDocument() instanceof HTMLDocument) {
					return "";
				}
				root = root.getFirstChild();
			}

			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(root), new StreamResult(writer));
			return writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (TransformerException e) {
			logger.error(e);
		}
		return "";
	}

	private String documentToString(Document doc) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		elementToStream(doc.getDocumentElement(), baos);
		return String.valueOf(baos.toByteArray());
	}

	private void elementToStream(Element element, OutputStream out) {
		try {
			DOMSource source = new DOMSource(element);
			StreamResult result = new StreamResult(out);
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.transform(source, result);
		} catch (Exception ex) {
		}
	}

}
