package vn.iotstar.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iotstar.entity.Product;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public <S extends Product> S save(S entity) {
        return productRepository.save(entity);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Page<Product> findByProductNameContaining(String name, Pageable pageable) {
        return productRepository.findByProductNameContaining(name, pageable);
    }

    @Override
    public Page<Product> searchProduct(String name, Integer categoryId, Pageable pageable) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasCategory = categoryId != null && categoryId > 0;

        if (hasName && hasCategory) {
            return productRepository.findByProductNameContainingIgnoreCaseAndCategory_CategoryId(name.trim(), categoryId, pageable);
        } else if (hasName) {
            return productRepository.findByProductNameContainingIgnoreCase(name.trim(), pageable);
        } else if (hasCategory) {
            return productRepository.findByCategory_CategoryId(categoryId, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }
}
