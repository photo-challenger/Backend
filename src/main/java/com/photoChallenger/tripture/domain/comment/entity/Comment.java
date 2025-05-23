package com.photoChallenger.tripture.domain.comment.entity;

import com.photoChallenger.tripture.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long commentId;

    @Column(columnDefinition = "LONGTEXT")
    private String commentContent;

    @Column(nullable = false)
    private LocalDateTime commentDate;

    @Column(updatable = false)
    private Long commentGroupId;

    @Column(nullable = false, updatable = false, columnDefinition = "TINYINT(1)")
    private Boolean nested;

    @Column(updatable = false, columnDefinition = "INT UNSIGNED")
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Comment(Long commentId, String commentContent, LocalDateTime commentDate, Long commentGroupId, Boolean nested, Long profileId) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
        this.commentGroupId = commentGroupId;
        this.nested = nested;
        this.profileId = profileId;
    }

    @Builder
    public static Comment create(Post post, Long commentId, String commentContent, LocalDateTime commentDate, Long commentGroupId, Boolean nested, Long profileId){
        Comment comment = new Comment(commentId, commentContent, commentDate, commentGroupId, nested, profileId);
        comment.addPost(post);
        return comment;
    }

    private void addPost(Post post){
        this.post = post;
        post.getComment().add(this);
    }

    public Comment update(String commentContent){
        this.commentContent = commentContent;
        this.commentDate = LocalDateTime.now();
        return this;
    }

    public void remove(Post post){
        post.getComment().remove(this);
    }
}
