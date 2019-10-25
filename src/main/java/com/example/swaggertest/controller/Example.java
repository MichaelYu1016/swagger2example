package com.example.swaggertest.controller;

import com.example.swaggertest.annotation.ApiJsonObject;
import com.example.swaggertest.annotation.ApiJsonProperty;
import com.example.swaggertest.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 示例：展示String参数的详细参数和参数类型
 *
 * @author ynx
 * @date 2019-10-24
 * @modified_date 2019-10-24
 */
@RestController
@RequestMapping(path = "/example")
@Api(value = "示例", tags = "示例")
public class Example {

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "编辑",notes = "编辑商品")
    public void edit(@ApiParam(value = "id",required = true,defaultValue = "1") @PathVariable Integer id,
                                 @ApiJsonObject(name = "Edit",notes = "展示如何显示参数属性",value = {
                                         @ApiJsonProperty(key = "name", description = "商品名称", example = "华为手机"),
                                         @ApiJsonProperty(key = "number",description = "数量",example = "8",type = "int"),
                                         @ApiJsonProperty(key = "price",description = "单价",example = "50.12",type = "double")
                                 })
                                 @RequestBody String request) {

    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录接口")
    public String login(@RequestBody User user) {
        return "Welcome";
    }

}
