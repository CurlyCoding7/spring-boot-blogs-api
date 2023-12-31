package com.app.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.blog.entities.Category;
import com.app.blog.entities.Post;
import com.app.blog.entities.User;
import com.app.blog.exceptions.ResourceNotFoundException;
import com.app.blog.payloads.PostDto;
import com.app.blog.payloads.PostResponse;
import com.app.blog.repositories.CategoryRepo;
import com.app.blog.repositories.PostRepo;
import com.app.blog.repositories.UserRepo;
import com.app.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    //create post
    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User user = this.userRepo.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));  

        Category cat = this.categoryRepo.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));  

        Post post = this.modelMapper.map(postDto, Post.class);
        post.setImage("default.jpg");
        post.setAddedDate(new Date());

        post.setUser(user);
        post.setCategory(cat);

        Post addedPost = this.postRepo.save(post);
        return this.modelMapper.map(addedPost, PostDto.class);
    }

    // update post
    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = this.postRepo.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));  

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImage(postDto.getImage());

        Post updatedPost = this.postRepo.save(post);

        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    // delete post
    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepo.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));  
        this.postRepo.delete(post);
    
    }

    // get post by id
    @Override
    public PostDto getPost(Integer postId) {
        Post post = this.postRepo.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));       
        
        return this.modelMapper.map(post, PostDto.class);

    }

    // get all posts
    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        
       Sort sort = null;
       if(sortDir.equalsIgnoreCase("asc")){
        sort = Sort.by(sortBy).ascending();
       }
       else{
        sort = Sort.by(sortBy).descending();
       }

       Pageable p = PageRequest.of(pageNumber, pageSize, sort);
       Page<Post> pagePosts = this.postRepo.findAll(p);
       List<Post> posts = pagePosts.getContent();

       List<PostDto> postDtos = posts.stream()
       .map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

       PostResponse postResponse = new PostResponse();
       postResponse.setContent(postDtos);
       postResponse.setPageNumber(pagePosts.getNumber());
       postResponse.setPageSize(pagePosts.getSize());
       postResponse.setTotalElements(pagePosts.getTotalElements());
       postResponse.setTotalPages(pagePosts.getTotalPages());
       postResponse.setLastPage(pagePosts.isLast());

       return postResponse;
    }

    // get post by category
    @Override
    public List<PostDto> getPostByCategory(Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId)); 

        List<Post> posts = this.postRepo.findByCategory(cat);

        List<PostDto> postDtos = posts.stream()
       .map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        
       return postDtos;
    }

    // get posts by user
    @Override
    public List<PostDto> getPostByUser(Integer userId) {
        User user = this.userRepo.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId)); 

        List<Post> posts = this.postRepo.findByUser(user);

        List<PostDto> postDtos = posts.stream()
       .map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        
       return postDtos;
    }

    // search posts
    @Override
    public List<PostDto> searchPosts(String title) {
        List<Post> posts = this.postRepo.findByTitleContaining(title);
        
        List<PostDto> postDtos = posts.stream()
       .map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        
       return postDtos;

    }

}
