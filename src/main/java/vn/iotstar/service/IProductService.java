package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Product;

public interface IProductService {
    <S extends Product> S save(S entity);
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(Integer id);
    void deleteById(Integer id);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    Page<Product> searchProduct(String name, Integer categoryId, Pageable pageable);
}
