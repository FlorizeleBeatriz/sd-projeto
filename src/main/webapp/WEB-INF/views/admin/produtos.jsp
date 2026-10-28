<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Produtos"/></jsp:include>
<h1>Produtos</h1>
<div class="card"><h2 style="margin-top:0">Novo produto</h2>
<form class="f" method="post" action="${ctx}/admin/produtos">
  <label>Nome <input name="nome" required></label>
  <label>Categoria <select name="categoria"><c:forEach var="c" items="${categorias}"><option>${c}</option></c:forEach></select></label>
  <label>Fornecedor <select name="fornecedorId" required>
    <c:forEach var="f" items="${fornecedoresLista}"><option value="${f.id}">#${f.id} — <c:out value="${f.nome}"/></option></c:forEach></select></label>
  <label>Preço (MT) <input name="preco" inputmode="decimal" required></label>
  <label>Quantidade <input type="number" name="quantidade" min="0" required></label>
  <label>Unidade <input name="unidade" placeholder="kg, saco, un" required></label>
  <label>Validade <input type="date" name="validade"></label>
  <label>Foto (URL) <input name="fotoUrl" placeholder="https://..."></label>
  <label>Descrição <input name="descricao"></label>
  <button class="btn" type="submit">Guardar</button>
</form>
<c:if test="${empty fornecedoresLista}"><p class="mudo">Registe primeiro um fornecedor.</p></c:if></div>
<div class="tabela"><table>
  <tr><th>ID</th><a:th base="${ctx}/admin/produtos" campo="nome" titulo="Nome" ordem="${param.ordem}" dir="${param.dir}"/><th>Categoria</th><th>Fornecedor</th>
  <a:th base="${ctx}/admin/produtos" campo="preco" titulo="Preço" ordem="${param.ordem}" dir="${param.dir}"/>
  <a:th base="${ctx}/admin/produtos" campo="stock" titulo="Stock" ordem="${param.ordem}" dir="${param.dir}"/><th>Validade</th><th>Repor stock</th><th></th></tr>
  <c:forEach var="p" items="${produtos}"><tr><td>${p.id}</td><td><c:out value="${p.nome}"/></td><td><span class="tag">${p.categoria}</span></td>
    <td><c:out value="${fornecedores[p.fornecedorId]}"/></td><td class="n"><a:mt valor="${p.preco}"/></td>
    <td>${p.quantidade} ${p.unidade}<c:if test="${p.quantidade == 0}"> <span class="tag warn">esgotado</span></c:if></td>
    <td>${p.validadeFmt}<c:if test="${p.expirado}"> <span class="tag warn">expirado</span></c:if></td>
    <td><form class="inline" method="post" action="${ctx}/admin/produtos/repor"><input type="hidden" name="id" value="${p.id}">
      <input type="number" name="quantidade" min="1" value="10" aria-label="Quantidade a repor"><button class="btn peq">+ Repor</button></form></td>
    <td><form class="inline" method="post" action="${ctx}/admin/produtos/remover" onsubmit="return confirm('Remover este produto?')">
      <input type="hidden" name="id" value="${p.id}"><button class="btn peq perigo">Remover</button></form></td></tr></c:forEach>
  <c:if test="${empty produtos}"><tr><td colspan="9" class="mudo">Sem produtos registados.</td></tr></c:if>
</table></div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
