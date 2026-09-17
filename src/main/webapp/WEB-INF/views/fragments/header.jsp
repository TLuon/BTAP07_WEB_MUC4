<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${param.title} - IOTStar</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary" href="${pageContext.request.contextPath}/">
            <i class="bi bi-bag-heart-fill me-2 text-danger"></i>IOTStar Shop
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav ms-auto align-items-center">
                <a class="nav-link" href="${pageContext.request.contextPath}/products">Sản phẩm</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/categories/ajax">Category (AJAX)</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/products/ajax">Product (AJAX)</a>
                <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin">Quản trị</a>
                </c:if>
                <c:choose>
                    <c:when test="${pageContext.request.userPrincipal != null}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                        <form action="${pageContext.request.contextPath}/logout" method="post" class="d-inline ms-2">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button class="btn btn-outline-light btn-sm">Đăng xuất</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
<main class="container py-4">
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
