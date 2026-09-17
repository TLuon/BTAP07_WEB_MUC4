<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="title" value="Quản lý Sản phẩm (AJAX)" />
</jsp:include>

<div class="container-fluid px-0">
    <!-- Top Hero Banner -->
    <div class="page-banner mb-4" style="background: linear-gradient(135deg, #064e3b 0%, #047857 50%, #10b981 100%);">
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div>
                <div class="badge bg-white bg-opacity-20 text-white px-3 py-2 rounded-pill mb-2 fw-semibold">
                    <i class="bi bi-lightning-charge-fill me-1"></i> Fast RESTful API & Dynamic AJAX Table
                </div>
                <h2 class="mb-1 fw-extrabold"><i class="bi bi-box-seam-fill me-2"></i>Quản Lý Danh Sách Sản Phẩm</h2>
                <p class="mb-0">Quản lý kho hàng, tìm kiếm linh hoạt theo danh mục và giá sản phẩm không cần tải lại trang</p>
            </div>
            <div>
                <button id="btnOpenAddModal" class="btn btn-light btn-lg shadow text-emerald fw-bold">
                    <i class="bi bi-plus-circle-fill me-2 text-emerald"></i>Thêm Sản Phẩm Mới
                </button>
            </div>
        </div>
    </div>

    <!-- Alert Notification Box -->
    <div id="alertBox" class="alert alert-dismissible fade show d-none mb-4" role="alert">
        <div class="d-flex align-items-center">
            <i class="bi bi-info-circle-fill me-2 fs-5" id="alertIcon"></i>
            <span id="alertMessage" class="fw-semibold"></span>
        </div>
        <button type="button" class="btn-close" onclick="$('#alertBox').addClass('d-none')"></button>
    </div>

    <!-- Filter Card -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-3 bg-white rounded-3">
            <form id="searchForm" class="row g-3 align-items-center" onsubmit="return false;">
                <div class="col-md-4">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-search"></i></span>
                        <input type="text" id="searchKeyword" class="form-control border-start-0" placeholder="Nhập tên sản phẩm cần tìm...">
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group">
                        <label class="input-group-text bg-light text-muted fw-bold" for="filterCategory"><i class="bi bi-tag-fill me-1"></i>Danh mục</label>
                        <select id="filterCategory" class="form-select">
                            <option value="">-- Tất cả danh mục --</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group">
                        <label class="input-group-text bg-light text-muted fw-bold" for="pageSize"><i class="bi bi-list-numeric me-1"></i>Hiển thị</label>
                        <select id="pageSize" class="form-select">
                            <option value="5" selected>5 sản phẩm / trang</option>
                            <option value="10">10 sản phẩm / trang</option>
                            <option value="20">20 sản phẩm / trang</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-2 d-flex gap-2">
                    <button type="button" id="btnSearch" class="btn btn-success flex-fill">
                        <i class="bi bi-funnel-fill me-1"></i> Lọc
                    </button>
                    <button type="button" id="btnReset" class="btn btn-outline-secondary" title="Đặt lại lọc">
                        <i class="bi bi-arrow-clockwise"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Data Table Card -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" id="productTable">
                    <thead>
                        <tr>
                            <th scope="col" class="text-center" style="width: 70px;">ID</th>
                            <th scope="col" class="text-center" style="width: 100px;">Ảnh SP</th>
                            <th scope="col">Tên sản phẩm</th>
                            <th scope="col">Danh mục</th>
                            <th scope="col" class="text-end">Đơn giá (VNĐ)</th>
                            <th scope="col" class="text-center" style="width: 100px;">Giảm giá</th>
                            <th scope="col" class="text-center" style="width: 100px;">Kho</th>
                            <th scope="col" class="text-center" style="width: 140px;">Trạng thái</th>
                            <th scope="col" class="text-center" style="width: 170px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="productTableBody">
                        <!-- Dynamic jQuery Render -->
                    </tbody>
                </table>
            </div>
        </div>
        <!-- Card Footer / Pagination -->
        <div class="card-footer bg-white d-flex justify-content-between align-items-center py-3">
            <div id="pageInfoText" class="text-muted fw-semibold small">
                Đang tải dữ liệu...
            </div>
            <nav>
                <ul class="pagination pagination-sm mb-0" id="paginationContainer">
                    <!-- Dynamic Pagination Render -->
                </ul>
            </nav>
        </div>
    </div>
</div>

<!-- Product Modal (Add / Edit) -->
<div class="modal fade" id="productModal" tabindex="-1" aria-labelledby="productModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content shadow-lg border-0">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title fw-bold" id="productModalLabel">
                    <i class="bi bi-box-seam me-2"></i>Thêm Sản Phẩm Mới
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="productForm" enctype="multipart/form-data">
                <div class="modal-body p-4">
                    <input type="hidden" id="productId" name="productId" value="">

                    <div class="row g-3">
                        <div class="col-md-8">
                            <label for="productName" class="form-label fw-bold">Tên Sản Phẩm <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="productName" name="productName" placeholder="Nhập tên sản phẩm..." required>
                        </div>

                        <div class="col-md-4">
                            <label for="modalCategoryId" class="form-label fw-bold">Danh Mục <span class="text-danger">*</span></label>
                            <select class="form-select" id="modalCategoryId" name="categoryId" required>
                                <option value="">-- Chọn danh mục --</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label for="unitPrice" class="form-label fw-bold">Đơn Giá (VNĐ) <span class="text-danger">*</span></label>
                            <input type="number" step="1000" min="0" class="form-control" id="unitPrice" name="unitPrice" placeholder="VD: 15000000" required>
                        </div>

                        <div class="col-md-4">
                            <label for="discount" class="form-label fw-bold">Giảm Giá (%)</label>
                            <input type="number" step="0.1" min="0" max="100" class="form-control" id="discount" name="discount" value="0" placeholder="VD: 10">
                        </div>

                        <div class="col-md-4">
                            <label for="quantity" class="form-label fw-bold">Số Lượng Kho <span class="text-danger">*</span></label>
                            <input type="number" min="0" class="form-control" id="quantity" name="quantity" placeholder="VD: 50" required>
                        </div>

                        <div class="col-md-4">
                            <label for="status" class="form-label fw-bold">Trạng Thái</label>
                            <select class="form-select" id="status" name="status">
                                <option value="1" selected>Đang bán</option>
                                <option value="0">Ngừng kinh doanh</option>
                            </select>
                        </div>

                        <div class="col-md-8">
                            <label for="imageFile" class="form-label fw-bold">Ảnh Đại Diện Sản Phẩm</label>
                            <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
                            <div class="form-text">Chọn ảnh mới để cập nhật. Giữ nguyên nếu không muốn đổi ảnh.</div>
                        </div>

                        <div class="col-12">
                            <label for="description" class="form-label fw-bold">Mô Tả Sản Phẩm</label>
                            <textarea class="form-control" id="description" name="description" rows="3" placeholder="Mô tả chi tiết tính năng sản phẩm..."></textarea>
                        </div>

                        <div class="col-12 text-center d-none" id="previewContainer">
                            <p class="small text-muted mb-1 fw-bold">Ảnh hiện tại:</p>
                            <img id="imagePreview" src="" alt="Product Preview" class="img-preview-thumb rounded shadow-sm" style="width: 120px; height: 120px; object-fit: contain;">
                        </div>
                    </div>
                </div>
                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">
                        <i class="bi bi-x-circle me-1"></i>Hủy
                    </button>
                    <button type="submit" class="btn btn-success px-4" id="btnSaveProduct">
                        <i class="bi bi-check-circle-fill me-1"></i>Lưu Sản Phẩm
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- AJAX JavaScript Script -->
<script>
    const CONTEXT_PATH = "${pageContext.request.contextPath}";
    let currentPage = 0;
    let currentPageSize = 5;
    let currentKeyword = "";
    let currentCategoryId = "";

    $(document).ready(function() {
        loadCategoryOptions();
        loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);

        $('#btnSearch').click(function() {
            currentKeyword = $('#searchKeyword').val().trim();
            currentCategoryId = $('#filterCategory').val();
            currentPageSize = parseInt($('#pageSize').val());
            currentPage = 0;
            loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);
        });

        $('#searchKeyword').keypress(function(e) {
            if (e.which === 13) {
                $('#btnSearch').click();
            }
        });

        $('#filterCategory').change(function() {
            $('#btnSearch').click();
        });

        $('#pageSize').change(function() {
            currentPageSize = parseInt($(this).val());
            currentPage = 0;
            loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);
        });

        $('#btnReset').click(function() {
            $('#searchKeyword').val('');
            $('#filterCategory').val('');
            currentKeyword = '';
            currentCategoryId = '';
            currentPage = 0;
            loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);
        });

        $('#btnOpenAddModal').click(function() {
            $('#productForm')[0].reset();
            $('#productId').val('');
            $('#productModalLabel').html('<i class="bi bi-box-seam me-2"></i>Thêm Sản Phẩm Mới');
            $('#previewContainer').addClass('d-none');
            $('#productModal').modal('show');
        });

        $('#imageFile').change(function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    $('#imagePreview').attr('src', e.target.result);
                    $('#previewContainer').removeClass('d-none');
                };
                reader.readAsDataURL(file);
            }
        });

        $('#productForm').submit(function(e) {
            e.preventDefault();

            const productIdVal = $('#productId').val();
            const isEdit = productIdVal && parseInt(productIdVal) > 0;
            const apiUrl = isEdit ? CONTEXT_PATH + "/api/product/updateProduct" : CONTEXT_PATH + "/api/product/addProduct";
            const apiMethod = isEdit ? "PUT" : "POST";

            const formData = new FormData(this);

            $('#btnSaveProduct').prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-1"></span>Đang xử lý...');

            $.ajax({
                url: apiUrl,
                type: apiMethod,
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    $('#btnSaveProduct').prop('disabled', false).html('<i class="bi bi-check-circle-fill me-1"></i>Lưu Sản Phẩm');
                    if (response.status) {
                        $('#productModal').modal('hide');
                        showAlert("success", response.message || "Thao tác sản phẩm thành công!");
                        loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);
                    } else {
                        showAlert("danger", response.message || "Có lỗi xảy ra!");
                    }
                },
                error: function(xhr) {
                    $('#btnSaveProduct').prop('disabled', false).html('<i class="bi bi-check-circle-fill me-1"></i>Lưu Sản Phẩm');
                    let errMsg = "Lỗi khi kết nối với máy chủ!";
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errMsg = xhr.responseJSON.message;
                    }
                    showAlert("danger", errMsg);
                }
            });
        });

        $(document).on('click', '.btn-edit', function() {
            const id = $(this).data('id');
            $.ajax({
                url: CONTEXT_PATH + "/api/product/getProduct?id=" + id,
                type: "POST",
                success: function(response) {
                    if (response.status && response.body) {
                        const prod = response.body;
                        $('#productId').val(prod.productId);
                        $('#productName').val(prod.productName);
                        $('#description').val(prod.description);
                        $('#unitPrice').val(prod.price);
                        $('#quantity').val(prod.stock);
                        $('#status').val(prod.status);

                        if (prod.category) {
                            $('#modalCategoryId').val(prod.category.categoryId);
                        }

                        if (prod.productImages && prod.productImages.length > 0) {
                            const imgPath = prod.productImages[0].imagePath;
                            const imgSrc = imgPath.startsWith('http') ? imgPath : CONTEXT_PATH + "/uploads/" + imgPath;
                            $('#imagePreview').attr('src', imgSrc);
                            $('#previewContainer').removeClass('d-none');
                        } else {
                            $('#previewContainer').addClass('d-none');
                        }

                        $('#productModalLabel').html('<i class="bi bi-pencil-square me-2"></i>Cập Nhật Sản Phẩm #' + prod.productId);
                        $('#productModal').modal('show');
                    } else {
                        showAlert("danger", response.message || "Không tìm thấy sản phẩm!");
                    }
                },
                error: function() {
                    showAlert("danger", "Lỗi khi tải thông tin sản phẩm!");
                }
            });
        });

        $(document).on('click', '.btn-delete', function() {
            const id = $(this).data('id');
            const name = $(this).data('name');
            const row = $(this).closest('tr');

            if (confirm("Bạn có chắc chắn muốn xóa sản phẩm '" + name + "' (ID: " + id + ")?")) {
                $.ajax({
                    url: CONTEXT_PATH + "/api/product/deleteProduct?productId=" + id,
                    type: "DELETE",
                    success: function(response) {
                        if (response.status) {
                            row.fadeOut(400, function() {
                                showAlert("success", response.message || "Đã xóa sản phẩm thành công!");
                                loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);
                            });
                        } else {
                            showAlert("danger", response.message || "Không thể xóa sản phẩm này!");
                        }
                    },
                    error: function(xhr) {
                        let msg = "Lỗi khi xóa sản phẩm!";
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            msg = xhr.responseJSON.message;
                        }
                        showAlert("danger", msg);
                    }
                });
            }
        });
    });

    function loadCategoryOptions() {
        $.ajax({
            url: CONTEXT_PATH + "/api/category",
            type: "GET",
            success: function(response) {
                if (response.status && response.body) {
                    const filterSelect = $('#filterCategory');
                    const modalSelect = $('#modalCategoryId');

                    filterSelect.find('option:gt(0)').remove();
                    modalSelect.find('option:gt(0)').remove();

                    $.each(response.body, function(i, cat) {
                        const opt = `<option value="${cat.categoryId}">${escapeHtml(cat.categoryname)}</option>`;
                        filterSelect.append(opt);
                        modalSelect.append(opt);
                    });
                }
            }
        });
    }

    function loadData(page, size, keyword, categoryId) {
        $('#productTableBody').html('<tr><td colspan="9" class="text-center py-5"><div class="spinner-border text-success" role="status"></div><p class="mt-2 mb-0 text-muted fw-semibold">Đang tải danh sách sản phẩm...</p></td></tr>');

        let url = CONTEXT_PATH + "/api/product/searchPaginated?page=" + page + "&size=" + size + "&sort=productId";
        if (keyword && keyword.length > 0) {
            url += "&name=" + encodeURIComponent(keyword);
        }
        if (categoryId && categoryId.length > 0) {
            url += "&categoryId=" + categoryId;
        }

        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function(response) {
                if (response.status && response.body) {
                    renderTable(response.body);
                    renderPagination(response.body);
                } else {
                    $('#productTableBody').html('<tr><td colspan="9" class="text-center text-muted py-5"><i class="bi bi-inbox fs-2 d-block mb-2 text-secondary"></i>Không tìm thấy sản phẩm nào phù hợp.</td></tr>');
                    $('#pageInfoText').text('');
                    $('#paginationContainer').html('');
                }
            },
            error: function(xhr) {
                $('#productTableBody').html('<tr><td colspan="9" class="text-center text-danger py-5"><i class="bi bi-exclamation-triangle fs-2 d-block mb-2"></i>Không thể kết nối đến máy chủ!</td></tr>');
            }
        });
    }

    function renderTable(pageData) {
        const content = pageData.content;
        const tbody = $('#productTableBody');
        tbody.empty();

        if (!content || content.length === 0) {
            tbody.html('<tr><td colspan="9" class="text-center text-muted py-5"><i class="bi bi-inbox fs-2 d-block mb-2 text-secondary"></i>Không có sản phẩm nào.</td></tr>');
            return;
        }

        $.each(content, function(index, prod) {
            let imgHtml = '<span class="badge bg-light text-muted border">Chưa có ảnh</span>';
            if (prod.productImages && prod.productImages.length > 0) {
                const imgPath = prod.productImages[0].imagePath;
                const imgSrc = imgPath.startsWith('http') ? imgPath : CONTEXT_PATH + "/uploads/" + imgPath;
                imgHtml = `<img src="${imgSrc}" class="img-preview-thumb" onerror="this.src='https://placehold.co/55x55?text=No+Img'">`;
            }

            const catName = prod.category ? escapeHtml(prod.category.categoryname) : '<span class="text-muted">Chưa phân loại</span>';
            const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(prod.price || 0);

            const statusBadge = prod.status === 1 
                ? '<span class="badge-status-active"><i class="bi bi-record-fill fs-6"></i>Đang bán</span>' 
                : '<span class="badge-status-inactive"><i class="bi bi-record-fill fs-6"></i>Ngừng bán</span>';

            const row = `
                <tr>
                    <td class="text-center fw-bold text-secondary">#${prod.productId}</td>
                    <td class="text-center">${imgHtml}</td>
                    <td class="fw-bold text-dark fs-6">${escapeHtml(prod.productName)}</td>
                    <td><span class="badge bg-indigo-subtle text-indigo px-2.5 py-1.5 rounded-pill border border-indigo-subtle fw-semibold" style="background:#e0e7ff; color:#3730a3;">${catName}</span></td>
                    <td class="text-end fw-bold text-primary fs-6">${formattedPrice}</td>
                    <td class="text-center"><span class="badge bg-warning bg-opacity-20 text-warning-emphasis px-2 py-1 rounded">0%</span></td>
                    <td class="text-center fw-bold text-dark">${prod.stock || 0}</td>
                    <td class="text-center">${statusBadge}</td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-primary me-1 btn-edit" data-id="${prod.productId}" title="Sửa sản phẩm">
                            <i class="bi bi-pencil-square"></i> Sửa
                        </button>
                        <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${prod.productId}" data-name="${escapeHtml(prod.productName)}" title="Xóa sản phẩm">
                            <i class="bi bi-trash"></i> Xóa
                        </button>
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    }

    function renderPagination(pageData) {
        const totalPages = pageData.totalPages;
        const pageNumber = pageData.number;
        const totalElements = pageData.totalElements;

        if (totalElements === 0) {
            $('#pageInfoText').text('Không có dữ liệu');
            $('#paginationContainer').html('');
            return;
        }

        const startItem = pageNumber * currentPageSize + 1;
        const endItem = Math.min((pageNumber + 1) * currentPageSize, totalElements);
        $('#pageInfoText').html(`Hiển thị <b>${startItem}</b> - <b>${endItem}</b> / <b>${totalElements}</b> sản phẩm (Trang <b>${pageNumber + 1}</b>/<b>${totalPages}</b>)`);

        const pagination = $('#paginationContainer');
        pagination.empty();

        if (totalPages <= 1) return;

        const prevDisabled = pageData.first ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${prevDisabled}">
                <a class="page-link" href="#" onclick="changePage(${pageNumber - 1}); return false;" aria-label="Previous">
                    <i class="bi bi-chevron-left"></i>
                </a>
            </li>
        `);

        let startPage = Math.max(0, pageNumber - 2);
        let endPage = Math.min(totalPages - 1, pageNumber + 2);

        for (let i = startPage; i <= endPage; i++) {
            const active = i === pageNumber ? 'active' : '';
            pagination.append(`
                <li class="page-item ${active}">
                    <a class="page-link" href="#" onclick="changePage(${i}); return false;">${i + 1}</a>
                </li>
            `);
        }

        const nextDisabled = pageData.last ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${nextDisabled}">
                <a class="page-link" href="#" onclick="changePage(${pageNumber + 1}); return false;" aria-label="Next">
                    <i class="bi bi-chevron-right"></i>
                </a>
            </li>
        `);
    }

    function changePage(page) {
        if (page < 0) return;
        currentPage = page;
        loadData(currentPage, currentPageSize, currentKeyword, currentCategoryId);
    }

    function showAlert(type, message) {
        $('#alertBox').removeClass('d-none alert-success alert-danger alert-warning')
                      .addClass('alert-' + type);
        $('#alertMessage').text(message);
        setTimeout(function() {
            $('#alertBox').addClass('d-none');
        }, 4000);
    }

    function escapeHtml(text) {
        if (!text) return '';
        return text.replace(/&/g, "&amp;")
                   .replace(/</g, "&lt;")
                   .replace(/>/g, "&gt;")
                   .replace(/"/g, "&quot;")
                   .replace(/'/g, "&#039;");
    }
</script>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
