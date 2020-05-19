package com.simon.bolg_sys.dao;

import com.simon.bolg_sys.bean.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITagDao extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    //    @Query("SELECT new com.simon.bolg_sys.bean.Tag(b,a) FROM Blog b, Tag a WHERE b.published = true ")
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);


}
