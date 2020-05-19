package com.simon.bolg_sys.dao;

import com.simon.bolg_sys.bean.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by limi on 2017/10/22.
 */
public interface ICommentDao extends JpaRepository<Comment,Long> {


    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
