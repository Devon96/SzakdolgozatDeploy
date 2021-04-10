package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Comment;
import hu.utazo.utazo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
