package com.mobiarch.pulsar;

import java.io.File;
import java.util.Timer;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Engine extends Timer implements ContentHandler {
	static Engine instance;
	String cfgFile;
	Locator locator;
	InitialContext ctx;
	Logger logger = Logger.getLogger(getClass().getName());

	public Engine(String file) throws Exception {
		this.cfgFile = file;
		reloadConfig();
		instance = this;
	}

	public static Engine getInstance() {
		return instance;
	}

	private static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}

	public void reloadConfig() throws Exception {
		logger.info("Loading configuration file: " + this.cfgFile);

		this.ctx = new InitialContext();
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();

			SAXParser p = spf.newSAXParser();
			XMLReader xmlReader = p.getXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.parse(convertToFileURL(this.cfgFile));
		} catch (SAXException e) {
			if (e.getException() != null) {
				throw e.getException();
			}
			throw e;
		}
	}

	public void characters(char[] ac, int i, int j) throws SAXException {
	}

	public void endDocument() throws SAXException {
	}

	public void endElement(String s, String s1, String s2) throws SAXException {
	}

	public void endPrefixMapping(String s) throws SAXException {
	}

	public void ignorableWhitespace(char[] ac, int i, int j)
			throws SAXException {
	}

	public void processingInstruction(String s, String s1) throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void skippedEntity(String s) throws SAXException {
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String s, String name, String qName,
			Attributes attributes) throws SAXException {
		try {
			if (qName.equals("task")) {
				TaskItem i = new TaskItem();

				i.setFrequency(attributes.getValue("frequency"));
				i.setType(attributes.getValue("type"));
				i.setId(attributes.getValue("id"));
				i.setStartTime(attributes.getValue("start"));
				addTask(i);
			}
		} catch (Exception e) {
			String msg = "\nError loading: " + this.cfgFile;
			msg = msg + "\nLine: " + this.locator.getLineNumber();
			msg = msg + "\nReason: " + e.toString();
			throw new SAXException(msg, e);
		}
	}

	public void startPrefixMapping(String s, String s1) throws SAXException {
	}

	public void addTask(TaskItem item) throws Exception {
		if (item.getType().equals("java")) {
			logger.info("Loading class: " + item.getId());
			Class cls = Class.forName(item.getId());
			Object obj = cls.newInstance();
			item.setTask((Task) obj);
		} else if (item.getType().equals("ejb")) {
			logger.fine("Looking up EJB home: " + item.getId());
			Object obj = this.ctx.lookup(item.getId());
			EJBTaskHome th = (EJBTaskHome) PortableRemoteObject.narrow(obj,
					EJBTaskHome.class);
			item.setTask(th.create());
		} else {
			throw new Exception("Unsupported task type: " + item.getType());
		}
		if (item.getStartTime() == null) {
			logger.fine("Scheduling task to run immediately.");
			schedule(item, 0L, item.getFrequency() * 1000 * 60);
		} else {
			logger.fine("Will start task on: " + item.getStartTime().toString());
			schedule(item, item.getStartTime(), item.getFrequency() * 1000 * 60);
		}
	}
}
