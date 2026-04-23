package com.happycat.usercenterbackendmaster.controller;

import com.happycat.usercenterbackendmaster.common.ErrorCode;
import com.happycat.usercenterbackendmaster.common.ResponseResult;
import com.happycat.usercenterbackendmaster.common.ResultUtils;
import com.happycat.usercenterbackendmaster.constant.UserConstant;
import com.happycat.usercenterbackendmaster.exception.BusinessException;
import com.happycat.usercenterbackendmaster.model.domain.User;
import com.happycat.usercenterbackendmaster.model.domain.request.UserLoginRequest;
import com.happycat.usercenterbackendmaster.model.domain.request.UserRegisterRequest;
import com.happycat.usercenterbackendmaster.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 吴昊
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无请求参数");
        }
        String account = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(account, password, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"部分请求参数为空");
        }
        long l = userService.registerUser(userRegisterRequest);
        return ResultUtils.success(l);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无请求参数");
        }
        String account = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"部分请求参数为空");
        }
        User user = userService.userLogin(account, password, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public ResponseResult<User> current(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) attribute;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        User user = userService.getById(currentUser.getId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
        }
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无请求参数");
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public ResponseResult<List<User>> search(String username, HttpServletRequest request) {
        checkPermission(request);
        List<User> userList = userService.searchByName(username);
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public ResponseResult<Boolean> delete(@RequestBody long id, HttpServletRequest request) {
        checkPermission(request);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不合法");
        }
        boolean b = userService.deleteById(id);
        return ResultUtils.success(b);
    }

    /**
     * 权限检查（未登录抛NOT_LOGIN_ERROR，非管理员抛NO_AUTH_ERROR）
     */
    private void checkPermission(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) attribute;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        if (!UserConstant.ADMIN_USER.equals(user.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有权限");
        }
    }

}
