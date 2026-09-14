<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<nav class="navbar">
    <div class="brand">ProductMVC</div>
    <div class="nav-right">
        <a href="<c:url value='/products' />">San pham</a>
        <a href="<c:url value='/categories' />">Danh muc</a>
        <span>Xin chao, <strong><c:out value="${sessionScope.loggedUser.fullName}" /></strong></span>
        <a href="<c:url value='/logout' />">Dang xuat</a>
    </div>
</nav>
