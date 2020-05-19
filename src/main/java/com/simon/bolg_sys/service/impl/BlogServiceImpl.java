package com.simon.bolg_sys.service.impl;

import com.simon.bolg_sys.NotFoundException;
import com.simon.bolg_sys.bean.Blog;
import com.simon.bolg_sys.bean.Type;
import com.simon.bolg_sys.dao.IBlogDao;
import com.simon.bolg_sys.service.IBlogService;
import com.simon.bolg_sys.utils.MarkdownUtils;
import com.simon.bolg_sys.utils.MyBeanUtils;
import com.simon.bolg_sys.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements IBlogService {


    @Autowired
    private IBlogDao iBlogDao;

    @Override
    public Blog getBlog(Long id) {
        return iBlogDao.findById(id).get();
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = iBlogDao.findById(id).get();
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
//        System.out.println(b);
//        System.out.println("------------------------------------------------");
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
//        System.out.println(MarkdownUtils.markdownToHtmlExtensions(content));
        iBlogDao.updateViews(id);
        return b;
    }

//    重新掌握内容
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return iBlogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Integer flag,Pageable pageable, BlogQuery blog) {
        return iBlogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                predicates.add(cb.equal(root.<Boolean>get("published"),true));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
//        return iBlogDao.findAll(pageable);
        return  iBlogDao.findByPublishedTrue(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return iBlogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                Join join = root.join("tags");
                predicates.add(cb.equal(join.get("id"),tagId));
                predicates.add(cb.equal(root.<Boolean>get("published"),true));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;

            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return iBlogDao.findByQuery(query,pageable,1);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
//        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
//        Pageable pageable = new PageRequest(0, size, sort);
        return iBlogDao.findTop(PageRequest.of(0,size,Sort.Direction.DESC,"updateTime"));
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = iBlogDao.findGroupYear();
        System.out.println(years);
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, iBlogDao.findByYear(year));
            System.out.println("循环"+year);
        }
        System.out.println(map);
        return map;
    }

    @Override
    public Long countBlog() {
        return iBlogDao.countByPublishedTrue();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return iBlogDao.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = iBlogDao.findById(id).get();
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return iBlogDao.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        iBlogDao.deleteById(id);
    }
}
