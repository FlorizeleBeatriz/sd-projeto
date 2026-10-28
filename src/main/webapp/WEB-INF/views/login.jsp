<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Entrar"/></jsp:include>
<div class="centro card">
  <h1>🌾 AgroGest — Entrar</h1>
  <form method="post" action="${ctx}/login" class="f" style="grid-template-columns:1fr">
    <label>Email <input type="email" name="email" value="<c:out value='${email}'/>" required autofocus></label>
    <label>Palavra-passe <input type="password" name="password" required></label>
    <button class="btn" type="submit">Entrar</button>
  </form>
  <p class="mudo">Ainda não tem conta? <a href="${ctx}/registo">Criar conta de cliente</a></p>
</div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
