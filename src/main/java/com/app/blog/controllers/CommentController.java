package com.app.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.payloads.APIResponse;
import com.app.blog.payloads.CommentDto;
import com.app.blog.services.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // create comment
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, 
    @PathVariable Integer postId
    ){

        CommentDto createdComment = this.commentService.createComment(commentDto, postId);
        return new ResponseEntity<CommentDto>(createdComment,HttpStatus.CREATED);
    }


    // delete comment
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<APIResponse> createComment(@PathVariable Integer commentId
    ){

        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(new APIResponse("Comment deleted!", true), HttpStatus.OK);
    }

}
