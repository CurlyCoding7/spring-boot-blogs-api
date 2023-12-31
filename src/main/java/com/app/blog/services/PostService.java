package com.app.blog.services;

import java.util.List;

import com.app.blog.payloads.PostDto;
import com.app.blog.payloads.PostResponse;

public interface PostService {

    //create
    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    //update
    PostDto updatePost(PostDto postDto, Integer postId);

    //delete
    void deletePost(Integer postId);

    //get
    PostDto getPost(Integer postId);

    //get all posts
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //get all posts by category
    List<PostDto> getPostByCategory(Integer categoryId);

    //get all posts by user
    List<PostDto> getPostByUser(Integer userId);

    //search posts
    List<PostDto> searchPosts(String keyword);


}
