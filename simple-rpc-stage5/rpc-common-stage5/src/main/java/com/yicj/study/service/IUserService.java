package com.yicj.study.service;

import java.util.List;
import java.util.Map;

import com.yicj.study.vo.User;

public interface IUserService {
	List<User> insertUser(User user);
    User getUserById(String id);
    void deleteUserById(String id);
    String getNameById(String id);
    Map<String,User> getAllUser();
}
