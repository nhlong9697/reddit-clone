package com.reddit.redditbackend.service;

import com.reddit.redditbackend.dto.SubredditDto;
import com.reddit.redditbackend.exception.SpringRedditException;
import com.reddit.redditbackend.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;

    public SubredditService(SubredditRepository subredditRepository, SubredditMapper subredditMapper) {
        this.subredditRepository = subredditRepository;
        this.subredditMapper = subredditMapper;
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit savedSubreddit =
                subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(savedSubreddit.getId());
        return subredditDto;
    }


    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }


    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit =
                subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No " +
                        "subreddit found"));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
