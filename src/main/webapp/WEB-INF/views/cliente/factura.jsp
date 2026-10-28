<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Factura"/></jsp:include>
<div class="acoes"><button class="btn" onclick="window.print()">Imprimir factura</button>
  <c:choose><c:when test="${u.admin}"><a class="btn sec" href="${ctx}/admin/listagens/compras">Voltar às compras</a></c:when>
  <c:otherwise><a class="btn sec" href="${ctx}/minhas-compras">Minhas compras</a></c:otherwise></c:choose></div>
<article class="factura">
  <header><div><h1 style="margin:0">FACTURA</h1><strong>${venda.numeroFactura}</strong></div>
    <div style="text-align:right"><strong>🌾 AgroGest</strong><br><span class="mudo">Gestão Agrícola · Nampula, Moçambique</span><br>Data: ${venda.dataFmt}</div></header>
  <h2>Dados do cliente</h2>
  <p><strong><c:out value="${venda.nomeCliente}"/></strong><br><c:out value="${venda.emailCliente}"/><br>
     <c:out value="${venda.endereco}"/><br>Tel.: <c:out value="${venda.telefone}"/></p>
  <div class="tabela"><table>
    <tr><th>Produto</th><th class="n">Qtd.</th><th class="n">Preço unit.</th><th class="n">Subtotal</th></tr>
    <c:forEach var="i" items="${venda.itens}"><tr><td><c:out value="${i.nomeProduto}"/></td><td class="n">${i.quantidade} ${i.unidade}</td>
      <td class="n"><a:mt valor="${i.precoUnitario}"/></td><td class="n"><a:mt valor="${i.subtotal}"/></td></tr></c:forEach>
  </table></div>
  <div class="totais" style="margin-top:1rem">
    <div><span>Subtotal</span><span><a:mt valor="${venda.subtotal}"/></span></div>
    <div><span>IVA (16%)</span><span><a:mt valor="${venda.valorIva}"/></span></div>
    <div class="tot"><span>Total</span><span><a:mt valor="${venda.total}"/></span></div>
  </div>
</article>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
