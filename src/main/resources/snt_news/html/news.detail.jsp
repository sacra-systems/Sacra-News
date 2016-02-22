<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="user" uri="http://www.jahia.org/tags/user" %>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="propertyDefinition" type="org.jahia.services.content.nodetypes.ExtendedPropertyDefinition"--%>
<%--@elvariable id="type" type="org.jahia.services.content.nodetypes.ExtendedNodeType"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<bootstrap:addCSS/>
<template:addResources type="css" resources="bootstrapComponents.css"/>

<c:if test="${!empty jcr:getParentOfType(renderContext.mainResource.node, 'jnt:page')}">
    <c:url value='${url.base}${jcr:getParentOfType(renderContext.mainResource.node, "jnt:page").path}.html' var="action"/>
</c:if>
<c:if test="${empty jcr:getParentOfType(renderContext.mainResource.node, 'jnt:page')}">
    <c:set var="action">javascript:history.back()</c:set>
</c:if>
<c:set var="action">javascript:history.back()</c:set>

<jcr:nodeProperty node="${currentNode}" name="jcr:title" var="title"/>
<c:set var="newsTitleEscaped" value="${not empty title ? fn:escapeXml(title.string) : ''}"/>

<jcr:nodeProperty node="${currentNode}" name="desc" var="newsDesc"/>

<jcr:nodeProperty node="${currentNode}" name="jcr:createdBy" var="createdBy"/>
<c:set var="currentUser" value="${user:lookupUser(createdBy.string)}"/>

<jcr:nodeProperty node="${currentNode}" name="date" var="newsDate"/>
<fmt:formatDate value="${newsDate.time}" pattern="MMMM" var="newsMonth"/>
<fmt:formatDate value="${newsDate.time}" pattern="d" var="newsDay"/>
<fmt:formatDate value="${newsDate.time}" pattern="yyyy" var="newsYear"/>
<jcr:nodeProperty var="image" node="${currentNode}" name="image"/>
<c:url value='${url.base}${currentNode.path}.html' var="linkUrl" />

<jcr:nodeProperty node="${currentNode}" name="j:defaultCategory" var="newsCategories"/>



<article class="news">

    <div class="media-body"><h1>${title.string}</h1> </div>
  
    <span > ${newsDay}. ${newsMonth}&nbsp;${newsYear} &nbsp; ${user:fullName(currentUser)}</span>
    <br clear=all>
  

    <c:if test="${! empty image}">
        <img class="img-responsive newsImage" src="<c:url value="${image.node.url}" context="/"/>" alt="${image.node.displayableName}">
    </c:if>

    <div class="media-text-big">
      
      <c:set var="newsText" value="${fn:replace(newsDesc.string, '*kwl*', '')}" />
      ${fn:replace(newsText, '*hwl*', '')}     
    </div>
     
   </p>


    <div class="meta">
    <!-- display categories applied on this news -->
    <c:if test="${!empty newsCategories }">
        <div class="newsMeta">
            <span class="categoryLabel"><fmt:message key='label.categories'/>:</span>
            <jcr:nodeProperty node="${currentNode}" name="j:defaultCategory" var="cat"/>
            <c:if test="${cat != null}">
                <c:forEach items="${cat}" var="category">
                    <span class="categorytitle">${category.node.displayableName}</span> |
                </c:forEach>
            </c:if>
        </div>
    </c:if>
      
   <br clear=all>
   <div class="shariff" data-services="[&quot;facebook&quot;,&quot;mail&quot;,&quot;info&quot;]" data-mail-url="mailto:Email_Adresse_hier_eingeben@de" data-mail-subject="Interessanter Artikel auf der Website der Pfarrei Heiliger Martin Kaiserslautern" data-mail-body="Hallo, ich m&ouml;chte auf folgenden Artikel hinweisen: https://heiliger-martin-kaiserslautern.de${linkUrl} Liebe Gr&uuml;&szlig;e" data-backend-url="/shariff-backend/" data-url="https://heiliger-martin-kaiserslautern.de${linkUrl}" data-theme="standard" data-orientation="horizontal"></div>        
      
</div>

</article>



<br><br>
<a class="btn btn-primary" href="${action}" title="<fmt:message key="label.back"/>">
<i class="icon-chevron-left icon-white"></i> <fmt:message key="label.back"/> </a>
