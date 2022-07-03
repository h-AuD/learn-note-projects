package AuD.template.project.spring_learn.beans;

/**
 * 实例C
 */
//@Component
public class InstanceC {

    private InstanceA instanceA;

    private InstanceB instanceB;

    // 无参构造器
    //public InstanceC() {
    //    System.out.println("call InstanceC 无参构造器");
    //}

    // 有参构造器
    public InstanceC(InstanceB instanceB) {
        System.out.println("call InstanceC 有参构造器 'InstanceC(InstanceB instanceB)' ");
        this.instanceB = instanceB;
    }

    // 有参构造器
    public InstanceC(InstanceA instanceA) {
        System.out.println("call InstanceC 有参构造器 'InstanceC(InstanceA instanceA)' ");
        this.instanceA = instanceA;
    }

    // 有参构造器
    //public InstanceC(int a,int b) {
    //    System.out.println("call InstanceC 有参构造器 'InstanceC(int a,int b)' ");
    //}

}
