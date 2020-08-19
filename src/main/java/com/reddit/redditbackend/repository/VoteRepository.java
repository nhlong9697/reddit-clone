package com.reddit.redditbackend.repository;

import com.reddit.redditbackend.model.Post;
import com.reddit.redditbackend.model.User;
import com.reddit.redditbackend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}