<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>

<jcr:nodeProperty node="${currentNode}" name="maxNews" var="maxNews"/>
<jcr:nodeProperty node="${currentNode}" name="orderBy" var="orderBy"/>
<jcr:nodeProperty node="${currentNode}" name="j:filter" var="filters"/>
<jcr:nodeProperty node="${currentNode}" name='j:sortDirection' var="sortDirection"/>

<query:definition var="listQuery" limit="${currentResource.moduleParams.queryLoadAllUnsorted == 'true' ? -1 : maxNews.long}">
    <query:selector nodeTypeName="snt:news"/>
    <query:descendantNode path="${currentNode.resolveSite.path}"/>

    <query:or>
        <c:forEach var="filter" items="${filters}">
            <c:if test="${not empty filter.string}">
                <query:equalTo propertyName="j:defaultCategory" value="${filter.string}"/>
            </c:if>
        </c:forEach>
    </query:or>

    <query:sortBy propertyName="sortingDate" order="${sortDirection.string}"/>
</query:definition>

<%-- ${listQuery.statement} --%>
<c:set target="${moduleMap}" property="editable" value="false"/>
<c:set target="${moduleMap}" property="emptyListMessage"><fmt:message key="label.noNewsFound"/></c:set>
<c:set target="${moduleMap}" property="listQuery" value="${listQuery}"/>
<c:set target="${moduleMap}" property="subNodesView" value="${currentNode.properties['j:subNodesView'].string}"/>
