package com.simon.bolg_sys.controller;

import com.simon.bolg_sys.bean.Type;
import com.simon.bolg_sys.service.IBlogService;
import com.simon.bolg_sys.service.ITypeService;
import com.simon.bolg_sys.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by limi on 2017/10/23.
 */
@Controller
public class TypeShowController {

    @Autowired
    private ITypeService typeService;

    @Autowired
    private IBlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlog(1,pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "type";
    }
}
