package com.yicj.study.service;

import java.util.List;
import java.util.Map;

import com.yicj.study.vo.InfoUser;

public interface InfoUserService {
	List<InfoUser> insertInfoUser(InfoUser infoUser);
    InfoUser getInfoUserById(String id);
    void deleteInfoUserById(String id);
    String getNameById(String id);
    Map<String,InfoUser> getAllUser();
}
