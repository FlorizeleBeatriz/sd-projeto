<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Criar conta"/></jsp:include>
<div class="centro card">
  <h1>Criar conta de cliente</h1>
  <form method="post" action="${ctx}/registo" class="f" style="grid-template-columns:1fr">
    <label>Nome completo <input name="nome" value="<c:out value='${nome}'/>" required></label>
    <label>Email (único) <input type="email" name="email" value="<c:out value='${email}'/>" required></label>
    <label>Palavra-passe (mín. 4 caracteres) <input type="password" name="password" minlength="4" required></label>
    <label>Telefone <input name="telefone" value="<c:out value='${telefone}'/>" required></label>
    <label>Endereço <input name="endereco" value="<c:out value='${endereco}'/>" required></label>
    <button class="btn" type="submit">Registar</button>
  </form>
  <p class="mudo">Já tem conta? <a href="${ctx}/login">Entrar</a></p>
</div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
