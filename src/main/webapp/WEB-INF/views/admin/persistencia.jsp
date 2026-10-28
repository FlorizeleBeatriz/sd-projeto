<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<jsp:include page="/WEB-INF/views/_topo.jsp"><jsp:param name="titulo" value="Persistência"/></jsp:include>
<h1>Persistência em ficheiro</h1>
<div class="card">
  <p>O estado é gravado <b>automaticamente</b> após cada alteração e ao encerrar o Tomcat, e restaurado no arranque.</p>
  <p>Ficheiro: <code><c:out value="${caminho}"/></code><br>
     Estado: <c:choose><c:when test="${existe}"><span class="tag">existe</span> · ${tamanho} bytes · última gravação ${ultima}</c:when><c:otherwise><span class="tag warn">não existe</span></c:otherwise></c:choose></p>
  <div class="acoes">
    <form method="post" action="${ctx}/admin/persistencia/gravar"><button class="btn">Gravar agora</button></form>
    <form method="post" action="${ctx}/admin/persistencia/restaurar" onsubmit="return confirm('Substituir o estado actual pelo ficheiro?')"><button class="btn sec">Restaurar do ficheiro</button></form>
    <form method="post" action="${ctx}/admin/persistencia/eliminar" onsubmit="return confirm('Eliminar o ficheiro de dados?')"><button class="btn perigo">Eliminar ficheiro</button></form>
  </div>
</div>
<jsp:include page="/WEB-INF/views/_rodape.jsp"/>
