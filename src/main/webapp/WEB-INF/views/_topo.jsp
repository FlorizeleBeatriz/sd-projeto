<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="pt_PT" scope="request"/>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="u" value="${sessionScope.utilizador}" scope="request"/>
<!DOCTYPE html>
<html lang="pt">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${param.titulo}"/> · AgroGest</title>
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<header class="topo">
  <div class="topo-in">
    <a class="marca" href="${ctx}/">🌾 AgroGest</a>
    <c:if test="${not empty u}">
      <input type="checkbox" id="menu-toggle" class="menu-toggle"><label for="menu-toggle" class="menu-btn" aria-label="Menu">☰</label>
      <nav class="menu">
        <c:choose>
          <c:when test="${u.admin}">
            <a href="${ctx}/admin/dashboard">Painel</a>
            <a href="${ctx}/admin/fornecedores">Fornecedores</a>
            <a href="${ctx}/admin/produtos">Produtos</a>
            <a href="${ctx}/admin/campanhas">Campanhas</a>
            <a href="${ctx}/admin/cultivos">Cultivos</a>
            <a href="${ctx}/admin/colheitas">Colheitas</a>
            <a href="${ctx}/admin/listagens/produtos">Listagens</a>
            <a href="${ctx}/admin/relatorios">Relatórios</a>
            <a href="${ctx}/admin/persistencia">Persistência</a>
          </c:when>
          <c:otherwise>
            <a href="${ctx}/catalogo">Catálogo</a>
            <a href="${ctx}/carrinho">Carrinho<c:if test="${not empty sessionScope.carrinho}"> (${sessionScope.carrinho.size()})</c:if></a>
            <a href="${ctx}/minhas-compras">Minhas compras</a>
          </c:otherwise>
        </c:choose>
        <span class="user"><c:out value="${u.nome}"/></span>
        <a class="sair" href="${ctx}/logout">Sair</a>
      </nav>
    </c:if>
  </div>
</header>
<main class="conteudo">
<c:if test="${not empty sessionScope.flashOk}"><div class="alerta ok" role="status"><c:out value="${sessionScope.flashOk}"/></div><c:remove var="flashOk" scope="session"/></c:if>
<c:if test="${not empty sessionScope.flashErro}"><div class="alerta erro" role="alert"><c:out value="${sessionScope.flashErro}"/></div><c:remove var="flashErro" scope="session"/></c:if>
<c:if test="${not empty erro}"><div class="alerta erro" role="alert"><c:out value="${erro}"/></div></c:if>
