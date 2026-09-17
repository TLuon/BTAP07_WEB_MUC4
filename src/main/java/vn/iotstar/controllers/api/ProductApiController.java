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
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    // 1. Phân trang & Tìm kiếm sản phẩm
    @GetMapping("/searchPaginated")
    public ResponseEntity<Response> searchPaginated(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "productId") String sortField) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
            Page<Product> result = productService.searchProduct(name, categoryId, pageable);
            return ResponseEntity.ok(new Response(true, "Lấy danh sách sản phẩm phân trang thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage(), null));
        }
    }

    // 2. Lấy tất cả sản phẩm
    @GetMapping("")
    public ResponseEntity<Response> getAllProducts() {
        try {
            List<Product> list = productService.findAll();
            return ResponseEntity.ok(new Response(true, "Lấy tất cả sản phẩm thành công", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi: " + e.getMessage(), null));
        }
    }

    // 3. Lấy chi tiết 1 sản phẩm theo ID
    @RequestMapping(value = "/getProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Response> getProduct(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "productId", required = false) Integer productId) {
        Integer prodId = id != null ? id : productId;
        if (prodId == null) {
            return ResponseEntity.badRequest().body(new Response(false, "Vui lòng truyền ID sản phẩm", null));
        }
        Optional<Product> opt = productService.findById(prodId);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Lấy thông tin sản phẩm thành công", opt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy sản phẩm với ID: " + prodId, null));
        }
    }

    // 4. Thêm mới sản phẩm
    @PostMapping("/addProduct")
    public ResponseEntity<Response> addProduct(
            @RequestParam(name = "productName") String productName,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "unitPrice", required = false) Double unitPrice,
            @RequestParam(name = "price", required = false) Double price,
            @RequestParam(name = "discount", required = false, defaultValue = "0") Double discount,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "stock", required = false) Integer stock,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(name = "images", required = false) MultipartFile imagesAlt) {
        try {
            if (!StringUtils.hasText(productName)) {
                return ResponseEntity.badRequest().body(new Response(false, "Tên sản phẩm không được rỗng", null));
            }

            Optional<Category> optCategory = categoryService.findById(categoryId);
            if (!optCategory.isPresent()) {
                return ResponseEntity.badRequest().body(new Response(false, "Danh mục không tồn tại", null));
            }

            Product product = new Product();
            product.setProductName(productName.trim());
            product.setDescription(description != null ? description.trim() : "");
            
            double finalPrice = (unitPrice != null) ? unitPrice : (price != null ? price : 0.0);
            product.setPrice(finalPrice);
            
            int finalStock = (quantity != null) ? quantity : (stock != null ? stock : 0);
            product.setStock(finalStock);
            
            product.setStatus(status);
            product.setCategory(optCategory.get());

            MultipartFile file = (imageFile != null && !imageFile.isEmpty()) ? imageFile : imagesAlt;
            if (file != null && !file.isEmpty()) {
                String filename = storageService.getSorageFilename(file, "product");
                storageService.store(file, filename);
                ProductImage img = new ProductImage(filename, product);
                product.getProductImages().add(img);
            }

            Product savedProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response(true, "Thêm mới sản phẩm thành công", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi thêm mới sản phẩm: " + e.getMessage(), null));
        }
    }

    // 5. Cập nhật sản phẩm
    @RequestMapping(value = "/updateProduct", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Response> updateProduct(
            @RequestParam(name = "productId", required = false) Integer productId,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "unitPrice", required = false) Double unitPrice,
            @RequestParam(name = "price", required = false) Double price,
            @RequestParam(name = "discount", required = false) Double discount,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "stock", required = false) Integer stock,
            @RequestParam(name = "status", required = false, defaultValue = "1") int status,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(name = "images", required = false) MultipartFile imagesAlt) {
        try {
            Integer prodId = productId != null ? productId : id;
            if (prodId == null) {
                return ResponseEntity.badRequest().body(new Response(false, "Vui lòng truyền productId để cập nhật", null));
            }

            Optional<Product> optProduct = productService.findById(prodId);
            if (!optProduct.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response(false, "Không tìm thấy sản phẩm cần cập nhật", null));
            }

            Product product = optProduct.get();

            if (StringUtils.hasText(productName)) {
                product.setProductName(productName.trim());
            }
            if (description != null) {
                product.setDescription(description.trim());
            }
            if (unitPrice != null) {
                product.setPrice(unitPrice);
            } else if (price != null) {
                product.setPrice(price);
            }
            if (quantity != null) {
                product.setStock(quantity);
            } else if (stock != null) {
                product.setStock(stock);
            }
            product.setStatus(status);

            if (categoryId != null) {
                Optional<Category> optCat = categoryService.findById(categoryId);
                optCat.ifPresent(product::setCategory);
            }

            MultipartFile file = (imageFile != null && !imageFile.isEmpty()) ? imageFile : imagesAlt;
            if (file != null && !file.isEmpty()) {
                // Delete previous images if present
                if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
                    for (ProductImage oldImg : product.getProductImages()) {
                        storageService.delete(oldImg.getImagePath());
                    }
                    product.getProductImages().clear();
                }
                String filename = storageService.getSorageFilename(file, "product_" + prodId);
                storageService.store(file, filename);
                ProductImage img = new ProductImage(filename, product);
                product.getProductImages().add(img);
            }

            Product updatedProduct = productService.save(product);
            return ResponseEntity.ok(new Response(true, "Cập nhật sản phẩm thành công", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), null));
        }
    }

    // 6. Xóa sản phẩm
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<Response> deleteProduct(
            @RequestParam(name = "productId", required = false) Integer productId,
            @RequestParam(name = "id", required = false) Integer id) {
        try {
            Integer prodId = productId != null ? productId : id;
            if (prodId == null) {
                return ResponseEntity.badRequest().body(new Response(false, "Vui lòng truyền productId", null));
            }

            Optional<Product> optProduct = productService.findById(prodId);
            if (!optProduct.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response(false, "Không tìm thấy sản phẩm cần xóa", null));
            }

            Product product = optProduct.get();
            if (product.getProductImages() != null) {
                for (ProductImage img : product.getProductImages()) {
                    storageService.delete(img.getImagePath());
                }
            }

            productService.deleteById(prodId);
            return ResponseEntity.ok(new Response(true, "Xóa sản phẩm thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, "Lỗi khi xóa sản phẩm: " + e.getMessage(), null));
        }
    }
}
