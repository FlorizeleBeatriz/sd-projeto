<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Relatórios"/></jsp:include>
<h1>Relatórios de clientes e vendas</h1>
<h2>1. Clientes ordenados por nome (A–Z)</h2>
<div class="tabela"><table><tr><th>Nome</th><th>Email</th><th>Telefone</th><th class="n">Compras</th></tr>
<c:forEach var="r" items="${porNome}"><tr><td><c:out value="${r.cliente.nome}"/></td><td><c:out value="${r.cliente.email}"/></td><td><c:out value="${r.cliente.telefone}"/></td><td class="n">${r.numeroCompras}</td></tr></c:forEach>
<c:if test="${empty porNome}"><tr><td colspan="4" class="mudo">Sem clientes.</td></tr></c:if></table></div>
<h2>2. Clientes que compraram (por volume)</h2>
<div class="tabela"><table><tr><th>#</th><th>Cliente</th><th class="n">Compras</th><th class="n">Volume total</th></tr>
<c:forEach var="r" items="${compraram}" varStatus="st"><tr><td>${st.count}</td><td><c:out value="${r.cliente.nome}"/></td><td class="n">${r.numeroCompras}</td><td class="n"><a:mt valor="${r.volume}"/></td></tr></c:forEach>
<c:if test="${empty compraram}"><tr><td colspan="4" class="mudo">Sem compras.</td></tr></c:if></table></div>
<h2>3. Clientes mais assíduos (nº de compras)</h2>
<div class="tabela"><table><tr><th>Posição</th><th>Cliente</th><th class="n">Nº de compras</th><th class="n">Volume</th></tr>
<c:forEach var="r" items="${assiduos}" varStatus="st"><tr><td>${st.count}º</td><td><c:out value="${r.cliente.nome}"/></td><td class="n">${r.numeroCompras}</td><td class="n"><a:mt valor="${r.volume}"/></td></tr></c:forEach>
<c:if test="${empty assiduos}"><tr><td colspan="4" class="mudo">Sem compras.</td></tr></c:if></table></div>
<h2>4. (Extra) Clientes sem compras</h2>
<div class="tabela"><table><tr><th>Cliente</th><th>Email</th><th>Telefone</th></tr>
<c:forEach var="r" items="${semCompras}"><tr><td><c:out value="${r.cliente.nome}"/></td><td><c:out value="${r.cliente.email}"/></td><td><c:out value="${r.cliente.telefone}"/></td></tr></c:forEach>
<c:if test="${empty semCompras}"><tr><td colspan="3" class="mudo">Todos os clientes já compraram.</td></tr></c:if></table></div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
