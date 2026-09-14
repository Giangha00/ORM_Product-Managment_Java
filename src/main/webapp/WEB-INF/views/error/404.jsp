<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>404 - Khong tim thay trang</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-box" style="text-align:center;">
            <h1>404</h1>
            <p class="subtitle">Trang ban tim khong ton tai.</p>
            <a class="btn" href="<c:url value='/' />">Ve trang chu</a>
        </div>
    </div>
</body>
</html>
