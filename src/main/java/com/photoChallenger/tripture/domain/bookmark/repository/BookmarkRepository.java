package com.photoChallenger.tripture.domain.bookmark.repository;

import com.photoChallenger.tripture.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
