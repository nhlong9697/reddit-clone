package com.reddit.redditbackend.repository;

import com.reddit.redditbackend.model.Comment;
import com.reddit.redditbackend.model.Post;
import com.reddit.redditbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
