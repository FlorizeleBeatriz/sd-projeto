<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="valor" required="true" type="java.lang.Object" %>
<fmt:formatNumber value="${valor}" type="number" minFractionDigits="2" maxFractionDigits="2"/> MT
