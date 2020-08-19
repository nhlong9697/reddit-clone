package com.reddit.redditbackend.mapper;

import com.reddit.redditbackend.dto.CommentsDto;
import com.reddit.redditbackend.model.Comment;
import com.reddit.redditbackend.model.Post;
import com.reddit.redditbackend.model.User;
import org.mapstruct.Mapping;

public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentsDto commentsDto, Post post, User user);

    //TODO: test post.id and user.username
    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}
