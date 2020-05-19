package com.simon.bolg_sys.service.impl;

import com.simon.bolg_sys.NotFoundException;
import com.simon.bolg_sys.bean.Type;
import com.simon.bolg_sys.dao.IBlogDao;
import com.simon.bolg_sys.dao.ITypeDao;
import com.simon.bolg_sys.service.IBlogService;
import com.simon.bolg_sys.service.ITypeService;
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

@Service
public class TypeServiceImpl implements ITypeService {
    @Autowired
    private ITypeDao typeRepository;
    @Autowired
    private IBlogDao iBlogDao;

    @org.springframework.transaction.annotation.Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }


    @Override
    public List<Type> listTypeTop(Integer size) {
//        List<String> list = new ArrayList<>();
//        list.add("blogs.size");
//        Sort sort = new Sort(Sort.Direction.DESC,list);
//        Pageable pageable = new PageRequest(0,size,Sort.by(Sort.Direction.DESC,"blogs.size"));
        List<Type> top = typeRepository.findTop(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size")));
        for (Type type : top) {
            if(type.getBlogs()!=null || type.getBlogs().size()>0){
                type.setBlogs(iBlogDao.findByPublishedTrueAndTypeId(type.getId()));
            }

        }
        return top;
    }


    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }



    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
