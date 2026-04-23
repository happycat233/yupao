package com.happycat.usercenterbackendmaster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happycat.usercenterbackendmaster.common.ErrorCode;
import com.happycat.usercenterbackendmaster.exception.BusinessException;
import com.happycat.usercenterbackendmaster.model.domain.User;
import com.happycat.usercenterbackendmaster.model.domain.request.UserRegisterRequest;
import com.happycat.usercenterbackendmaster.service.UserService;
import com.happycat.usercenterbackendmaster.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.happycat.usercenterbackendmaster.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Lenovo
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2026-03-31 21:50:48
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     * @return 用户id
     */
    @Override
    public long registerUser(UserRegisterRequest userRegisterRequest) {
        String account = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        //检查参数非空
        if (StringUtils.isAnyBlank(account, password, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数部分为空");
        }

        //账号是否合法（长度大于等于4 无非法字符）
        String pattern = "^[a-zA-Z][a-zA-Z0-9_-]{3,19}$";
        if (!Pattern.matches(pattern, account)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号格式不合法");
        }

        //星球账号是否合规
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号不合规");
        }

        //密码是否合规（>=8)
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不合规");
        }

        //两次密码是否一致
        if(!checkPassword.equals(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }

        //账号是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        long num = this.count(queryWrapper);
        if (num > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }

        //星球账号是否已存在
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        num = this.count(queryWrapper);
        if (num > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号已注册");
        }

        //密码加密
        String encryptPassword = PASSWORD_ENCODER.encode(password);

        //注册
        User user = new User();
        user.setAccount(account);
        user.setPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean save = this.save(user);

        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String account, String password, HttpServletRequest request) {
        //检查参数非空
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        //账号是否合法（长度大于等于4 无非法字符）
        String pattern = "^[a-zA-Z][a-zA-Z0-9_-]{3,19}$";
        if (!Pattern.matches(pattern, account)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不合法");
        }

        //密码是否合规（>=8)
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不合法");
        }

        //根据账号查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);

        User user = userMapper.selectOne(queryWrapper);
        if (user == null || !PASSWORD_ENCODER.matches(password, user.getPassword())) {
            log.warn("user login failed, account: {}", account);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }

        //用户信息去敏
        User safetyUser = getSafetyUser(user);

        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public List<User> searchByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isAnyBlank(username)) {
            queryWrapper.like("username", username);
        }
        return this.list(queryWrapper).stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return this.remove(queryWrapper);
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User getSafetyUser(User user) {
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setStatus(user.getStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setAccount(user.getAccount());
        safetyUser.setPlanetCode(user.getPlanetCode());
        return safetyUser;
    }

}




