<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="base" required="true" %>
<%@ attribute name="campo" required="true" %>
<%@ attribute name="titulo" required="true" %>
<%@ attribute name="ordem" required="false" %>
<%@ attribute name="dir" required="false" %>
<c:set var="novaDir" value="${ordem == campo and dir == 'asc' ? 'desc' : 'asc'}"/>
<th><a class="ord" href="${base}${base.contains('?') ? '&' : '?'}ordem=${campo}&amp;dir=${novaDir}"><c:out value="${titulo}"/><c:if test="${ordem == campo}"> ${dir == 'asc' ? '▲' : '▼'}</c:if></a></th>
