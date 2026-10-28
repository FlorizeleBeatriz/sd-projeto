<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Campanhas"/></jsp:include>
<h1>Campanhas agrícolas</h1>
<div class="card"><h2 style="margin-top:0">Nova campanha</h2>
<form class="f" method="post" action="${ctx}/admin/campanhas">
  <label>Nome <input name="nome" placeholder="Campanha 2026/2027" required></label>
  <label>Início <input type="date" name="inicio" required></label><label>Fim <input type="date" name="fim" required></label>
  <button class="btn" type="submit">Criar</button></form></div>
<div class="tabela"><table>
  <tr><th>ID</th><th>Nome</th><th>Início</th><th>Fim</th><th>Estado</th><th>Acções</th></tr>
  <c:forEach var="c" items="${campanhas}"><tr><td>${c.id}</td><td><c:out value="${c.nome}"/></td><td>${c.inicioFmt}</td><td>${c.fimFmt}</td>
    <td><span class="tag ${c.aberta ? '' : 'off'}">${c.aberta ? 'ABERTA' : 'FECHADA'}</span></td>
    <td><form class="inline" method="post" action="${ctx}/admin/campanhas/${c.aberta ? 'fechar' : 'reabrir'}"><input type="hidden" name="id" value="${c.id}">
        <button class="btn peq sec">${c.aberta ? 'Fechar' : 'Reabrir'}</button></form>
      <form class="inline" method="post" action="${ctx}/admin/campanhas/remover" onsubmit="return confirm('Remover esta campanha?')"><input type="hidden" name="id" value="${c.id}">
        <button class="btn peq perigo">Remover</button></form></td></tr></c:forEach>
  <c:if test="${empty campanhas}"><tr><td colspan="6" class="mudo">Sem campanhas.</td></tr></c:if>
</table></div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
