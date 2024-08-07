package com.photoChallenger.tripture.domain.comment.repository;

import com.photoChallenger.tripture.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.commentGroupId = :commentId AND c.nested = true")
    List<Comment> findAllNestedCommentByCommentId(@Param("commentId") Long commentId);
}
