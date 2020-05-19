package com.simon.bolg_sys.service;

import com.simon.bolg_sys.bean.User;

public interface IUserService {

    public User checkUser(String username, String password);
}
