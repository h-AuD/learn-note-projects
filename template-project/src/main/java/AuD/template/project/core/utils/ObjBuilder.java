package AuD.template.project.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Description 对象构建器 ==== ps:暂时放在这里,后期需要移植到base-common
 * -- 不需要obj field map
 * -- 不适用 Lombok#@Builder注解(有坑,需要注意,因此显得麻烦)
 * @ClassName ObjBuilder
 * @Author 胡钊
 * @Date 2022/10/13 13:53
 * @Version
 */
public class ObjBuilder<T> {

    /**
     * 实例化提供器
     */
    private final Supplier<T> instantiate;

    private List<Consumer<T>> modifiers = new ArrayList<>();

    private ObjBuilder(Supplier<T> instantiate) {
        this.instantiate = instantiate;
    }

    public static <T> ObjBuilder<T> of(Supplier<T> instantiator) {
        return new ObjBuilder<>(instantiator);
    }

    /**
     * 设置属性 -- function只有 1个参数
     *
     * @param consumer
     * @param p1
     * @param <P1>
     * @return
     */
    public <P1> ObjBuilder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public <P1, P2> ObjBuilder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }


    public <P1, P2, P3> ObjBuilder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }


    public T build() {
        T value = instantiate.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }


    /**
     * 1 参数 Consumer
     */
    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }
    /**
     * 2 参数 Consumer
     */
    @FunctionalInterface
    public interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }
    /**
     * 3 参数 Consumer
     */
    @FunctionalInterface
    public interface Consumer3<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }

}