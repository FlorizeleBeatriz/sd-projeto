<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Erro"/></jsp:include>
<%@ page isErrorPage="true" %>
<div class="centro card">
  <h1>Erro ${pageContext.errorData.statusCode}</h1>
  <c:choose>
    <c:when test="${pageContext.errorData.statusCode == 403}"><p>Acesso negado. Não tem permissão para aceder a este recurso.</p></c:when>
    <c:when test="${pageContext.errorData.statusCode == 404}"><p>Página ou recurso não encontrado.</p></c:when>
    <c:otherwise><p>Não foi possível processar o pedido.</p></c:otherwise>
  </c:choose>
  <a class="btn" href="${ctx}/">Voltar ao início</a>
</div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
