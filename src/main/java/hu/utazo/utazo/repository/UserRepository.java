package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByVerificationCode(String code);

    @Query(" SELECT u FROM User u WHERE u.email = ?1 OR u.googleId = ?1 ")
    Optional<User> findByUsername(String username);

    @Query(" SELECT u FROM User u WHERE u.email = ?1 OR u.googleId = ?1 ")
    Optional<User> findByEmail(String email);

    @Query(" SELECT u FROM User u WHERE u.email LIKE ?1 OR u.username LIKE ?1 ORDER BY u.username")
    Page<User> findAllWithFilter(String search, PageRequest of);
}
