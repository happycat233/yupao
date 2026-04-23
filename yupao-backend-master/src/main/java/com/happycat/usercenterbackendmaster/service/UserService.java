package com.happycat.usercenterbackendmaster.service;

import com.happycat.usercenterbackendmaster.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happycat.usercenterbackendmaster.model.domain.request.UserRegisterRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-03-31 21:50:48
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @return 用户id
     */
    long registerUser(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param account 用户账号
     * @param password 密码
     * @return 去敏信息
     */
    User userLogin(String account, String password, HttpServletRequest request);

    /**
     * 根据用户名查找
     * @param username 用户名
     * @return
     */
    List<User> searchByName(String username);

    /**
     * 通过id删除
     * @param id id
     * @return true 成功 false 失败
     */
    boolean deleteById(long id);

    /**
     * 用户注销
     * @param request
     * @return
     */
    Integer userLogout(HttpServletRequest request);

    /**
     * 用户信息去敏
     * @param user 原始用户
     * @return 去敏后的用户
     */
    User getSafetyUser(User user);
}
