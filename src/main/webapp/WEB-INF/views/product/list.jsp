<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Danh sach san pham - ProductMVC</title>
                <link rel="stylesheet" href="<c:url value='/css/style.css' />">
            </head>

            <body>
                <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

                <div class="container">

                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-success">
                            <c:out value="${sessionScope.message}" />
                        </div>
                        <c:remove var="message" scope="session" />
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-error">
                            <c:out value="${error}" />
                        </div>
                    </c:if>

                    <div class="page-header">
                        <h2>Danh sach san pham <span class="badge">${totalItems} san pham</span></h2>
                        <a class="btn" href="<c:url value='/products/create' />">+ Them san pham</a>
                    </div>

                    <div class="card">
                        <form class="search-box" method="get" action="<c:url value='/products' />">
                            <input type="text" name="keyword" placeholder="Tim theo ten"
                                value="<c:out value='${keyword}' />">
                            <select name="categoryId">
                                <option value="">-- Tat ca danh muc --</option>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.id}" ${categoryId==c.id ? 'selected' : '' }>
                                        <c:out value="${c.name}" />
                                    </option>
                                </c:forEach>
                            </select>
                            <select name="status">
                                <option value="">-- Tat ca trang thai --</option>
                                <option value="true" ${statusFilter==true ? 'selected' : '' }>Con ban</option>
                                <option value="false" ${statusFilter==false ? 'selected' : '' }>Ngung ban</option>
                            </select>
                            <input type="number" name="minPrice" step="0.01" min="0" placeholder="Gia tu"
                                value="<c:out value='${minPrice}' />">
                            <input type="number" name="maxPrice" step="0.01" min="0" placeholder="Gia den"
                                value="<c:out value='${maxPrice}' />">
                            <select name="sortBy">
                                <option value="">Moi nhat</option>
                                <c:forEach var="opt" items="${sortOptions}">
                                    <option value="${opt.key}" ${sortBy == opt.key ? 'selected' : ''}>
                                        <c:out value="${opt.value}" />
                                    </option>
                                </c:forEach>
                            </select>
                            <select name="sortDir">
                                <option value="desc" ${sortDir !='asc' ? 'selected' : '' }>Giam dan</option>
                                <option value="asc" ${sortDir=='asc' ? 'selected' : '' }>Tang dan</option>
                            </select>
                            <button type="submit" class="btn btn-secondary">Loc / Tim kiem</button>
                        </form>

                        <c:choose>
                            <c:when test="${empty products}">
                                <div class="empty-state">Khong co san pham nao.</div>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>SKU</th>
                                            <th>Ten san pham</th>
                                            <th>Gia</th>
                                            <th>So luong</th>
                                            <th>Danh muc</th>
                                            <th>Trang thai</th>
                                            <th>Thao tac</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${products}">
                                            <c:url var="detailUrl" value="/products/detail">
                                                <c:param name="id" value="${p.id}" />
                                            </c:url>
                                            <c:url var="editUrl" value="/products/edit">
                                                <c:param name="id" value="${p.id}" />
                                            </c:url>
                                            <c:url var="deleteUrl" value="/products/delete">
                                                <c:param name="id" value="${p.id}" />
                                            </c:url>
                                            <tr>
                                                <td>
                                                    <c:out value="${p.sku}" />
                                                </td>
                                                <td>
                                                    <a class="product-name-link" href="${detailUrl}">
                                                        <c:out value="${p.name}" />
                                                    </a>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${p.price}" type="number"
                                                        groupingUsed="true" /> d
                                                </td>
                                                <td>
                                                    <c:out value="${p.quantity}" />
                                                </td>
                                                <td>
                                                    <c:out value="${p.categoryName}" />
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${p.status}">
                                                            <span class="badge badge-ok">Con ban</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-off">Ngung ban</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="actions">
                                                    <a href="${detailUrl}">Chi tiet</a>
                                                    <a href="${editUrl}">Sua</a>
                                                    <a href="${deleteUrl}" data-name="<c:out value='${p.name}' />"
                                                        onclick="return confirmDelete(this.dataset.name)">Xoa</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <c:if test="${totalPages > 1}">
                                    <nav class="pagination">
                                        <c:choose>
                                            <c:when test="${page > 1}">
                                                <c:url var="prevUrl" value="/products">
                                                    <c:param name="keyword" value="${keyword}" />
                                                    <c:param name="categoryId" value="${categoryId}" />
                                                    <c:param name="status" value="${statusFilter}" />
                                                    <c:param name="minPrice" value="${minPrice}" />
                                                    <c:param name="maxPrice" value="${maxPrice}" />
                                                    <c:param name="sortBy" value="${sortBy}" />
                                                    <c:param name="sortDir" value="${sortDir}" />
                                                    <c:param name="page" value="${page - 1}" />
                                                </c:url>
                                                <a class="btn btn-secondary" href="${prevUrl}">Previous</a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="btn btn-secondary btn-disabled">Previous</span>
                                            </c:otherwise>
                                        </c:choose>

                                        <span class="page-info">Trang ${page} / ${totalPages}</span>

                                        <c:choose>
                                            <c:when test="${page < totalPages}">
                                                <c:url var="nextUrl" value="/products">
                                                    <c:param name="keyword" value="${keyword}" />
                                                    <c:param name="categoryId" value="${categoryId}" />
                                                    <c:param name="status" value="${statusFilter}" />
                                                    <c:param name="minPrice" value="${minPrice}" />
                                                    <c:param name="maxPrice" value="${maxPrice}" />
                                                    <c:param name="sortBy" value="${sortBy}" />
                                                    <c:param name="sortDir" value="${sortDir}" />
                                                    <c:param name="page" value="${page + 1}" />
                                                </c:url>
                                                <a class="btn btn-secondary" href="${nextUrl}">Next</a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="btn btn-secondary btn-disabled">Next</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </nav>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <script src="<c:url value='/js/main.js' />"></script>
            </body>

            </html>