package com.happycat.usercenterbackendmaster.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="`user`")
@Data
public class User {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 密码
     */
    private String password;

    /**
     *  状态 0-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户权限 - 0 普通用户  1 管理员
     */
    private Integer userRole;
    /**
     * 星球编号
     */
    private String planetCode;
}