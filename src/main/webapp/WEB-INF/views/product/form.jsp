<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="isEdit" value="${not empty product and product.id > 0}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${isEdit ? 'Sua' : 'Them'} san pham - ProductMVC</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <h2>
            <c:choose>
                <c:when test="${isEdit}">Sua san pham</c:when>
                <c:otherwise>Them san pham moi</c:otherwise>
            </c:choose>
        </h2>
        <a class="btn btn-secondary" href="<c:url value='/products' />">&larr; Quay lai danh sach</a>
    </div>

    <div class="card" style="max-width:640px;">
        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}" /></div>
        </c:if>

        <c:choose>
            <c:when test="${isEdit}">
                <c:url var="formAction" value="/products/edit" />
            </c:when>
            <c:otherwise>
                <c:url var="formAction" value="/products/create" />
            </c:otherwise>
        </c:choose>

        <form method="post" action="${formAction}" onsubmit="return validateProductForm();">
            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="<c:out value='${product.id}' />">
            </c:if>

            <h3 class="form-section-title">Thong tin san pham</h3>

            <div class="form-group">
                <label for="sku">SKU</label>
                <input type="text" id="sku" name="sku" value="<c:out value='${product.sku}' />">
            </div>

            <div class="form-group">
                <label for="name">Ten san pham</label>
                <input type="text" id="name" name="name"
                       value="<c:out value='${product.name}' />">
            </div>

            <div class="form-group">
                <label for="price">Gia (VND)</label>
                <input type="number" id="price" name="price" step="0.01" min="0"
                       value="<c:out value='${product.price}' />">
            </div>

            <div class="form-group">
                <label for="quantity">So luong</label>
                <input type="number" id="quantity" name="quantity" min="0"
                       value="<c:out value='${product.quantity}' />">
            </div>

            <div class="form-group">
                <label for="categoryId">Danh muc</label>
                <select id="categoryId" name="categoryId">
                    <option value="">-- Chon danh muc --</option>
                    <c:forEach var="c" items="${categories}">
                        <option value="${c.id}" ${product.categoryId == c.id ? 'selected' : ''}>
                            <c:out value="${c.name}" />
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="status">Trang thai</label>
                <select id="status" name="status">
                    <option value="true" ${product.status ? 'selected' : ''}>Con ban</option>
                    <option value="false" ${not product.status ? 'selected' : ''}>Ngung ban</option>
                </select>
            </div>

            <div class="form-group">
                <label for="description">Mo ta ngan</label>
                <textarea id="description" name="description" rows="2"><c:out value="${product.description}" /></textarea>
            </div>

            <h3 class="form-section-title">Chi tiet san pham</h3>

            <div class="form-group">
                <label for="manufacturer">Hang san xuat</label>
                <input type="text" id="manufacturer" name="manufacturer"
                       value="<c:out value='${product.detail.manufacturer}' />">
            </div>

            <div class="form-group">
                <label for="warrantyMonths">Bao hanh (thang)</label>
                <input type="number" id="warrantyMonths" name="warrantyMonths" min="0"
                       value="<c:out value='${product.detail.warrantyMonths}' />">
            </div>

            <div class="form-group">
                <label for="origin">Xuat xu</label>
                <input type="text" id="origin" name="origin"
                       value="<c:out value='${product.detail.origin}' />">
            </div>

            <div class="form-group">
                <label for="detailDescription">Mo ta chi tiet</label>
                <textarea id="detailDescription" name="detailDescription" rows="3"><c:out value="${product.detail.description}" /></textarea>
            </div>

            <div class="form-group">
                <label for="technicalSpec">Thong so ky thuat</label>
                <textarea id="technicalSpec" name="technicalSpec" rows="3"><c:out value="${product.detail.technicalSpec}" /></textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn">Luu</button>
                <a class="btn btn-secondary" href="<c:url value='/products' />">Huy</a>
            </div>
        </form>
    </div>
</div>

<script src="<c:url value='/js/main.js' />"></script>
</body>
</html>
