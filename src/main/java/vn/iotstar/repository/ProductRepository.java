package vn.iotstar.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.Product;
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByProductNameContainingIgnoreCaseAndStatus(String keyword, int status, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
    long countByCategory_CategoryId(int categoryId);
}
