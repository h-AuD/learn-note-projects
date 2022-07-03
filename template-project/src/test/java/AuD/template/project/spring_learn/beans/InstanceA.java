package AuD.template.project.spring_learn.beans;

import org.springframework.stereotype.Component;

/**
 * 实例A 与实例B 组合成循环依赖关系
 */
@Component
public class InstanceA {

    private InstanceB instanceB;


    public InstanceB getInstanceB() {
        return instanceB;
    }

    public void setInstanceB(InstanceB instanceB) {
        this.instanceB = instanceB;
    }
}
