package vn.iotstar.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.Category;
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByCategorynameContainingIgnoreCaseAndStatus(String keyword, int status, Pageable pageable);
    Page<Category> findByCategorynameContainingIgnoreCase(String keyword, Pageable pageable);
    boolean existsByCategorynameIgnoreCase(String categoryname);
    boolean existsByCategorynameIgnoreCaseAndCategoryIdNot(String categoryname, int categoryId);
}
