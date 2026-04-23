package com.happycat.usercenterbackendmaster.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 吴昊
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userAccount;
    private String userPassword;
}
