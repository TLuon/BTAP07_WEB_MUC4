package vn.iotstar.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminAjaxViewController {

    @GetMapping("/categories/ajax")
    public String categoryAjaxView() {
        return "admin/category-ajax";
    }

    @GetMapping("/products/ajax")
    public String productAjaxView() {
        return "admin/product-ajax";
    }
}
