<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Carrinho"/></jsp:include>
<h1>O meu carrinho</h1>
<c:choose>
<c:when test="${empty linhas}"><div class="card">O carrinho está vazio. <a href="${ctx}/catalogo">Ver catálogo</a></div></c:when>
<c:otherwise>
<form method="post" action="${ctx}/carrinho/actualizar">
<div class="tabela"><table>
  <tr><th>Produto</th><th class="n">Preço unit.</th><th>Quantidade</th><th class="n">Subtotal</th><th></th></tr>
  <c:forEach var="l" items="${linhas}">
  <tr>
    <td><c:out value="${l.produto.nome}"/><c:if test="${l.excedeStock}"> <span class="tag warn">acima do stock (${l.produto.quantidade})</span></c:if></td>
    <td class="n"><a:mt valor="${l.produto.preco}"/></td>
    <td><input type="number" name="q_${l.produto.id}" value="${l.quantidade}" min="0" style="width:6rem"> ${l.produto.unidade}</td>
    <td class="n"><a:mt valor="${l.subtotal}"/></td>
    <td><button class="btn peq perigo" type="submit" formaction="${ctx}/carrinho/remover?x=1" name="produtoId" value="${l.produto.id}" formnovalidate>Remover</button></td>
  </tr></c:forEach>
</table></div>
<div class="acoes">
  <button class="btn sec" type="submit">Actualizar quantidades</button>
  <button class="btn sec" type="submit" formaction="${ctx}/carrinho/esvaziar" formnovalidate>Esvaziar carrinho</button>
  <a class="btn sec" href="${ctx}/catalogo">Continuar a comprar</a>
</div>
</form>
<div class="card totais"><div><span>Subtotal</span><span><a:mt valor="${subtotal}"/></span></div>
  <div class="mudo"><span>IVA (16%) e total no passo seguinte</span></div></div>
<a class="btn" href="${ctx}/compra">Finalizar compra</a>
</c:otherwise></c:choose>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
