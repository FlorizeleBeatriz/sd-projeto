<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Catálogo"/></jsp:include>
<h1>Catálogo de produtos</h1>
<div class="filtros">
  <a href="${ctx}/catalogo" class="${empty categoria ? 'on' : ''}">Todas</a>
  <c:forEach var="cat" items="${categorias}"><a href="${ctx}/catalogo?categoria=${cat}" class="${categoria == cat ? 'on' : ''}">${cat}</a></c:forEach>
</div>
<c:if test="${empty produtos}"><div class="card">Não há produtos disponíveis nesta categoria.</div></c:if>
<div class="grid">
<c:forEach var="p" items="${produtos}">
  <div class="card produto">
    <div class="foto"><c:choose>
      <c:when test="${not empty p.fotoUrl}"><img src="<c:out value='${p.fotoUrl}'/>" alt="<c:out value='${p.nome}'/>" loading="lazy"></c:when>
      <c:otherwise>${p.categoria == 'SEMENTE' ? '🌱' : p.categoria == 'FERTILIZANTE' ? '🧪' : p.categoria == 'MAQUINARIA' ? '🚜' : '🛠️'}</c:otherwise></c:choose></div>
    <div class="corpo">
      <strong><c:out value="${p.nome}"/></strong>
      <span class="tag">${p.categoria}</span>
      <span class="mudo"><c:out value="${p.descricao}"/></span>
      <span class="preco"><a:mt valor="${p.preco}"/> <small class="mudo">/ <c:out value="${p.unidade}"/></small></span>
      <span class="mudo">Disponível: <b>${p.quantidade}</b> ${p.unidade} · Validade: ${p.validadeFmt}</span>
      <span class="mudo">Fornecedor: <c:out value="${fornecedores[p.fornecedorId]}"/></span>
      <form class="inline" method="post" action="${ctx}/carrinho/adicionar">
        <input type="hidden" name="produtoId" value="${p.id}"><input type="hidden" name="categoria" value="${categoria}">
        <input type="number" name="quantidade" value="1" min="1" max="${p.quantidade}" aria-label="Quantidade">
        <button class="btn peq" type="submit">Adicionar</button>
      </form>
    </div>
  </div>
</c:forEach>
</div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
