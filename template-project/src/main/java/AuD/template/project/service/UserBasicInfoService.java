package AuD.template.project.service;

import AuD.component.common.web.ControllerResultInfo;
import AuD.component.common.web.ControllerResultInfoBuilder;
import AuD.template.project.mapper.UserBasicInfoMapper;
import AuD.template.project.model.UserBasicInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName TemplateService
 * @date 2021/4/16 19:46
 * @Version 1.0
 */
@Slf4j
@Service
public class UserBasicInfoService {

    @Autowired
    private UserBasicInfoMapper userBasicInfoMapper;

    /**
     * 验证 mybatis 的{@link ResultContext}接口.
     * @return
     */
    public ControllerResultInfo doResultHandler(){
        List<UserBasicInfo> result = new ArrayList<>();
        userBasicInfoMapper.getAllInfo((resultContext -> {
            DefaultResultContext<UserBasicInfo> defaultResultContext = (DefaultResultContext<UserBasicInfo>) resultContext;
            result.add(defaultResultContext.getResultObject());
            log.info("{}",defaultResultContext.getResultCount());
        }));
        return ControllerResultInfoBuilder.success(result);
    }


    /**
     * 验证spring事务回滚机制.  <br>
     * 在加上事务{@link Transactional}情况下,一般不推荐使用try..catch..(理由见catch内注释),
     * 因为如果使用try..catch..那么导致异常被吞了,事务不会回滚(参见{@code Transactional}). <br>
     * 但是在某些特定的场景下,需要使用try..catch..,并且希望事务可以正常回滚,
     * 此时需要使用手动回滚,使用{@link TransactionAspectSupport}(spring提供的事务工具类)或者抛一个异常(推荐). <br>
     *
     * 特定的场景case:   <br>
     * 新增用户的接口中,其中DB设置用户名称UK,在并发(或者重复提交)的情况下,insert SQL执行是会抛异常的,
     * 所以,有人会在insert之前通过select判断该username是否存在,来执行是否需要insert.
     * 这里笔者不推荐这样做,理由:真正的逻辑是insert,没有必要增加一条的SQL(无形增加DB压力,而且这条SQL的价值并不大,SQL是个很重要的资源,不用随意使用),
     * 因为可以通过捕获异常(对inset逻辑try..catch..)来处理上述问题,然后异常交由全局异常来处理。
     * 可能会有人说,insert发生异常不一定是DB约束导致,可能是其他问题,比如连接问题.
     * 是的,没毛病,但是就inset这个逻辑/SQL而言,如果发生异常,那么大概率就是约束问题,况且可以在catch中做精确异常匹配(如此做,就显得有点臃肿,除非真的有必要的情况下,是可以这样做的).  <br>
     * 笔者(本人)持有的观点就是,select价值不大,没有必要,SQL是重要资源,不要随意使用.   <br>
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ControllerResultInfo transactionRollback(UserBasicInfo userBasicInfo){
        userBasicInfoMapper.insertOne(userBasicInfo);
        try {
            // TODO 设置userBasicInfo对象某个属性值,使其违反DB约束
            // 故意使这里抛异常
            userBasicInfoMapper.updateByPK(userBasicInfo);
        }catch (Exception e){

            /* =======================================================================================
             * 捕获异常,需要手动回滚,由于异常被catch,下面的代码/流程依然会被执行,所以此时事务会回滚吗?
             * 依然会回滚的,由于捕获异常导致下面的"deleteByPK"会被执行,i.e.依然会发送SQL
             * 不过因为回滚的原因,使下面SQL执行是没有没用意义的(因为最终事务回滚了)
             * 到这里问题就出现了,明明不需要执行的SQL,确执行而且还没有任何意义,无形增加DB压力负担
             * 这也就是为什么加上"@Transactional",不推荐使用try..catch..
             * 但是,正如注释内容所述,某些场景下,需要使用try..catch..
             * 所以,这里建议:如果使用try..catch..,避免执行无意义逻辑(eg.在catch中抛一个异常,或者增加try..catch..范围).
             * =======================================================================================*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // throw AbstractServerException.of(SERVER_ERROR.getCode(),SERVER_ERROR.getMessage());
        }
        int id = userBasicInfo.getId()+1;
        userBasicInfoMapper.deleteByPK(id);
        return ControllerResultInfoBuilder.success();
    }

}
