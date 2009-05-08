package de.decidr.model.reveng;

import java.util.List;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;

/**
 * Provides a customized reverse engineering strategy for the DecidR MySQL
 * database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 1.0
 */
public class DecidrReverseEngineeringStrategy extends
		DelegatingReverseEngineeringStrategy {

	/**
	 * Constructor
	 * 
	 * @param delegate
	 *            Fallback strategy
	 */
	public DecidrReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	/**
	 * Turns MySQL underscored table_names into camelCased identifiers.
	 * 
	 * @param tableName
	 *            underscored table name
	 * @return camelCased table name
	 */
	public String convertTableNameToIdentifier(String tableName) {
		String[] split = tableName.toLowerCase().split("_");
		String result = split[0];
		for (int i = 1; i < split.length; i++) {
			String rest = split[i];
			rest = rest.length() > 1 ? rest.substring(1, rest.length()) : "";
			result += split[i].substring(0, 1).toUpperCase() + rest;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String foreignKeyToCollectionName(String keyname,
			TableIdentifier fromTable, List fromColumns,
			TableIdentifier referencedTable, List referencedColumns,
			boolean uniqueReference) {

		String result = null;
		/*
		 * The following special case has been hardcoded. Should more than a few
		 * special cases occur, please consider using a configuration file.
		 * 
		 * We have tried using a reverse engineering configuration file
		 * (reveng.xml) but it only allows manual overrides on a per-table basis
		 * i.e. you have to specify the entire table.
		 */
		if ("fk_tenant_admin".equals(keyname)) {
			result = "administratedTenants";
		} else {
			result = super.foreignKeyToCollectionName(keyname, fromTable,
					fromColumns, referencedTable, referencedColumns,
					uniqueReference);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String foreignKeyToEntityName(String keyname,
			TableIdentifier fromTable, List fromColumnNames,
			TableIdentifier referencedTable, List referencedColumnNames,
			boolean uniqueReference) {

		String fromColumn = ((Column) fromColumnNames.get(0)).getName();
		if (fromColumn.endsWith("Id")) {
			return fromColumn.substring(0, fromColumn.length() - 2);
		} else {
			return super.foreignKeyToEntityName(keyname, fromTable,
					fromColumnNames, referencedTable, referencedColumnNames,
					uniqueReference);
		}
	}

	@Override
	public String foreignKeyToManyToManyName(ForeignKey fromKey,
			TableIdentifier middleTable, ForeignKey toKey,
			boolean uniqueReference) {

		return convertTableNameToIdentifier(middleTable.getName());
	}

}