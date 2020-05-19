package com.simon.bolg_sys.service.impl;

import com.simon.bolg_sys.bean.User;
import com.simon.bolg_sys.dao.IUserDao;
import com.simon.bolg_sys.service.IUserService;
import com.simon.bolg_sys.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao dao ;
    @Override
    public User checkUser(String username, String password) {
        User user = dao.findByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }
}
