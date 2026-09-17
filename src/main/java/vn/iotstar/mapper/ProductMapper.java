package vn.iotstar.mapper;

import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Category;
import java.util.stream.Collectors;

import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductImage;

public class ProductMapper {

    // Chuyển từ DTO sang Entity
    public static Product toEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setStatus(dto.getStatus());
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(dto.getCategoryId());
            entity.setCategory(category);
        }
        return entity;
    }

    // Chuyển từ Entity sang DTO
    public static ProductDTO toDTO(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setStatus(entity.getStatus());
        
        if (entity.getProductImages() != null) {
            dto.setImagePaths(entity.getProductImages().stream()
                    .map(ProductImage::getImagePath)
                    .collect(Collectors.toList()));
        }
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getCategoryId());
        }
        return dto;
    }
}
