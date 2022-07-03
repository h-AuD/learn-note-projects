package AuD.template.project.spring_learn.beans;

import org.springframework.stereotype.Component;

/**
 * 实例B 与实例A 组合成循环依赖关系
 */
@Component
public class InstanceB {

    private InstanceA instanceA;


    public InstanceA getInstanceA() {
        return instanceA;
    }

    public void setInstanceA(InstanceA instanceA) {
        this.instanceA = instanceA;
    }
}
