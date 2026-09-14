<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
    index.jsp chi dong vai tro "dieu huong" ban dau, khong chua logic nghiep vu.
    Toan bo viet bang JSTL - KHONG dung scriptlet Java.
    <c:redirect> tu dong them contextPath khi url bat dau bang "/".
--%>
<c:choose>
    <c:when test="${not empty sessionScope.loggedUser}">
        <c:redirect url="/products" />
    </c:when>
    <c:otherwise>
        <c:redirect url="/login" />
    </c:otherwise>
</c:choose>
