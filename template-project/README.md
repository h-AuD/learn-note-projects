# template-project
this is a learn material of spring boot &amp; basic application project with template framework

```bash
### this projiect is a temlate,这是个模板工程,有着通用的项目结构和常用的配置,如下:
# 1.项目的结构
- 此处采用的单模块设计,即所有的代码(controller、model、mapper...)都放在一起。
# 2.package structure as follow:
- package root path(包的根路径):"AuD.template.project"。大部分包内有个"package_info.java",即this-package内容说明 & 文档。
- controller：前端控制器所处的位置。如果存在对外开放的API,可以考虑单独用一个package存放。
- service:业务/功能代码所在的路径。
- mapper:DAO接口
- model:模型数据
- core：application所需要的配置信息 & 过滤器 & 监听器 辅助功能(eg.定时任务、AOP) & utils...
# 3.如果项目变得越来越复杂,eg.controller、model、core等等内代码过于庞大,可以考虑将其拆分为多个module,利用maven多模块构建功能.
- 可以将model & mapper单独提取成独立的module、core下的内容单独组成一个module、controller & service单独成立一个module，application入口类 & 配置文件单独组成一个"start-up"模块.
- 上述module都有一个相同的 "package root path"。拆分的目的是使项目代码层面结构简单化,不至于当代码变得庞大时,项目显得很臃肿，既不方便阅读,也不利于维护.
- Attention:代码层面的拆分,一定要考量清楚,千万不要弄成拆分后结果还不如不拆分.所以代码方面的拆分需要注意 "解耦" & "单一职责"。eg：
-- model & mapper可以放在一起的,它们的作用基本相同(或说它们有着相同的使命),操作DB & 数据传输.
-- controller & service 可以放在一起的,它们就是接口以及具体的功能实现.
- 切记:每个模块都要各自的任务,必须遵循"单一职责"原则.eg:不可以在 model & mapper模块中放service,不可在controller & service模块中放utils等等...
- 参考另外一个项目设计:template-project-multi-module
--- 关于multi-module总结一句:如果用的好的话,代码给人一种清爽的感觉,如果没有设计好的话,还不如不用.
```

