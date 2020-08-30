package com.reddit.redditbackend.controller;

import com.reddit.redditbackend.dto.VoteDto;
import com.reddit.redditbackend.exception.SpringRedditException;
import com.reddit.redditbackend.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ExceptionHandler(SpringRedditException.class)
    public ResponseEntity<String> handleSpringRedditException(SpringRedditException e) {
        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.I_AM_A_TEAPOT);
    }
}
