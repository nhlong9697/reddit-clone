package com.reddit.redditbackend.service;

import com.reddit.redditbackend.dto.CommentsDto;
import com.reddit.redditbackend.exception.PostNotFoundException;
import com.reddit.redditbackend.mapper.CommentMapper;
import com.reddit.redditbackend.model.Comment;
import com.reddit.redditbackend.model.NotificationEmail;
import com.reddit.redditbackend.model.Post;
import com.reddit.redditbackend.model.User;
import com.reddit.redditbackend.repository.CommentRepository;
import com.reddit.redditbackend.repository.PostRepository;
import com.reddit.redditbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public CommentService(PostRepository postRepository, UserRepository userRepository, AuthService authService, CommentMapper commentMapper, CommentRepository commentRepository, MailContentBuilder mailContentBuilder, MailService mailService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.mailContentBuilder = mailContentBuilder;
        this.mailService = mailService;
    }

    public Comment save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());

        String message =  mailContentBuilder.build(post.getUser().getUsername() + " posted a " +
                "comment on your post" + POST_URL);
        sendCommentNotificcation(message, post.getUser());
        return commentRepository.save(comment);
    }

    private void sendCommentNotificcation(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + "Commented your post",
                user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository
                .findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user =
                userRepository.findByUsername(userName)
                        .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.
                findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
