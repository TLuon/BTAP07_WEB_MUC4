package vn.iotstar.repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.iotstar.entity.User;
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, int id);
    boolean existsByUsernameIgnoreCaseAndIdNot(String username, int id);
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullnameContainingIgnoreCase(String a, String b, String c, Pageable pageable);
}
