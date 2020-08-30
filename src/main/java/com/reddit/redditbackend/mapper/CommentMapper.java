package com.reddit.redditbackend.mapper;

import com.reddit.redditbackend.dto.CommentsDto;
import com.reddit.redditbackend.model.Comment;
import com.reddit.redditbackend.model.Post;
import com.reddit.redditbackend.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "appUser", source = "appUser")
    Comment map(CommentsDto commentsDto, Post post, AppUser appUser);

    //TODO: test post.id and user.username
    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}
