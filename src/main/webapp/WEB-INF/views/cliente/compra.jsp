<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Confirmar compra"/></jsp:include>
<h1>Confirmar compra</h1>
<div class="tabela"><table>
  <tr><th>Produto</th><th class="n">Qtd.</th><th class="n">Preço unit.</th><th class="n">Subtotal</th></tr>
  <c:forEach var="l" items="${linhas}"><tr><td><c:out value="${l.produto.nome}"/></td><td class="n">${l.quantidade} ${l.produto.unidade}</td>
    <td class="n"><a:mt valor="${l.produto.preco}"/></td><td class="n"><a:mt valor="${l.subtotal}"/></td></tr></c:forEach>
</table></div>
<div class="card totais" style="margin-top:1rem">
  <div><span>Subtotal</span><span><a:mt valor="${subtotal}"/></span></div>
  <div><span>IVA (16%)</span><span><a:mt valor="${iva}"/></span></div>
  <div class="tot"><span>Total</span><span><a:mt valor="${total}"/></span></div>
</div>
<p class="mudo">Ao confirmar, o stock é descontado e a factura é emitida. Se algum item não tiver stock suficiente, nenhuma quantidade é descontada.</p>
<form method="post" action="${ctx}/compra" class="acoes">
  <button class="btn" type="submit">Confirmar compra e emitir factura</button>
  <a class="btn sec" href="${ctx}/carrinho">Voltar ao carrinho</a>
</form>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
