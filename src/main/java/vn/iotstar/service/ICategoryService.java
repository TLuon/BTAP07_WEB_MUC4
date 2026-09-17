package vn.iotstar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.iotstar.entity.Category;

public interface ICategoryService {
    <S extends Category> S save(S entity);
    
    Optional<Category> findById(Integer id);
    
    List<Category> findAll();
    
    Page<Category> findAll(Pageable pageable);
    
    void deleteById(Integer id);
    
    Page<Category> findByCategorynameContainingIgnoreCaseAndStatus(String keyword, int status, Pageable pageable);
    
    Page<Category> findByCategorynameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
}
