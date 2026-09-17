package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;
import vn.iotstar.mapper.ProductMapper;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.UploadService;

@Controller
@RequestMapping("/th/admin/products")
public class AdminProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private UploadService uploadService;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(5);

            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("productName"));
            Page<Product> resultPage = null;

            if (StringUtils.hasText(name)) {
                resultPage = productService.findByProductNameContainingIgnoreCase(name, pageable);
                model.addAttribute("name", name);
            } else {
                resultPage = productService.findAll(pageable);
            }

            int totalPages = resultPage.getTotalPages();
            if (totalPages > 0) {
                int start = Math.max(1, currentPage - 2);
                int end = Math.min(currentPage + 2, totalPages);
                if (totalPages > 5) {
                    if (end == totalPages) start = end - 5;
                    else if (start == 1) end = start + 5;
                }
                List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }

            model.addAttribute("productPage", resultPage);
            return "admin/product/list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "admin/product/list";
        }
    }

    @GetMapping("/add")
    public String add(ModelMap model) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setStatus(1); // default active
        model.addAttribute("product", productDTO);
        return "admin/product/form";
    }

    @GetMapping("/edit/{productId}")
    public String edit(ModelMap model, @PathVariable("productId") Integer productId) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isPresent()) {
            ProductDTO productDTO = ProductMapper.toDTO(optProduct.get());
            model.addAttribute("product", productDTO);
            return "admin/product/form";
        }
        model.addAttribute("message", "Sản phẩm không tồn tại");
        return "redirect:/th/admin/products";
    }

    @PostMapping("/save")
    public String saveOrUpdate(@ModelAttribute("product") ProductDTO productDTO,
                               org.springframework.validation.BindingResult result,
                               ModelMap model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("product", productDTO);
            return "admin/product/form";
        }
        try {
            Product entity = ProductMapper.toEntity(productDTO);
            
            // handle image upload
            if (productDTO.getImageFiles() != null && !productDTO.getImageFiles().isEmpty()) {
                for (org.springframework.web.multipart.MultipartFile file : productDTO.getImageFiles()) {
                    if (file != null && !file.isEmpty()) {
                        String uploadedFile = uploadService.save(file, "products");
                        if (uploadedFile != null) {
                            entity.getProductImages().add(new ProductImage(uploadedFile, entity));
                        }
                    }
                }
            } 
            
            // keep old images if no new images uploaded or append mode
            if (productDTO.getProductId() > 0) {
                Optional<Product> existProduct = productService.findById(productDTO.getProductId());
                if (existProduct.isPresent()) {
                    // we append new images to existing ones
                    for (ProductImage oldImage : existProduct.get().getProductImages()) {
                        // check if already added to avoid duplicates if any logic was added later
                        boolean exists = entity.getProductImages().stream().anyMatch(img -> img.getImagePath().equals(oldImage.getImagePath()));
                        if(!exists) {
                            entity.getProductImages().add(new ProductImage(oldImage.getImagePath(), entity));
                        }
                    }
                }
            }
            
            productService.save(entity);
            redirectAttributes.addFlashAttribute("message", "Lưu Sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi lưu Sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/th/admin/products";
    }

    @GetMapping("/delete/{productId}")
    public String delete(@PathVariable("productId") Integer productId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Product> optProduct = productService.findById(productId);
            if (optProduct.isPresent()) {
                productService.deleteById(productId);
                redirectAttributes.addFlashAttribute("message", "Đã xóa Sản phẩm!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xóa Sản phẩm (có thể do ràng buộc dữ liệu)!");
        }
        return "redirect:/th/admin/products";
    }
}
