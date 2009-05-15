/**
 * CreateTask.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:41 LKT)
 */

package de.decidr.webservices.humantask;

/**
 * CreateTask bean class
 */

public class CreateTask implements org.apache.axis2.databinding.ADBBean {

	public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
			"http://decidr.de/webservices/HumanTask", "createTask", "ns2");

	private static java.lang.String generatePrefix(java.lang.String namespace) {
		if (namespace.equals("http://decidr.de/webservices/HumanTask")) {
			return "ns2";
		}
		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/**
	 * field for WfmID
	 */

	protected de.decidr.schema.decidrtypes.TID localWfmID;

	/**
	 * Auto generated getter method
	 * 
	 * @return de.decidr.schema.decidrtypes.TID
	 */
	public de.decidr.schema.decidrtypes.TID getWfmID() {
		return localWfmID;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            WfmID
	 */
	public void setWfmID(de.decidr.schema.decidrtypes.TID param) {

		this.localWfmID = param;

	}

	/**
	 * field for ProcessID
	 */

	protected de.decidr.schema.decidrtypes.TID localProcessID;

	/**
	 * Auto generated getter method
	 * 
	 * @return de.decidr.schema.decidrtypes.TID
	 */
	public de.decidr.schema.decidrtypes.TID getProcessID() {
		return localProcessID;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            ProcessID
	 */
	public void setProcessID(de.decidr.schema.decidrtypes.TID param) {

		this.localProcessID = param;

	}

	/**
	 * field for UserID
	 */

	protected de.decidr.schema.decidrtypes.TID localUserID;

	/**
	 * Auto generated getter method
	 * 
	 * @return de.decidr.schema.decidrtypes.TID
	 */
	public de.decidr.schema.decidrtypes.TID getUserID() {
		return localUserID;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            UserID
	 */
	public void setUserID(de.decidr.schema.decidrtypes.TID param) {

		this.localUserID = param;

	}

	/**
	 * field for TaskName
	 */

	protected java.lang.String localTaskName;

	/**
	 * Auto generated getter method
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTaskName() {
		return localTaskName;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            TaskName
	 */
	public void setTaskName(java.lang.String param) {

		this.localTaskName = param;

	}

	/**
	 * field for UserNotification
	 */

	protected boolean localUserNotification = org.apache.axis2.databinding.utils.ConverterUtil
			.convertToBoolean("false");

	/*
	 * This tracker boolean wil be used to detect whether the user called the
	 * set method for this attribute. It will be used to determine whether to
	 * include this field in the serialized XML
	 */
	protected boolean localUserNotificationTracker = false;

	/**
	 * Auto generated getter method
	 * 
	 * @return boolean
	 */
	public boolean getUserNotification() {
		return localUserNotification;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            UserNotification
	 */
	public void setUserNotification(boolean param) {

		// setting primitive attribute tracker to true

		if (false) {
			localUserNotificationTracker = false;

		} else {
			localUserNotificationTracker = true;
		}

		this.localUserNotification = param;

	}

	/**
	 * field for Description
	 */

	protected java.lang.String localDescription;

	/**
	 * Auto generated getter method
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescription() {
		return localDescription;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            Description
	 */
	public void setDescription(java.lang.String param) {

		this.localDescription = param;

	}

	/**
	 * field for TaskData
	 */

	protected de.decidr.schema.decidrtypes.TItemList localTaskData;

	/*
	 * This tracker boolean wil be used to detect whether the user called the
	 * set method for this attribute. It will be used to determine whether to
	 * include this field in the serialized XML
	 */
	protected boolean localTaskDataTracker = false;

	/**
	 * Auto generated getter method
	 * 
	 * @return de.decidr.schema.decidrtypes.TItemList
	 */
	public de.decidr.schema.decidrtypes.TItemList getTaskData() {
		return localTaskData;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            TaskData
	 */
	public void setTaskData(de.decidr.schema.decidrtypes.TItemList param) {

		if (param != null) {
			// update the setting tracker
			localTaskDataTracker = true;
		} else {
			localTaskDataTracker = false;

		}

		this.localTaskData = param;

	}

	/**
	 * isReaderMTOMAware
	 * 
	 * @return true if the reader supports MTOM
	 */
	public static boolean isReaderMTOMAware(
			javax.xml.stream.XMLStreamReader reader) {
		boolean isReaderMTOMAware = false;

		try {
			isReaderMTOMAware = java.lang.Boolean.TRUE
					.equals(reader
							.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
		} catch (java.lang.IllegalArgumentException e) {
			isReaderMTOMAware = false;
		}
		return isReaderMTOMAware;
	}

	/**
	 * 
	 * @param parentQName
	 * @param factory
	 * @return org.apache.axiom.om.OMElement
	 */
	public org.apache.axiom.om.OMElement getOMElement(
			final javax.xml.namespace.QName parentQName,
			final org.apache.axiom.om.OMFactory factory)
			throws org.apache.axis2.databinding.ADBException {

		org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
				this, MY_QNAME) {

			public void serialize(
					org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
					throws javax.xml.stream.XMLStreamException {
				CreateTask.this.serialize(MY_QNAME, factory, xmlWriter);
			}
		};
		return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME,
				factory, dataSource);

	}

	public void serialize(
			final javax.xml.namespace.QName parentQName,
			final org.apache.axiom.om.OMFactory factory,
			org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException,
			org.apache.axis2.databinding.ADBException {
		serialize(parentQName, factory, xmlWriter, false);
	}

	public void serialize(
			final javax.xml.namespace.QName parentQName,
			final org.apache.axiom.om.OMFactory factory,
			org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
			boolean serializeType) throws javax.xml.stream.XMLStreamException,
			org.apache.axis2.databinding.ADBException {

		java.lang.String prefix = null;
		java.lang.String namespace = null;

		prefix = parentQName.getPrefix();
		namespace = parentQName.getNamespaceURI();

		if ((namespace != null) && (namespace.trim().length() > 0)) {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, parentQName
						.getLocalPart());
			} else {
				if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(),
						namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		} else {
			xmlWriter.writeStartElement(parentQName.getLocalPart());
		}

		if (serializeType) {

			java.lang.String namespacePrefix = registerPrefix(xmlWriter,
					"http://decidr.de/webservices/HumanTask");
			if ((namespacePrefix != null)
					&& (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi",
						"http://www.w3.org/2001/XMLSchema-instance", "type",
						namespacePrefix + ":createTask", xmlWriter);
			} else {
				writeAttribute("xsi",
						"http://www.w3.org/2001/XMLSchema-instance", "type",
						"createTask", xmlWriter);
			}

		}

		if (localWfmID == null) {
			throw new org.apache.axis2.databinding.ADBException(
					"wfmID cannot be null!!");
		}
		localWfmID.serialize(new javax.xml.namespace.QName("", "wfmID"),
				factory, xmlWriter);

		if (localProcessID == null) {
			throw new org.apache.axis2.databinding.ADBException(
					"processID cannot be null!!");
		}
		localProcessID.serialize(
				new javax.xml.namespace.QName("", "processID"), factory,
				xmlWriter);

		if (localUserID == null) {
			throw new org.apache.axis2.databinding.ADBException(
					"userID cannot be null!!");
		}
		localUserID.serialize(new javax.xml.namespace.QName("", "userID"),
				factory, xmlWriter);

		namespace = "";
		if (!namespace.equals("")) {
			prefix = xmlWriter.getPrefix(namespace);

			if (prefix == null) {
				prefix = generatePrefix(namespace);

				xmlWriter.writeStartElement(prefix, "taskName", namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);

			} else {
				xmlWriter.writeStartElement(namespace, "taskName");
			}

		} else {
			xmlWriter.writeStartElement("taskName");
		}

		if (localTaskName == null) {
			// write the nil attribute

			throw new org.apache.axis2.databinding.ADBException(
					"taskName cannot be null!!");

		} else {

			xmlWriter.writeCharacters(localTaskName);

		}

		xmlWriter.writeEndElement();
		if (localUserNotificationTracker) {
			namespace = "";
			if (!namespace.equals("")) {
				prefix = xmlWriter.getPrefix(namespace);

				if (prefix == null) {
					prefix = generatePrefix(namespace);

					xmlWriter.writeStartElement(prefix, "userNotification",
							namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);

				} else {
					xmlWriter.writeStartElement(namespace, "userNotification");
				}

			} else {
				xmlWriter.writeStartElement("userNotification");
			}

			if (false) {

				throw new org.apache.axis2.databinding.ADBException(
						"userNotification cannot be null!!");

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localUserNotification));
			}

			xmlWriter.writeEndElement();
		}
		namespace = "";
		if (!namespace.equals("")) {
			prefix = xmlWriter.getPrefix(namespace);

			if (prefix == null) {
				prefix = generatePrefix(namespace);

				xmlWriter.writeStartElement(prefix, "description", namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);

			} else {
				xmlWriter.writeStartElement(namespace, "description");
			}

		} else {
			xmlWriter.writeStartElement("description");
		}

		if (localDescription == null) {
			// write the nil attribute

			throw new org.apache.axis2.databinding.ADBException(
					"description cannot be null!!");

		} else {

			xmlWriter.writeCharacters(localDescription);

		}

		xmlWriter.writeEndElement();
		if (localTaskDataTracker) {
			if (localTaskData == null) {
				throw new org.apache.axis2.databinding.ADBException(
						"taskData cannot be null!!");
			}
			localTaskData.serialize(new javax.xml.namespace.QName("",
					"taskData"), factory, xmlWriter);
		}
		xmlWriter.writeEndElement();

	}

	/**
	 * Util method to write an attribute with the ns prefix
	 */
	private void writeAttribute(java.lang.String prefix,
			java.lang.String namespace, java.lang.String attName,
			java.lang.String attValue,
			javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException {
		if (xmlWriter.getPrefix(namespace) == null) {
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);

		}

		xmlWriter.writeAttribute(namespace, attName, attValue);

	}

	/**
	 * Util method to write an attribute without the ns prefix
	 */
	private void writeAttribute(java.lang.String namespace,
			java.lang.String attName, java.lang.String attValue,
			javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException {
		if (namespace.equals("")) {
			xmlWriter.writeAttribute(attName, attValue);
		} else {
			registerPrefix(xmlWriter, namespace);
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}
	}

	/**
	 * Util method to write an attribute without the ns prefix
	 */
	private void writeQNameAttribute(java.lang.String namespace,
			java.lang.String attName, javax.xml.namespace.QName qname,
			javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException {

		java.lang.String attributeNamespace = qname.getNamespaceURI();
		java.lang.String attributePrefix = xmlWriter
				.getPrefix(attributeNamespace);
		if (attributePrefix == null) {
			attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
		}
		java.lang.String attributeValue;
		if (attributePrefix.trim().length() > 0) {
			attributeValue = attributePrefix + ":" + qname.getLocalPart();
		} else {
			attributeValue = qname.getLocalPart();
		}

		if (namespace.equals("")) {
			xmlWriter.writeAttribute(attName, attributeValue);
		} else {
			registerPrefix(xmlWriter, namespace);
			xmlWriter.writeAttribute(namespace, attName, attributeValue);
		}
	}

	/**
	 * method to handle Qnames
	 */

	private void writeQName(javax.xml.namespace.QName qname,
			javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException {
		java.lang.String namespaceURI = qname.getNamespaceURI();
		if (namespaceURI != null) {
			java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
			if (prefix == null) {
				prefix = generatePrefix(namespaceURI);
				xmlWriter.writeNamespace(prefix, namespaceURI);
				xmlWriter.setPrefix(prefix, namespaceURI);
			}

			if (prefix.trim().length() > 0) {
				xmlWriter.writeCharacters(prefix
						+ ":"
						+ org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			} else {
				// i.e this is the default namespace
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}

		} else {
			xmlWriter
					.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
							.convertToString(qname));
		}
	}

	private void writeQNames(javax.xml.namespace.QName[] qnames,
			javax.xml.stream.XMLStreamWriter xmlWriter)
			throws javax.xml.stream.XMLStreamException {

		if (qnames != null) {
			// we have to store this data until last moment since it is not
			// possible to write any
			// namespace data after writing the charactor data
			java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
			java.lang.String namespaceURI = null;
			java.lang.String prefix = null;

			for (int i = 0; i < qnames.length; i++) {
				if (i > 0) {
					stringToWrite.append(" ");
				}
				namespaceURI = qnames[i].getNamespaceURI();
				if (namespaceURI != null) {
					prefix = xmlWriter.getPrefix(namespaceURI);
					if ((prefix == null) || (prefix.length() == 0)) {
						prefix = generatePrefix(namespaceURI);
						xmlWriter.writeNamespace(prefix, namespaceURI);
						xmlWriter.setPrefix(prefix, namespaceURI);
					}

					if (prefix.trim().length() > 0) {
						stringToWrite
								.append(prefix)
								.append(":")
								.append(
										org.apache.axis2.databinding.utils.ConverterUtil
												.convertToString(qnames[i]));
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				} else {
					stringToWrite
							.append(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qnames[i]));
				}
			}
			xmlWriter.writeCharacters(stringToWrite.toString());
		}

	}

	/**
	 * Register a namespace prefix
	 */
	private java.lang.String registerPrefix(
			javax.xml.stream.XMLStreamWriter xmlWriter,
			java.lang.String namespace)
			throws javax.xml.stream.XMLStreamException {
		java.lang.String prefix = xmlWriter.getPrefix(namespace);

		if (prefix == null) {
			prefix = generatePrefix(namespace);

			while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
				prefix = org.apache.axis2.databinding.utils.BeanUtil
						.getUniquePrefix();
			}

			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}

		return prefix;
	}

	/**
	 * databinding method to get an XML representation of this object
	 * 
	 */
	public javax.xml.stream.XMLStreamReader getPullParser(
			javax.xml.namespace.QName qName)
			throws org.apache.axis2.databinding.ADBException {

		java.util.ArrayList elementList = new java.util.ArrayList();
		java.util.ArrayList attribList = new java.util.ArrayList();

		elementList.add(new javax.xml.namespace.QName("", "wfmID"));

		if (localWfmID == null) {
			throw new org.apache.axis2.databinding.ADBException(
					"wfmID cannot be null!!");
		}
		elementList.add(localWfmID);

		elementList.add(new javax.xml.namespace.QName("", "processID"));

		if (localProcessID == null) {
			throw new org.apache.axis2.databinding.ADBException(
					"processID cannot be null!!");
		}
		elementList.add(localProcessID);

		elementList.add(new javax.xml.namespace.QName("", "userID"));

		if (localUserID == null) {
			throw new org.apache.axis2.databinding.ADBException(
					"userID cannot be null!!");
		}
		elementList.add(localUserID);

		elementList.add(new javax.xml.namespace.QName("", "taskName"));

		if (localTaskName != null) {
			elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
					.convertToString(localTaskName));
		} else {
			throw new org.apache.axis2.databinding.ADBException(
					"taskName cannot be null!!");
		}
		if (localUserNotificationTracker) {
			elementList.add(new javax.xml.namespace.QName("",
					"userNotification"));

			elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
					.convertToString(localUserNotification));
		}
		elementList.add(new javax.xml.namespace.QName("", "description"));

		if (localDescription != null) {
			elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
					.convertToString(localDescription));
		} else {
			throw new org.apache.axis2.databinding.ADBException(
					"description cannot be null!!");
		}
		if (localTaskDataTracker) {
			elementList.add(new javax.xml.namespace.QName("", "taskData"));

			if (localTaskData == null) {
				throw new org.apache.axis2.databinding.ADBException(
						"taskData cannot be null!!");
			}
			elementList.add(localTaskData);
		}

		return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
				qName, elementList.toArray(), attribList.toArray());

	}

	/**
	 * Factory class that keeps the parse method
	 */
	public static class Factory {

		/**
		 * static method to create the object Precondition: If this object is an
		 * element, the current or next start element starts this object and any
		 * intervening reader events are ignorable If this object is not an
		 * element, it is a complex type and the reader is at the event just
		 * after the outer start element Postcondition: If this object is an
		 * element, the reader is positioned at its end element If this object
		 * is a complex type, the reader is positioned at the end element of its
		 * outer element
		 */
		public static CreateTask parse(javax.xml.stream.XMLStreamReader reader)
				throws java.lang.Exception {
			CreateTask object = new CreateTask();

			int event;
			java.lang.String nillableValue = null;
			java.lang.String prefix = "";
			java.lang.String namespaceuri = "";
			try {

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.getAttributeValue(
						"http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
					java.lang.String fullTypeName = reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type");
					if (fullTypeName != null) {
						java.lang.String nsPrefix = null;
						if (fullTypeName.indexOf(":") > -1) {
							nsPrefix = fullTypeName.substring(0, fullTypeName
									.indexOf(":"));
						}
						nsPrefix = nsPrefix == null ? "" : nsPrefix;

						java.lang.String type = fullTypeName
								.substring(fullTypeName.indexOf(":") + 1);

						if (!"createTask".equals(type)) {
							// find namespace for the prefix
							java.lang.String nsUri = reader
									.getNamespaceContext().getNamespaceURI(
											nsPrefix);
							return (CreateTask) de.decidr.webservices.humantask.ExtensionMapper
									.getTypeObject(nsUri, type, reader);
						}

					}

				}

				// Note all attributes that were handled. Used to differ normal
				// attributes
				// from anyAttributes.
				java.util.Vector handledAttributes = new java.util.Vector();

				reader.next();

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "wfmID")
								.equals(reader.getName())) {

					object.setWfmID(de.decidr.schema.decidrtypes.TID.Factory
							.parse(reader));

					reader.next();

				} // End of if for expected property start element

				else {
					// A start element we are not expecting indicates an invalid
					// parameter was passed
					throw new org.apache.axis2.databinding.ADBException(
							"Unexpected subelement " + reader.getLocalName());
				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "processID")
								.equals(reader.getName())) {

					object
							.setProcessID(de.decidr.schema.decidrtypes.TID.Factory
									.parse(reader));

					reader.next();

				} // End of if for expected property start element

				else {
					// A start element we are not expecting indicates an invalid
					// parameter was passed
					throw new org.apache.axis2.databinding.ADBException(
							"Unexpected subelement " + reader.getLocalName());
				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "userID")
								.equals(reader.getName())) {

					object.setUserID(de.decidr.schema.decidrtypes.TID.Factory
							.parse(reader));

					reader.next();

				} // End of if for expected property start element

				else {
					// A start element we are not expecting indicates an invalid
					// parameter was passed
					throw new org.apache.axis2.databinding.ADBException(
							"Unexpected subelement " + reader.getLocalName());
				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "taskName")
								.equals(reader.getName())) {

					java.lang.String content = reader.getElementText();

					object
							.setTaskName(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

					reader.next();

				} // End of if for expected property start element

				else {
					// A start element we are not expecting indicates an invalid
					// parameter was passed
					throw new org.apache.axis2.databinding.ADBException(
							"Unexpected subelement " + reader.getLocalName());
				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "userNotification")
								.equals(reader.getName())) {

					java.lang.String content = reader.getElementText();

					object
							.setUserNotification(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToBoolean(content));

					reader.next();

				} // End of if for expected property start element

				else {

				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "description")
								.equals(reader.getName())) {

					java.lang.String content = reader.getElementText();

					object
							.setDescription(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

					reader.next();

				} // End of if for expected property start element

				else {
					// A start element we are not expecting indicates an invalid
					// parameter was passed
					throw new org.apache.axis2.databinding.ADBException(
							"Unexpected subelement " + reader.getLocalName());
				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement()
						&& new javax.xml.namespace.QName("", "taskData")
								.equals(reader.getName())) {

					object
							.setTaskData(de.decidr.schema.decidrtypes.TItemList.Factory
									.parse(reader));

					reader.next();

				} // End of if for expected property start element

				else {

				}

				while (!reader.isStartElement() && !reader.isEndElement())
					reader.next();

				if (reader.isStartElement())
					// A start element we are not expecting indicates a trailing
					// invalid property
					throw new org.apache.axis2.databinding.ADBException(
							"Unexpected subelement " + reader.getLocalName());

			} catch (javax.xml.stream.XMLStreamException e) {
				throw new java.lang.Exception(e);
			}

			return object;
		}

	}// end of factory class

}
