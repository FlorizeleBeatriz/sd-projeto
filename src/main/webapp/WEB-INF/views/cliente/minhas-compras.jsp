<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Minhas compras"/></jsp:include>
<h1>Minhas compras</h1>
<c:choose><c:when test="${empty vendas}"><div class="card">Ainda não efectuou compras. <a href="${ctx}/catalogo">Ver catálogo</a></div></c:when>
<c:otherwise><div class="tabela"><table>
  <tr><th>Factura</th><th>Data</th><th>Itens</th><th class="n">Total</th><th></th></tr>
  <c:forEach var="v" items="${vendas}"><tr><td>${v.numeroFactura}</td><td>${v.dataFmt}</td><td>${v.itens.size()}</td>
    <td class="n"><a:mt valor="${v.total}"/></td><td><a class="btn peq" href="${ctx}/factura?id=${v.id}">Ver factura</a></td></tr></c:forEach>
</table></div></c:otherwise></c:choose>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
