package AuD.template.project.mapper;


import AuD.template.project.model.UserBasicInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;


/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName UserMapper
 * @date 2021/4/13 17:43
 * @Version 1.0
 */
@Repository
public interface UserBasicInfoMapper {

    /**
     * 验证接口{@link ResultHandler},ResultHandler是一个函数式接口,作用类似于JDBC中的{@link java.sql.ResultSet}.
     * @param resultHandler
     */
    public void getAllInfo(ResultHandler<UserBasicInfo> resultHandler);

    /**
     * 插入一条记录
     * @param userBasicInfo
     * @return
     */
    public int insertOne(UserBasicInfo userBasicInfo);

    /**
     * 根据主键更新记录
     * @param userBasicInfo
     * @return
     */
    public int updateByPK(UserBasicInfo userBasicInfo);

    /**
     * 根据主键删除记录
     * @param id
     * @return
     */
    public int deleteByPK(int id);

}
