package com.yicj.study.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.annotation.RpcService;
import com.yicj.study.service.InfoUserService;
import com.yicj.study.vo.InfoUser;

@RpcService
public class InfoUserServiceImpl implements InfoUserService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String,InfoUser> infoUserMap = new ConcurrentHashMap<>();

    public List<InfoUser> insertInfoUser(InfoUser infoUser) {
        logger.info("新增用户信息:{}", infoUser);
        infoUserMap.put(infoUser.getId(),infoUser);
        return getInfoUserList();
    }

    public InfoUser getInfoUserById(String id) {
        InfoUser infoUser = infoUserMap.get(id);
        logger.info("查询用户ID:{}",id);
        return infoUser;
    }

    public List<InfoUser> getInfoUserList() {
        List<InfoUser> userList = new ArrayList<>();
        Iterator<Map.Entry<String, InfoUser>> iterator = infoUserMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, InfoUser> next = iterator.next();
            userList.add(next.getValue());
        }
        logger.info("返回用户信息记录:{}", userList);
        return userList;
    }

    public void deleteInfoUserById(String id) {
        logger.info("删除用户信息:{}",infoUserMap.remove(id));
    }

    public String getNameById(String id){
        logger.info("根据ID查询用户名称:{}",id);
        return infoUserMap.get(id).getName();
    }
    public Map<String,InfoUser> getAllUser(){
        logger.info("查询所有用户信息{}",infoUserMap);
        return infoUserMap;
    }
}
