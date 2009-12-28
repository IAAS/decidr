<#--  /** 
  The DecidR Development Team licenses this file to you under
  the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied. See the License for the
  specific language governing permissions and limitations
  under the License.

Modified Hibernate code generation template that adds dynamic-update=true and
dynamic-insert=true to ALL generated entities.
     
@author Daniel Huss
@version 0.1
*/ -->
<${c2h.getTag(clazz)} 
    name="${c2h.getClassName(clazz)}"
<#if !c2h.getClassName(clazz).equals(clazz.entityName)>
    entity-name="${clazz.entityName}"
</#if>
<#if clazz.superclass?exists>
    extends="${clazz.getSuperclass().className}"
</#if>
<#if c2h.needsTable(clazz)>
    table="${clazz.table.quotedName}"
</#if>
<#if clazz.table.schema?exists>
    schema="${clazz.table.quotedSchema}"
</#if>
<#if clazz.table.catalog?exists>
    catalog="${clazz.table.catalog}"
</#if>
<#if !clazz.mutable>
    mutable="false"
</#if>
    dynamic-update="true"
    dynamic-insert="true"
<#if clazz.hasSelectBeforeUpdate()>
    select-before-update="true"
</#if>
<#if c2h.needsDiscriminator(clazz)>
    discriminator-value="${clazz.discriminatorValue}"
</#if>
<#if clazz.isExplicitPolymorphism()>
    polymorphism="explicit"
</#if>
<#if clazz.isLazy() && !c2h.getClassName(clazz).equals(c2h.getProxyInterfaceName(clazz))>
    proxy="${c2h.getProxyInterfaceName(clazz)}"
<#elseif !clazz.isLazy()>
    lazy="false"
</#if>
<#if clazz.abstract?exists && clazz.abstract>
    abstract="true"
</#if>
<#if c2h.isClassLevelOptimisticLockMode(clazz)>
    optimistic-lock="${c2h.getClassLevelOptimisticLockMode(clazz)}"
</#if>
<#if (clazz.batchSize>1)>
    batch-size="${clazz.batchSize}"
</#if>
<#if clazz.where?exists>
    where="${clazz.where}"
</#if>
<#if clazz.table.subselect>
    subselect="${clazz.table.getSubselect()}"
</#if>
<#if c2h.hasCustomEntityPersister(clazz)>
    persister="${clazz.getEntityPersisterClass().name}"
</#if>
<#if clazz.table.rowId?exists>
    rowid="${clazz.table.rowId}"
</#if>>
<#assign metaattributable=clazz/>
<#include "meta.hbm.ftl"/>

<#if clazz.table.comment?exists  && clazz.table.comment?trim?length!=0>
 <comment>${clazz.table.comment}</comment>
</#if>
<#-- TODO: move this to id.hbm.ftl -->
<#if !c2h.isSubclass(clazz)>
 <#if clazz.hasIdentifierProperty()>
 <#assign property=clazz.getIdentifierProperty()/>
 <#include "id.hbm.ftl"/>
 <#elseif clazz.hasEmbeddedIdentifier()>
 <#assign embeddedid=clazz.key/>
 <#include "id.hbm.ftl"/>
 </#if>
<#elseif c2h.isJoinedSubclass(clazz)>
 <key> 
       <#foreach column in clazz.key.columnIterator>
                <#include "column.hbm.ftl">
       </#foreach>
 </key>
</#if>

<#-- version has to be done explicitly since Annotation's does not list version first -->
<#if pojo.hasVersionProperty()>
<#assign property=clazz.getVersion()/>
<#include "${c2h.getTag(property)}.hbm.ftl"/>
</#if>

<#foreach property in clazz.getUnjoinedPropertyIterator()>
<#if c2h.getTag(property)!="version" && c2h.getTag(property)!="timestamp">
<#include "${c2h.getTag(property)}.hbm.ftl"/>
</#if>
</#foreach>

</${c2h.getTag(clazz)}>