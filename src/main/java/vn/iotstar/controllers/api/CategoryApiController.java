package vn.iotstar.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IStorageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private vn.iotstar.repository.ProductRepository productRepository;

    @Autowired
    private IStorageService storageService;

    // 1. Phân trang & Tìm kiếm
    @GetMapping("/searchPaginated")
    public ResponseEntity<Response> searchPaginated(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "categoryId") String sortField) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
            Page<Category> result;
            if (StringUtils.hasText(name)) {
                result = categoryService.findByCategorynameContainingIgnoreCase(name.trim(), pageable);
            } else {
                result = categoryService.findAll(pageable);
            }
            return ResponseEntity.ok(new Response(true, "Lấy danh sách danh mục phân trang thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi lấy danh sách danh mục: " + e.getMessage(), null));
        }
    }

    // 2. Lấy tất cả danh mục
    @GetMapping("")
    public ResponseEntity<Response> getAllCategories() {
        try {
            List<Category> list = categoryService.findAll();
            return ResponseEntity.ok(new Response(true, "Lấy tất cả danh mục thành công", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi: " + e.getMessage(), null));
        }
    }

    // 3. Lấy chi tiết danh mục theo ID
    @RequestMapping(value = "/getCategory", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Response> getCategory(@RequestParam(name = "id", required = false) Integer id,
                                                @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        Integer catId = id != null ? id : categoryId;
        if (catId == null) {
            return ResponseEntity.badRequest().body(new Response(false, "Vui lòng cung cấp ID danh mục", null));
        }
        Optional<Category> opt = categoryService.findById(catId);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Lấy thông tin danh mục thành công", opt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy danh mục với ID: " + catId, null));
        }
    }

    // 4. Thêm mới danh mục
    @PostMapping("/addCategory")
    public ResponseEntity<Response> addCategory(
            @RequestParam(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "categoryname", required = false) String categorynameAlt,
            @RequestParam(name = "status", defaultValue = "1") int status,
            @RequestParam(name = "icon", required = false) MultipartFile icon,
            @RequestParam(name = "images", required = false) MultipartFile imagesAlt) {
        try {
            String name = StringUtils.hasText(categoryName) ? categoryName : categorynameAlt;
            if (!StringUtils.hasText(name)) {
                return ResponseEntity.badRequest().body(new Response(false, "Tên danh mục không được để trống", null));
            }

            MultipartFile file = (icon != null && !icon.isEmpty()) ? icon : imagesAlt;
            Category category = new Category();
            category.setCategoryname(name.trim());
            category.setStatus(status);

            if (file != null && !file.isEmpty()) {
                String storedFileName = storageService.getSorageFilename(file, "category");
                storageService.store(file, storedFileName);
                category.setImages(storedFileName);
            }

            Category savedCategory = categoryService.save(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response(true, "Thêm danh mục mới thành công", savedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi thêm danh mục: " + e.getMessage(), null));
        }
    }

    // 5. Cập nhật danh mục
    @RequestMapping(value = "/updateCategory", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Response> updateCategory(
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "categoryname", required = false) String categorynameAlt,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "icon", required = false) MultipartFile icon,
            @RequestParam(name = "images", required = false) MultipartFile imagesAlt) {
        try {
            Integer catId = categoryId != null ? categoryId : id;
            if (catId == null) {
                return ResponseEntity.badRequest().body(new Response(false, "Cần có categoryId để cập nhật", null));
            }

            Optional<Category> opt = categoryService.findById(catId);
            if (!opt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response(false, "Không tìm thấy danh mục để cập nhật", null));
            }

            Category category = opt.get();
            String name = StringUtils.hasText(categoryName) ? categoryName : categorynameAlt;
            if (StringUtils.hasText(name)) {
                category.setCategoryname(name.trim());
            }
            category.setStatus(status);

            MultipartFile file = (icon != null && !icon.isEmpty()) ? icon : imagesAlt;
            if (file != null && !file.isEmpty()) {
                if (category.getImages() != null) {
                    storageService.delete(category.getImages());
                }
                String storedFileName = storageService.getSorageFilename(file, "category_" + catId);
                storageService.store(file, storedFileName);
                category.setImages(storedFileName);
            }

            Category updatedCategory = categoryService.save(category);
            return ResponseEntity.ok(new Response(true, "Cập nhật danh mục thành công", updatedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi cập nhật danh mục: " + e.getMessage(), null));
        }
    }

    // 6. Xóa danh mục
    @DeleteMapping("/deleteCategory")
    public ResponseEntity<Response> deleteCategory(
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "id", required = false) Integer id) {
        try {
            Integer catId = categoryId != null ? categoryId : id;
            if (catId == null) {
                return ResponseEntity.badRequest().body(new Response(false, "Vui lòng truyền categoryId", null));
            }

            Optional<Category> opt = categoryService.findById(catId);
            if (!opt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response(false, "Không tìm thấy danh mục cần xóa", null));
            }

            Category cat = opt.get();
            if (productRepository.countByCategory_CategoryId(catId) > 0) {
                return ResponseEntity.badRequest()
                        .body(new Response(false, "Không thể xóa danh mục đang có sản phẩm", null));
            }

            if (cat.getImages() != null) {
                storageService.delete(cat.getImages());
            }
            categoryService.deleteById(catId);
            return ResponseEntity.ok(new Response(true, "Xóa danh mục thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi xóa danh mục: " + e.getMessage(), null));
        }
    }
}
