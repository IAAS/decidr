<#--  /** 

Modified Hibernate code generation template that adds
backticks to column attribute names. This makes sure that
Hibernate will use quoted identifiers in every query, allowing
for column names that are reserved words (user, order, etc.)

@author Daniel Huss
@version 0.1
*/ -->
<#if column.isFormula()>
<formula>${column.getFormula()}</formula>
<#else>
<column name="`${column.quotedName}`" ${c2h.columnAttributes(column)}<#if column.comment?exists && column.comment?trim?length!=0>>
<comment>${column.comment}</comment>
</column><#else>/>
</#if>
</#if>