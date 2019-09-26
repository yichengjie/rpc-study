package com.yicj.study.service.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import com.yicj.study.vo.Response;
import com.yicj.study.vo.User;

public interface IUserService {
	List<User> insertUser(User user);
	Future<Response> insertUserAsync(User infoUser);
	///////////////////////////////////////////
    User getUserById(String id);
    Future<Response> getUserByIdAsync(String id);
    ///////////////////////////////////////////
    void deleteUserById(String id);
    void deleteUserByIdAsync(String id);
    ///////////////////////////////////////////
    String getNameById(String id);
    String getNameByIdAsync(String id);
    ///////////////////////////////////////////
    Map<String,User> getAllUser();
    Map<String,User> getAllUserAsync();
}
