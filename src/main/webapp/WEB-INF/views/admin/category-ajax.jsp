<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="title" value="Quản lý Danh mục (AJAX)" />
</jsp:include>

<div class="container-fluid px-0">
    <!-- Top Hero Banner -->
    <div class="page-banner mb-4">
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div>
                <div class="badge bg-white bg-opacity-20 text-white px-3 py-2 rounded-pill mb-2 fw-semibold">
                    <i class="bi bi-cpu-fill me-1"></i> RESTful API & AJAX Dynamic
                </div>
                <h2 class="mb-1 fw-extrabold"><i class="bi bi-grid-3x3-gap-fill me-2"></i>Quản Lý Danh Mục Sản Phẩm</h2>
                <p class="mb-0">Thao tác CRUD, Tìm kiếm và Phân trang thời gian thực không tải lại trang</p>
            </div>
            <div>
                <button id="btnOpenAddModal" class="btn btn-success btn-lg shadow">
                    <i class="bi bi-plus-circle-fill me-2"></i>Thêm Danh Mục Mới
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

    <!-- Toolbar Filter Card -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-3 bg-white rounded-3">
            <form id="searchForm" class="row g-3 align-items-center" onsubmit="return false;">
                <div class="col-md-6 col-lg-7">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-search"></i></span>
                        <input type="text" id="searchKeyword" class="form-control border-start-0" placeholder="Nhập tên danh mục cần tìm kiếm...">
                    </div>
                </div>
                <div class="col-md-3 col-lg-3">
                    <div class="input-group">
                        <label class="input-group-text bg-light text-muted fw-bold" for="pageSize"><i class="bi bi-list-numeric me-1"></i>Hiển thị</label>
                        <select id="pageSize" class="form-select">
                            <option value="5" selected>5 mục / trang</option>
                            <option value="10">10 mục / trang</option>
                            <option value="20">20 mục / trang</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-3 col-lg-2 d-flex gap-2">
                    <button type="button" id="btnSearch" class="btn btn-primary flex-fill">
                        <i class="bi bi-search me-1"></i> Tìm kiếm
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
                <table class="table table-hover align-middle mb-0" id="categoryTable">
                    <thead>
                        <tr>
                            <th scope="col" class="text-center" style="width: 80px;">ID</th>
                            <th scope="col" class="text-center" style="width: 110px;">Icon / Ảnh</th>
                            <th scope="col">Tên danh mục</th>
                            <th scope="col" class="text-center" style="width: 160px;">Trạng thái</th>
                            <th scope="col" class="text-center" style="width: 180px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="categoryTableBody">
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

<!-- Category Modal (Add / Edit) -->
<div class="modal fade" id="categoryModal" tabindex="-1" aria-labelledby="categoryModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title fw-bold" id="categoryModalLabel">
                    <i class="bi bi-folder-plus me-2"></i>Thêm Danh Mục
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="categoryForm" enctype="multipart/form-data">
                <div class="modal-body p-4">
                    <input type="hidden" id="categoryId" name="categoryId" value="">

                    <div class="mb-3">
                        <label for="categoryName" class="form-label fw-bold">Tên Danh Mục <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="categoryName" name="categoryName" placeholder="Nhập tên danh mục..." required>
                    </div>

                    <div class="mb-3">
                        <label for="status" class="form-label fw-bold">Trạng Thái</label>
                        <select class="form-select" id="status" name="status">
                            <option value="1" selected>Hoạt động</option>
                            <option value="0">Khóa / Thấy ẩn</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="icon" class="form-label fw-bold">Ảnh Icon Danh Mục</label>
                        <input type="file" class="form-control" id="icon" name="icon" accept="image/*">
                        <div class="form-text">Chọn ảnh mới nếu muốn thay đổi. Giữ nguyên nếu không tải ảnh mới.</div>
                    </div>

                    <div class="text-center mt-3 d-none" id="previewContainer">
                        <p class="small text-muted mb-1 fw-bold">Xem trước ảnh:</p>
                        <img id="iconPreview" src="" alt="Icon Preview" class="img-preview-thumb rounded shadow-sm" style="width: 100px; height: 100px; object-fit: contain;">
                    </div>
                </div>
                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">
                        <i class="bi bi-x-circle me-1"></i>Hủy
                    </button>
                    <button type="submit" class="btn btn-primary px-4" id="btnSaveCategory">
                        <i class="bi bi-check-circle-fill me-1"></i>Lưu Dữ Liệu
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

    $(document).ready(function() {
        loadData(currentPage, currentPageSize, currentKeyword);

        $('#btnSearch').click(function() {
            currentKeyword = $('#searchKeyword').val().trim();
            currentPageSize = parseInt($('#pageSize').val());
            currentPage = 0;
            loadData(currentPage, currentPageSize, currentKeyword);
        });

        $('#searchKeyword').keypress(function(e) {
            if (e.which === 13) {
                $('#btnSearch').click();
            }
        });

        $('#pageSize').change(function() {
            currentPageSize = parseInt($(this).val());
            currentPage = 0;
            loadData(currentPage, currentPageSize, currentKeyword);
        });

        $('#btnReset').click(function() {
            $('#searchKeyword').val('');
            currentKeyword = '';
            currentPage = 0;
            loadData(currentPage, currentPageSize, currentKeyword);
        });

        $('#btnOpenAddModal').click(function() {
            $('#categoryForm')[0].reset();
            $('#categoryId').val('');
            $('#categoryModalLabel').html('<i class="bi bi-folder-plus me-2"></i>Thêm Danh Mục Mới');
            $('#previewContainer').addClass('d-none');
            $('#categoryModal').modal('show');
        });

        $('#icon').change(function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    $('#iconPreview').attr('src', e.target.result);
                    $('#previewContainer').removeClass('d-none');
                };
                reader.readAsDataURL(file);
            }
        });

        $('#categoryForm').submit(function(e) {
            e.preventDefault();

            const categoryIdVal = $('#categoryId').val();
            const isEdit = categoryIdVal && parseInt(categoryIdVal) > 0;
            const apiUrl = isEdit ? CONTEXT_PATH + "/api/category/updateCategory" : CONTEXT_PATH + "/api/category/addCategory";
            const apiMethod = isEdit ? "PUT" : "POST";

            const formData = new FormData(this);

            $('#btnSaveCategory').prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-1"></span>Đang xử lý...');

            $.ajax({
                url: apiUrl,
                type: apiMethod,
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    $('#btnSaveCategory').prop('disabled', false).html('<i class="bi bi-check-circle-fill me-1"></i>Lưu Dữ Liệu');
                    if (response.status) {
                        $('#categoryModal').modal('hide');
                        showAlert("success", response.message || "Thao tác thành công!");
                        loadData(currentPage, currentPageSize, currentKeyword);
                    } else {
                        showAlert("danger", response.message || "Có lỗi xảy ra!");
                    }
                },
                error: function(xhr) {
                    $('#btnSaveCategory').prop('disabled', false).html('<i class="bi bi-check-circle-fill me-1"></i>Lưu Dữ Liệu');
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
                url: CONTEXT_PATH + "/api/category/getCategory?id=" + id,
                type: "POST",
                success: function(response) {
                    if (response.status && response.body) {
                        const cat = response.body;
                        $('#categoryId').val(cat.categoryId);
                        $('#categoryName').val(cat.categoryname);
                        $('#status').val(cat.status);

                        if (cat.images) {
                            const imgSrc = cat.images.startsWith('http') ? cat.images : CONTEXT_PATH + "/uploads/" + cat.images;
                            $('#iconPreview').attr('src', imgSrc);
                            $('#previewContainer').removeClass('d-none');
                        } else {
                            $('#previewContainer').addClass('d-none');
                        }

                        $('#categoryModalLabel').html('<i class="bi bi-pencil-square me-2"></i>Cập Nhật Danh Mục #' + cat.categoryId);
                        $('#categoryModal').modal('show');
                    } else {
                        showAlert("danger", response.message || "Không tìm thấy dữ liệu danh mục!");
                    }
                },
                error: function() {
                    showAlert("danger", "Lỗi khi lấy thông tin danh mục!");
                }
            });
        });

        $(document).on('click', '.btn-delete', function() {
            const id = $(this).data('id');
            const name = $(this).data('name');
            const row = $(this).closest('tr');

            if (confirm("Bạn có chắc chắn muốn xóa danh mục '" + name + "' (ID: " + id + ")?")) {
                $.ajax({
                    url: CONTEXT_PATH + "/api/category/deleteCategory?categoryId=" + id,
                    type: "DELETE",
                    success: function(response) {
                        if (response.status) {
                            row.fadeOut(400, function() {
                                showAlert("success", response.message || "Đã xóa danh mục thành công!");
                                loadData(currentPage, currentPageSize, currentKeyword);
                            });
                        } else {
                            showAlert("danger", response.message || "Không thể xóa danh mục này!");
                        }
                    },
                    error: function(xhr) {
                        let msg = "Lỗi khi thực hiện xóa danh mục!";
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            msg = xhr.responseJSON.message;
                        }
                        showAlert("danger", msg);
                    }
                });
            }
        });
    });

    function loadData(page, size, keyword) {
        $('#categoryTableBody').html('<tr><td colspan="5" class="text-center py-5"><div class="spinner-border text-primary role="status"></div><p class="mt-2 mb-0 text-muted fw-semibold">Đang tải danh sách danh mục...</p></td></tr>');

        let url = CONTEXT_PATH + "/api/category/searchPaginated?page=" + page + "&size=" + size + "&sort=categoryId";
        if (keyword && keyword.length > 0) {
            url += "&name=" + encodeURIComponent(keyword);
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
                    $('#categoryTableBody').html('<tr><td colspan="5" class="text-center text-muted py-5"><i class="bi bi-inbox fs-2 d-block mb-2 text-secondary"></i>Không tìm thấy danh mục nào phù hợp.</td></tr>');
                    $('#pageInfoText').text('');
                    $('#paginationContainer').html('');
                }
            },
            error: function(xhr) {
                $('#categoryTableBody').html('<tr><td colspan="5" class="text-center text-danger py-5"><i class="bi bi-exclamation-triangle fs-2 d-block mb-2"></i>Không thể tải dữ liệu danh mục!</td></tr>');
            }
        });
    }

    function renderTable(pageData) {
        const content = pageData.content;
        const tbody = $('#categoryTableBody');
        tbody.empty();

        if (!content || content.length === 0) {
            tbody.html('<tr><td colspan="5" class="text-center text-muted py-5"><i class="bi bi-inbox fs-2 d-block mb-2 text-secondary"></i>Không có dữ liệu danh mục.</td></tr>');
            return;
        }

        $.each(content, function(index, cat) {
            let imgHtml = '<span class="badge bg-light text-muted border">Không có ảnh</span>';
            if (cat.images) {
                const imgSrc = cat.images.startsWith('http') ? cat.images : CONTEXT_PATH + "/uploads/" + cat.images;
                imgHtml = `<img src="${imgSrc}" class="img-preview-thumb" onerror="this.src='https://placehold.co/60x60?text=No+Img'">`;
            }

            const statusBadge = cat.status === 1 
                ? '<span class="badge-status-active"><i class="bi bi-record-fill fs-6"></i>Hoạt động</span>' 
                : '<span class="badge-status-inactive"><i class="bi bi-record-fill fs-6"></i>Đã khóa</span>';

            const row = `
                <tr>
                    <td class="text-center fw-bold text-secondary">#${cat.categoryId}</td>
                    <td class="text-center">${imgHtml}</td>
                    <td class="fw-bold text-dark fs-6">${escapeHtml(cat.categoryname)}</td>
                    <td class="text-center">${statusBadge}</td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-primary me-1 btn-edit" data-id="${cat.categoryId}" title="Chỉnh sửa danh mục">
                            <i class="bi bi-pencil-square"></i> Sửa
                        </button>
                        <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${cat.categoryId}" data-name="${escapeHtml(cat.categoryname)}" title="Xóa danh mục">
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
        $('#pageInfoText').html(`Hiển thị <b>${startItem}</b> - <b>${endItem}</b> / <b>${totalElements}</b> danh mục (Trang <b>${pageNumber + 1}</b>/<b>${totalPages}</b>)`);

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
        loadData(currentPage, currentPageSize, currentKeyword);
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
