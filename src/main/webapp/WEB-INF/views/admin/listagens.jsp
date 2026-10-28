<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Listagens"/></jsp:include>
<h1>Listagens</h1>
<div class="filtros">
  <a href="${ctx}/admin/listagens/produtos" class="${tipo == 'produtos' ? 'on' : ''}">Produtos</a>
  <a href="${ctx}/admin/listagens/fornecedores" class="${tipo == 'fornecedores' ? 'on' : ''}">Fornecedores</a>
  <a href="${ctx}/admin/listagens/clientes" class="${tipo == 'clientes' ? 'on' : ''}">Clientes</a>
  <a href="${ctx}/admin/listagens/armazem" class="${tipo == 'armazem' ? 'on' : ''}">Armazém (stock &gt; 0)</a>
  <a href="${ctx}/admin/listagens/compras" class="${tipo == 'compras' ? 'on' : ''}">Compras detalhadas</a>
</div>
<c:set var="base" value="${ctx}/admin/listagens/${tipo}"/>
<c:choose>
<c:when test="${tipo == 'produtos' or tipo == 'armazem'}">
<div class="tabela"><table><tr><th>ID</th><a:th base="${base}" campo="nome" titulo="Nome" ordem="${ordem}" dir="${dir}"/><th>Categoria</th><th>Fornecedor</th>
  <a:th base="${base}" campo="preco" titulo="Preço" ordem="${ordem}" dir="${dir}"/><a:th base="${base}" campo="stock" titulo="Stock" ordem="${ordem}" dir="${dir}"/></tr>
  <c:forEach var="p" items="${produtos}"><tr><td>${p.id}</td><td><c:out value="${p.nome}"/></td><td>${p.categoria}</td><td><c:out value="${fornecedores[p.fornecedorId]}"/></td>
    <td class="n"><a:mt valor="${p.preco}"/></td><td>${p.quantidade} ${p.unidade}</td></tr></c:forEach>
  <c:if test="${empty produtos}"><tr><td colspan="6" class="mudo">Sem resultados.</td></tr></c:if></table></div>
</c:when>
<c:when test="${tipo == 'fornecedores'}">
<div class="tabela"><table><tr><th>ID</th><a:th base="${base}" campo="nome" titulo="Nome" ordem="${ordem}" dir="${dir}"/><a:th base="${base}" campo="tipo" titulo="Tipo" ordem="${ordem}" dir="${dir}"/><th>Contacto</th>
  <a:th base="${base}" campo="provincia" titulo="Província" ordem="${ordem}" dir="${dir}"/></tr>
  <c:forEach var="f" items="${fornecedoresLista}"><tr><td>${f.id}</td><td><c:out value="${f.nome}"/></td><td>${f.tipo}</td><td><c:out value="${f.contacto}"/></td><td><c:out value="${f.provincia}"/></td></tr></c:forEach>
  <c:if test="${empty fornecedoresLista}"><tr><td colspan="5" class="mudo">Sem resultados.</td></tr></c:if></table></div>
</c:when>
<c:when test="${tipo == 'clientes'}">
<div class="tabela"><table><tr><th>ID</th><a:th base="${base}" campo="nome" titulo="Nome" ordem="${ordem}" dir="${dir}"/><a:th base="${base}" campo="email" titulo="Email" ordem="${ordem}" dir="${dir}"/><th>Telefone</th><th>Endereço</th></tr>
  <c:forEach var="c" items="${clientes}"><tr><td>${c.id}</td><td><c:out value="${c.nome}"/></td><td><c:out value="${c.email}"/></td><td><c:out value="${c.telefone}"/></td><td><c:out value="${c.endereco}"/></td></tr></c:forEach>
  <c:if test="${empty clientes}"><tr><td colspan="5" class="mudo">Sem clientes.</td></tr></c:if></table></div>
</c:when>
<c:otherwise>
<p class="mudo">Ordenar por: <a href="${base}?ordem=data&amp;dir=desc">data</a> · <a href="${base}?ordem=valor&amp;dir=desc">valor</a> · <a href="${base}?ordem=cliente&amp;dir=asc">cliente</a></p>
<c:forEach var="v" items="${vendas}"><div class="card">
  <div style="display:flex;justify-content:space-between;flex-wrap:wrap;gap:.5rem"><strong>${v.numeroFactura}</strong><span class="mudo">${v.dataFmt}</span></div>
  <p class="mudo" style="margin:.3rem 0"><b><c:out value="${v.nomeCliente}"/></b> · <c:out value="${v.endereco}"/> · Tel. <c:out value="${v.telefone}"/></p>
  <div class="tabela"><table><tr><th>Produto</th><th class="n">Qtd.</th><th class="n">Valor unit.</th><th class="n">Subtotal</th></tr>
    <c:forEach var="i" items="${v.itens}"><tr><td><c:out value="${i.nomeProduto}"/></td><td class="n">${i.quantidade} ${i.unidade}</td><td class="n"><a:mt valor="${i.precoUnitario}"/></td><td class="n"><a:mt valor="${i.subtotal}"/></td></tr></c:forEach></table></div>
  <div class="totais"><div><span>Subtotal</span><span><a:mt valor="${v.subtotal}"/></span></div><div><span>IVA 16%</span><span><a:mt valor="${v.valorIva}"/></span></div>
    <div class="tot"><span>Total</span><span><a:mt valor="${v.total}"/></span></div></div>
  <a class="btn peq sec" href="${ctx}/factura?id=${v.id}">Abrir factura</a></div></c:forEach>
<c:if test="${empty vendas}"><div class="card">Ainda não há compras.</div></c:if>
</c:otherwise></c:choose>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
