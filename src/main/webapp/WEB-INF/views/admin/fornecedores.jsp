<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Fornecedores"/></jsp:include>
<h1>Fornecedores</h1>
<div class="card"><h2 style="margin-top:0">Novo fornecedor</h2>
<form class="f" method="post" action="${ctx}/admin/fornecedores">
  <label>Nome <input name="nome" required></label>
  <label>Tipo <select name="tipo"><c:forEach var="t" items="${tipos}"><option>${t}</option></c:forEach></select></label>
  <label>Contacto <input name="contacto" required></label>
  <label>Província <input name="provincia" required></label>
  <button class="btn" type="submit">Guardar</button>
</form></div>
<div class="tabela"><table>
  <tr><a:th base="${ctx}/admin/fornecedores" campo="nome" titulo="Nome" ordem="${param.ordem}" dir="${param.dir}"/><th>ID</th>
  <a:th base="${ctx}/admin/fornecedores" campo="tipo" titulo="Tipo" ordem="${param.ordem}" dir="${param.dir}"/><th>Contacto</th>
  <a:th base="${ctx}/admin/fornecedores" campo="provincia" titulo="Província" ordem="${param.ordem}" dir="${param.dir}"/><th></th></tr>
  <c:forEach var="f" items="${fornecedores}"><tr><td><c:out value="${f.nome}"/></td><td>${f.id}</td><td><span class="tag">${f.tipo}</span></td>
    <td><c:out value="${f.contacto}"/></td><td><c:out value="${f.provincia}"/></td>
    <td><form class="inline" method="post" action="${ctx}/admin/fornecedores/remover" onsubmit="return confirm('Remover este fornecedor?')">
      <input type="hidden" name="id" value="${f.id}"><button class="btn peq perigo">Remover</button></form></td></tr></c:forEach>
  <c:if test="${empty fornecedores}"><tr><td colspan="6" class="mudo">Sem fornecedores registados.</td></tr></c:if>
</table></div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
