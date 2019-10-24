# Swagger2 关于Map或String参数在API文档中展示详细参数以及参数说明

本程序主体来源于[https://blog.csdn.net/hellopeng1/article/details/82227942](https://blog.csdn.net/hellopeng1/article/details/82227942)，后期根据使用对程序中的一些bug进行了修复。

## 改进之处

1. 源程序在ApiJsonProperty中type设置为int时会出现NPE，只有设置为string时才能正常显示。阅读代码发现原作者将example属性进行了修改，导致前端接受不到正确参数。
*修正*: 不更改example属性的类型，在设置ApiModelProperty时根据ApiJsonProperty的type设置dataType，即能够正确展示模型参数和参数类型.

2. 新增支持double类型.

3. ApiJsonProperty新增notes属性用来描述该参数.