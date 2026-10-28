<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Colheitas"/></jsp:include>
<h1>Colheitas por cultivo</h1>
<div class="card"><h2 style="margin-top:0">Registar colheita</h2>
<form class="f" method="post" action="${ctx}/admin/colheitas">
  <label>Cultivo <select name="cultivoId" required><c:forEach var="c" items="${cultivos}"><option value="${c.id}">#${c.id} — <c:out value="${c.cultura}"/> (<c:out value="${c.variedade}"/>)</option></c:forEach></select></label>
  <label>Data <input type="date" name="data" required></label><label>Quantidade (kg) <input name="kg" inputmode="decimal" required></label>
  <label>Preço por kg (MT) <input name="precoKg" inputmode="decimal" required></label>
  <button class="btn" type="submit">Registar</button></form></div>
<div class="tabela"><table>
  <tr><th>ID</th><th>Cultivo</th><th>Data</th><th class="n">Quantidade</th><th class="n">Preço/kg</th><th class="n">Valor total</th><th></th></tr>
  <c:forEach var="h" items="${colheitas}"><tr><td>${h.id}</td><td><c:out value="${nomesCultivos[h.cultivoId]}"/></td><td>${h.dataFmt}</td>
    <td class="n"><fmt:formatNumber value="${h.quantidadeKg}" maxFractionDigits="2"/> kg</td><td class="n"><a:mt valor="${h.precoKg}"/></td>
    <td class="n"><b><a:mt valor="${h.valorTotal}"/></b></td>
    <td><form class="inline" method="post" action="${ctx}/admin/colheitas/remover" onsubmit="return confirm('Remover esta colheita?')"><input type="hidden" name="id" value="${h.id}">
      <button class="btn peq perigo">Remover</button></form></td></tr></c:forEach>
  <c:if test="${empty colheitas}"><tr><td colspan="7" class="mudo">Sem colheitas.</td></tr></c:if>
</table></div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
