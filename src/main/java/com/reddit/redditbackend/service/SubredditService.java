package com.reddit.redditbackend.service;

import com.reddit.redditbackend.dto.SubredditDto;
import com.reddit.redditbackend.model.Subreddit;
import com.reddit.redditbackend.repository.SubredditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;

    public SubredditService(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit savedSubreddit = subredditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(savedSubreddit.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        Subreddit subreddit = new Subreddit();
        subreddit.setName(subredditDto.getName());
        subreddit.setDescription(subredditDto.getDescription());
        return subreddit;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        SubredditDto subredditDto = new SubredditDto();
        subredditDto.setName(subreddit.getName());
        subredditDto.setId(subreddit.getId());
        subredditDto.setNumberOfPosts(subreddit.getPosts().size());
        return subredditDto;
    }

}
