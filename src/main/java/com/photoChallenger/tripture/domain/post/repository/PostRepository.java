package com.photoChallenger.tripture.domain.post.repository;

import com.photoChallenger.tripture.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByProfile_ProfileId(Long profileId, Pageable pageable);


}
