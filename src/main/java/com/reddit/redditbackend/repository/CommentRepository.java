package com.reddit.redditbackend.repository;

import com.reddit.redditbackend.model.AppUser;
import com.reddit.redditbackend.model.Comment;
import com.reddit.redditbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByAppUser(AppUser appUser);
}
