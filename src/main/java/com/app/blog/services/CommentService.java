package com.app.blog.services;

import com.app.blog.payloads.CommentDto;

public interface CommentService {

    // create comment
    CommentDto createComment(CommentDto commentDto, Integer postId);

    // delete comment
    void deleteComment(Integer commentId);

}
