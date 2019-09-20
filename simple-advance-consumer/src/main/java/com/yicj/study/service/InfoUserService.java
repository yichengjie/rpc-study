package com.yicj.study.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.yicj.study.vo.InfoUser;
import com.yicj.study.vo.Response;

public interface InfoUserService {
	List<InfoUser> insertInfoUser(InfoUser infoUser);
	Future<Response> insertInfoUserAsync(InfoUser infoUser);
    InfoUser getInfoUserById(String id);
    void deleteInfoUserById(String id);
    String getNameById(String id);
    Map<String,InfoUser> getAllUser();
}
