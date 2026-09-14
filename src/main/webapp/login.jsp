<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Dang nhap - ProductMVC</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-box">
            <h1>Dang nhap he thong</h1>
            <p class="subtitle">ProductMVC &middot; JSP + Servlet + JavaBean + MySQL</p>

            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}" /></div>
            </c:if>

            <form action="<c:url value='/login' />" method="post">
                <div class="form-group">
                    <label for="username">Ten dang nhap</label>
                    <input type="text" id="username" name="username"
                           value="<c:out value='${username}' />" required autofocus>
                </div>
                <div class="form-group">
                    <label for="password">Mat khau</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-block">Dang nhap</button>
            </form>

            <p style="margin-top:16px; font-size:12px; color:#6b7280; text-align:center;">
                Tai khoan demo: <strong>admin</strong> / <strong>admin123</strong>
            </p>
        </div>
    </div>
    <script src="<c:url value='/js/main.js' />"></script>
</body>
</html>
