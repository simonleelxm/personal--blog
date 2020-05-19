package com.simon.bolg_sys.service;

import com.simon.bolg_sys.bean.Comment;

import java.util.List;


public interface ICommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
