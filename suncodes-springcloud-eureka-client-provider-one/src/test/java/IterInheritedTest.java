import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IterInheritedTest {

    @Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface DESC {
        String value() default "";
    }

    interface SuperInterface {
        @DESC("父接口的属性")
        String field = "field";
        @DESC("父接口方法foo")
        @RequestMapping("/a")
        public void foo(@DESC String s);
        @DESC("父接口方法bar")
        default public void bar() {

        }
    }

    interface ChildInterface extends SuperInterface {
        @DESC("子接口方法foo")
        @Override
        void foo(String s);
    }

    class ChildClass implements SuperInterface {
        @DESC("子类的属性")
        public String field = "field";
        @Override
        public void foo(String s) {
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException {

        Method iFoo = ChildInterface.class.getMethod("foo", String.class);
        System.out.println(Arrays.toString(iFoo.getAnnotations()));
        // output: [@annotations.IterInheritedTest$DESC(value=子接口方法foo)]

        Method iBar = ChildInterface.class.getMethod("bar");
        System.out.println(Arrays.toString(iBar.getAnnotations()));
        // output: [@annotations.IterInheritedTest$DESC(value=父接口方法bar)]

        Field iField = ChildInterface.class.getField("field");
        System.out.println(Arrays.toString(iField.getAnnotations()));
        // output: [@annotations.IterInheritedTest$DESC(value=父接口的属性)]

        Annotation[] annotations = ChildClass.class.getAnnotations();
        System.out.println(Arrays.toString(annotations));

        Method foo = ChildClass.class.getMethod("foo", String.class);
        System.out.println(Arrays.toString(foo.getAnnotations()));
        // output: []; 被子类覆盖

        Method bar = ChildClass.class.getMethod("bar");
        System.out.println(Arrays.toString(bar.getAnnotations()));
        // output: [@annotations.IterInheritedTest$DESC(value=父接口方法bar)]

        Field field = ChildClass.class.getField("field");
        System.out.println(Arrays.toString(field.getAnnotations()));
        // output: [@annotations.IterInheritedTest$DESC(value=子类的属性)]
        // 是子类作用域下的属性`field`

        System.out.println("====================");
        DESC mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(foo, DESC.class);
        DESC mergedAnnotation1 = AnnotatedElementUtils.findMergedAnnotation(iFoo, DESC.class);
        System.out.println(mergedAnnotation);
        System.out.println(mergedAnnotation1);

        // 获取实现类的接口列表
        Class<?>[] interfaces = ChildClass.class.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            // 获取接口上的方法
            Method foo1 = anInterface.getMethod("foo", String.class);
            Annotation[] annotations1 = foo1.getAnnotations();
            System.out.println(Arrays.toString(annotations1));
            // 获取方法参数注解
            Annotation[][] parameterAnnotations = foo1.getParameterAnnotations();
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                System.out.println(Arrays.toString(parameterAnnotation));
            }
        }
    }
}