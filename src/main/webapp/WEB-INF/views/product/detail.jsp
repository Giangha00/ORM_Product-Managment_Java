<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiet san pham - ProductMVC</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <h2>Chi tiet san pham</h2>
        <a class="btn btn-secondary" href="<c:url value='/products' />">&larr; Quay lai danh sach</a>
    </div>

    <div class="card" style="max-width:640px;">
        <fieldset class="readonly-form" disabled>
            <h3 class="form-section-title">Thong tin san pham</h3>

            <div class="form-group">
                <label for="sku">SKU</label>
                <input type="text" id="sku" value="<c:out value='${product.sku}' />">
            </div>

            <div class="form-group">
                <label for="name">Ten san pham</label>
                <input type="text" id="name" value="<c:out value='${product.name}' />">
            </div>

            <div class="form-group">
                <label for="price">Gia (VND)</label>
                <input type="text" id="price"
                       value="<fmt:formatNumber value='${product.price}' type='number' groupingUsed='true' />">
            </div>

            <div class="form-group">
                <label for="quantity">So luong</label>
                <input type="number" id="quantity" value="<c:out value='${product.quantity}' />">
            </div>

            <div class="form-group">
                <label for="categoryId">Danh muc</label>
                <select id="categoryId">
                    <option selected>
                        <c:out value="${product.categoryName}" />
                    </option>
                </select>
            </div>

            <div class="form-group">
                <label for="status">Trang thai</label>
                <select id="status">
                    <option value="true" ${product.status ? 'selected' : ''}>Con ban</option>
                    <option value="false" ${not product.status ? 'selected' : ''}>Ngung ban</option>
                </select>
            </div>

            <div class="form-group">
                <label for="description">Mo ta ngan</label>
                <textarea id="description" rows="2"><c:out value="${product.description}" /></textarea>
            </div>

            <h3 class="form-section-title">Chi tiet san pham</h3>

            <div class="form-group">
                <label for="manufacturer">Hang san xuat</label>
                <input type="text" id="manufacturer"
                       value="<c:out value='${product.detail.manufacturer}' />">
            </div>

            <div class="form-group">
                <label for="warrantyMonths">Bao hanh (thang)</label>
                <input type="number" id="warrantyMonths"
                       value="<c:out value='${product.detail.warrantyMonths}' />">
            </div>

            <div class="form-group">
                <label for="origin">Xuat xu</label>
                <input type="text" id="origin"
                       value="<c:out value='${product.detail.origin}' />">
            </div>

            <div class="form-group">
                <label for="detailDescription">Mo ta chi tiet</label>
                <textarea id="detailDescription" rows="3"><c:out value="${product.detail.description}" /></textarea>
            </div>

            <div class="form-group">
                <label for="technicalSpec">Thong so ky thuat</label>
                <textarea id="technicalSpec" rows="3"><c:out value="${product.detail.technicalSpec}" /></textarea>
            </div>
        </fieldset>

        <c:url var="editUrl" value="/products/edit">
            <c:param name="id" value="${product.id}" />
        </c:url>
        <div class="form-actions">
            <a class="btn" href="${editUrl}">Sua</a>
            <a class="btn btn-secondary" href="<c:url value='/products' />">Quay lai</a>
        </div>
    </div>
</div>
</body>
</html>
