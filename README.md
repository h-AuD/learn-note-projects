# Learn_project_collection
```shell
######### 项目集合 ########
`learn_project_collection` 
-- 并不是一个项目/工程,而只是一个"文件夹(dir)",充当的角色仅仅只是'room/相同的空间',即将多个项目工程/代码放在一个文件下(这里笔者仅仅为了方便管理 & 查阅).
ps:实际工作中,个人不推荐这样做(给人一种混乱的感觉).
eg.场景:为了方便管理代码,创建一个git仓库,该仓库下面有多个项目(project_A、project_A...),并且这些项目都是服务于同一个业务/同一类型业务 ==== 不推荐这样做,每个project是独立的存在,即使project_A和project_B是有关联的(eg.它们需要互相通信),放在一起就破坏它们各自的独立性,某个开发人员负责project_B,但是却连其他工程也一起download

包含如下工程:
- template-project
- template-project-basic_multi_module
note:
`相同点`:
1.All in one
2.基于MVC三层架构,即controller --> service --> mapper
`不同点`:
1.template-project采用maven单模块开发模式
2.template-project-basic_multi_module采用maven多模块开发模式,即从代码层面上做相应的拆分.
```

