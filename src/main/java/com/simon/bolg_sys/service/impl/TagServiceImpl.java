package com.simon.bolg_sys.service.impl;

import com.simon.bolg_sys.NotFoundException;
import com.simon.bolg_sys.bean.Tag;
import com.simon.bolg_sys.bean.Type;
import com.simon.bolg_sys.dao.IBlogDao;
import com.simon.bolg_sys.dao.ITagDao;
import com.simon.bolg_sys.service.ITagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */
@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private ITagDao tagRepository;
    @Autowired
    private IBlogDao iBlogDao;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
//        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
//        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size"));
        List<Tag> top = tagRepository.findTop(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size")));
        for (Tag type : top) {
            if(type.getBlogs()!=null || type.getBlogs().size()>0){
//                type.setBlogs(iBlogDao.findByPublishedTrueAndTagId(type.getId()));
            }

        }
        return top;
    }


    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }


    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.findById(id).get();
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }



    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
