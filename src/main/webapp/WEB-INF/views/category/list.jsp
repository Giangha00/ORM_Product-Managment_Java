<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh muc - ProductMVC</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container">
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success"><c:out value="${sessionScope.message}" /></div>
        <c:remove var="message" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error"><c:out value="${sessionScope.error}" /></div>
        <c:remove var="error" scope="session" />
    </c:if>

    <div class="page-header">
        <h2>Quan ly danh muc</h2>
    </div>

    <div class="card" style="max-width:520px; margin-bottom:20px;">
        <h3 class="form-section-title">Them danh muc</h3>
        <form method="post" action="<c:url value='/categories' />">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label for="name">Ten</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="description">Mo ta</label>
                <input type="text" id="description" name="description">
            </div>
            <button type="submit" class="btn">Them</button>
        </form>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty categories}">
                <div class="empty-state">Chua co danh muc.</div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Ten</th>
                            <th>Mo ta</th>
                            <th>Trang thai</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cat" items="${categories}">
                            <tr>
                                <td colspan="4">
                                    <form method="post" action="<c:url value='/categories' />" class="inline-form">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="id" value="${cat.id}">
                                        <input type="text" name="name" value="<c:out value='${cat.name}' />">
                                        <input type="text" name="description" value="<c:out value='${cat.description}' />">
                                        <select name="status">
                                            <option value="true" ${cat.status ? 'selected' : ''}>Dang hoat dong</option>
                                            <option value="false" ${not cat.status ? 'selected' : ''}>Ngung</option>
                                        </select>
                                        <button type="submit" class="btn btn-secondary">Luu</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
