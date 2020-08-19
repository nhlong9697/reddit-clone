package com.reddit.redditbackend.repository;

import com.reddit.redditbackend.model.Post;
import com.reddit.redditbackend.model.Subreddit;
import com.reddit.redditbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}