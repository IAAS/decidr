/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:41 LKT)
 */

package de.decidr.webservices.humantask;

/**
 * ExtensionMapper class
 */

public class ExtensionMapper {

	public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
			java.lang.String typeName, javax.xml.stream.XMLStreamReader reader)
			throws java.lang.Exception {

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "list-string".equals(typeName)) {

			return de.decidr.schema.decidrtypes.ListString.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tActor".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TActor.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tStringItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TStringItem.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TItem.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "list-boolean".equals(typeName)) {

			return de.decidr.schema.decidrtypes.ListBoolean.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tBooleanItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TBooleanItem.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tItemList".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TItemList.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "list-file".equals(typeName)) {

			return de.decidr.schema.decidrtypes.ListFile.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tIntegerItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TIntegerItem.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tURIItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TURIItem.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "list-integer".equals(typeName)) {

			return de.decidr.schema.decidrtypes.ListInteger.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tFloatItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TFloatItem.Factory
					.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "list-float".equals(typeName)) {

			return de.decidr.schema.decidrtypes.ListFloat.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tID".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TID.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tIDList".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TIDList.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "list-date".equals(typeName)) {

			return de.decidr.schema.decidrtypes.ListDate.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tRole".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TRole.Factory.parse(reader);

		}

		if ("http://decidr.de/schema/DecidrTypes".equals(namespaceURI)
				&& "tDateItem".equals(typeName)) {

			return de.decidr.schema.decidrtypes.TDateItem.Factory.parse(reader);

		}

		throw new org.apache.axis2.databinding.ADBException("Unsupported type "
				+ namespaceURI + " " + typeName);
	}

}
