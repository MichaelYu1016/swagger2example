package com.example.swaggertest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 登录用户实体类
 *
 * @author ynx
 * @date 2019-10-25
 * @modified_date 2019-10-25
 */
@ApiModel(value = "User", description = "用户实体类")
public class User {

    @ApiModelProperty(value = "用户名")
    public String username;
    @ApiModelProperty(value = "密码")
    public String password;
}
