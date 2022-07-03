package AuD.template.project.core.exception;

/**
 * Description:自定义服务端exception info.  <br>
 *
 * Note:    <br>
 * 1.需要继承{@link AuD.component.common.web.AbstractServerException}.  <br>
 * <br>
 * 2.构造器私有化(eg.protected或包访问权限),i.e.不希望通过new方式构建对象,eg:参考{@link AuD.template.project.core.exception.SignatureFailException} <br>
 * - 每个自定义异常表示某一个场景值,即所对应的code值是唯一的.如果开放构造器,那么code就不再具有唯一性,
 * 比如:4001表示签名失败,但是可以new SignatureFailException(4002,"msg"),从语义(class name)上来看就是表示签名失败的场景,
 * 但是签名失败的code应该是4001,这就给人一种迷惑感.    <br>
 * <br>
 * 3.根据条款2可知,需要一个常量,用于表示该场景含义.(参考{@link AuD.template.project.core.exception.SignatureFailException#SIGNATURE_FAIL_EXCEPTION})
 * <br>
 * 4.但是,即使在相同的场景下,其中msg可能会需要使用不同的值,依据条款3 & 父类来看,是没有msg setter
 * --> 可以通过构建对象的方式设置msg.(参考{@link AuD.template.project.core.exception.ParameterException#of(java.lang.String)})   <br>
 * -- 为什么不通过3中的常量以及提供setter来完成呢? --> 因为不安全(比如高并发下,如果使用同一个对象,那么msg1可能会覆盖msg2).   <br>
 *
 * @author AuD/胡钊
 *
 * @date 2021/1/20 13:34
 * @Version 1.0
 */
