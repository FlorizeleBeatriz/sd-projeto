<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Painel"/></jsp:include>
<h1>Painel de administração</h1>
<div class="stats">
  <div class="stat"><b>${contadores.fornecedores}</b><span>Fornecedores</span></div>
  <div class="stat"><b>${contadores.produtos}</b><span>Produtos</span></div>
  <div class="stat"><b>${contadores.clientes}</b><span>Clientes</span></div>
  <div class="stat"><b>${contadores.vendas}</b><span>Vendas</span></div>
  <div class="stat"><b>${contadores.campanhas}</b><span>Campanhas</span></div>
  <div class="stat"><b>${contadores.cultivos}</b><span>Cultivos</span></div>
  <div class="stat"><b>${contadores.colheitas}</b><span>Colheitas</span></div>
</div>
<h2>Atalhos</h2>
<div class="acoes">
  <a class="btn" href="${ctx}/admin/fornecedores">Novo fornecedor</a><a class="btn" href="${ctx}/admin/produtos">Novo produto</a>
  <a class="btn" href="${ctx}/admin/campanhas">Campanhas</a><a class="btn sec" href="${ctx}/admin/listagens/compras">Compras detalhadas</a>
  <a class="btn sec" href="${ctx}/admin/relatorios">Relatórios</a><a class="btn sec" href="${ctx}/admin/persistencia">Persistência</a>
</div>
<p class="mudo">Ficheiro de dados: <c:choose><c:when test="${ficheiroExiste}"><span class="tag">gravado</span></c:when><c:otherwise><span class="tag warn">ainda não gravado</span></c:otherwise></c:choose></p>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
