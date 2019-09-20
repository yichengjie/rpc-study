package com.yicj.study.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.yicj.study.service.IUserService;
import com.yicj.study.vo.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceImpl implements IUserService {

	private Map<String, User> userMap = new ConcurrentHashMap<>();

	@Override
	public List<User> insertUser(User user) {
		
		log.info("新增用户信息:{}", user);
		userMap.put(user.getId(), user);
		return getUserList();
	}

	@Override
	public User getUserById(String id) {
		User infoUser = userMap.get(id);
		log.info("查询用户ID:{}", id);
		return infoUser;
	}

	@Override
	public void deleteUserById(String id) {
		log.info("删除用户信息:{}", userMap.remove(id));
	}

	@Override
	public String getNameById(String id) {
		log.info("根据ID查询用户名称:{}", id);
		return userMap.get(id).getName();
	}

	@Override
	public Map<String, User> getAllUser() {
		log.info("查询所有用户信息{}", userMap);
		return userMap;
	}

	public List<User> getUserList() {
		List<User> userList = new ArrayList<>();
		Iterator<Map.Entry<String, User>> iterator = userMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, User> next = iterator.next();
			userList.add(next.getValue());
		}
		log.info("返回用户信息记录:{}", userList);
		return userList;
	}

}
