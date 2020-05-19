package com.simon.bolg_sys.dao;

import com.simon.bolg_sys.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
