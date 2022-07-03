package AuD.template.project.mapper;


import AuD.template.project.model.UserBasicInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;


/**
 * Description: 用于执行错误SQL的mapper. -- 使用{@link Insert}等注解,这里SQL写在Mapper接口,仅仅为了方便而已,实际开发不推荐这样做.
 *
 * @author AuD/胡钊
 * @ClassName UserMapper
 * @date 2021/4/13 17:43
 * @Version 1.0
 */
@Repository
public interface UserBasicInfoErrorMapper {

    /**
     * 执行一个error SQL -- 表不存在导致
     * @param userBasicInfo
     * @return
     */
    @Insert("INSERT INTO Xxx_ccc(NAME) VALUES (#{name})")
    int errorSqlAsTableNotExists(UserBasicInfo userBasicInfo);

    /**
     * 表字段不存在导致
     * @param name
     * @return
     */
    @Insert("INSERT INTO user_basic_info(XXX) VALUES (#{name})")
    int errorSqlAsFieldNotExists(String name);

    /**
     * 1.userBasicInfo.age==null,但是table设置AGE NOT NULL.   <br>
     * 2.table name 长度设置为N,但是userBasicInfo.name.length>N. <br>
     * 3.table name 设置unique key,但是userBasicInfo.name是一个已经存在的值. <br>
     * @param userBasicInfo
     * @return
     */
    @Insert("INSERT INTO user_basic_info(NAME,AGE) VALUES(#{name},#{age})")
    int errorSqlAsFieldConstraint(UserBasicInfo userBasicInfo);



}
