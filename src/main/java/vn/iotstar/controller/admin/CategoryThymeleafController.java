package vn.iotstar.controller.admin;

import java.util.Optional;
import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.UploadService;

@Controller
@RequestMapping("/th/admin/categories")
public class CategoryThymeleafController {

    @Autowired
    private ICategoryService categoryService;
    
    @Autowired
    private UploadService uploadService;

    @GetMapping("")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(3);
            
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryname"));
            Page<Category> resultPage = null;
            
            if (StringUtils.hasText(name)) {
                resultPage = categoryService.findByCategorynameContainingIgnoreCase(name, pageable);
                model.addAttribute("name", name);
            } else {
                resultPage = categoryService.findAll(pageable);
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
            
            model.addAttribute("categoryPage", resultPage);
            return "admin/category/list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Exception: " + e.getMessage() + " | Cause: " + e.getCause());
            // return to a simple view or just return the layout with error
            return "admin/category/list";
        }
    }

    @GetMapping("/add")
    public String add(ModelMap model) {
        Category category = new Category();
        category.setStatus(1); // default active
        model.addAttribute("category", category);
        return "admin/category/form";
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(ModelMap model, @PathVariable("categoryId") Integer categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            model.addAttribute("category", optCategory.get());
            return "admin/category/form";
        }
        model.addAttribute("message", "Category không tồn tại");
        return "redirect:/th/admin/categories";
    }

    @PostMapping("/save")
    public String saveOrUpdate(@ModelAttribute("category") Category category,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            // handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadedFile = uploadService.save(imageFile);
                if (uploadedFile != null) {
                    category.setImages(uploadedFile);
                }
            } else {
                if (category.getCategoryId() > 0) {
                    Optional<Category> existCategory = categoryService.findById(category.getCategoryId());
                    if (existCategory.isPresent()) {
                        category.setImages(existCategory.get().getImages());
                    }
                }
            }
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("message", "Lưu Category thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi lưu Category!");
            e.printStackTrace();
        }
        return "redirect:/th/admin/categories";
    }

    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Integer categoryId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Category> optCategory = categoryService.findById(categoryId);
            if(optCategory.isPresent()) {
                // Here we might want to do soft delete or hard delete. Let's do hard delete for now.
                categoryService.deleteById(categoryId);
                redirectAttributes.addFlashAttribute("message", "Đã xóa Category!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xóa Category (có thể do ràng buộc dữ liệu)!");
        }
        return "redirect:/th/admin/categories";
    }
}
