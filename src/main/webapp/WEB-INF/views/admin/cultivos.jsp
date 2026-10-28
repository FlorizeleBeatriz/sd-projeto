<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Cultivos"/></jsp:include>
<h1>Cultivos por campanha</h1>
<div class="card"><h2 style="margin-top:0">Registar cultivo</h2>
<form class="f" method="post" action="${ctx}/admin/cultivos">
  <label>Campanha <select name="campanhaId" required><c:forEach var="c" items="${campanhas}"><option value="${c.id}"><c:out value="${c.nome}"/> (${c.aberta ? 'aberta' : 'fechada'})</option></c:forEach></select></label>
  <label>Cultura <input name="cultura" placeholder="Feijão nhemba" required></label><label>Variedade <input name="variedade" required></label>
  <label>Hectares <input name="hectares" inputmode="decimal" required></label><label>Data de plantio <input type="date" name="plantio" required></label>
  <button class="btn" type="submit">Registar</button></form></div>
<div class="tabela"><table>
  <tr><th>ID</th><th>Campanha</th><th>Cultura</th><th>Variedade</th><th class="n">Hectares</th><th>Plantio</th><th></th></tr>
  <c:forEach var="c" items="${cultivos}"><tr><td>${c.id}</td><td><c:out value="${nomesCampanhas[c.campanhaId]}"/></td><td><c:out value="${c.cultura}"/></td>
    <td><c:out value="${c.variedade}"/></td><td class="n">${c.hectares} ha</td><td>${c.dataPlantioFmt}</td>
    <td><form class="inline" method="post" action="${ctx}/admin/cultivos/remover" onsubmit="return confirm('Remover este cultivo?')"><input type="hidden" name="id" value="${c.id}">
      <button class="btn peq perigo">Remover</button></form></td></tr></c:forEach>
  <c:if test="${empty cultivos}"><tr><td colspan="7" class="mudo">Sem cultivos.</td></tr></c:if>
</table></div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
