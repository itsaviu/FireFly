package com.firefly.dp;

import com.firefly.dp.annotations.FactoryMember;
import com.firefly.dp.helper.FactoryMemberHelper;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FactoryProducer {

    private List<Class> factoryMembers;

    public <T> T getImplemention(Class<T> clazz, String value) {

        List<Object> implementedClazz = new FactoryMemberHelper(this.factoryMembers, clazz).accumulateByParent();
        for (Object o : implementedClazz) {
            Annotation[] annotations = o.getClass().getAnnotations();
            Optional<Annotation> first = Stream.of(annotations)
                    .filter(annotation -> annotation.annotationType().equals(FactoryMember.class))
                    .findFirst();
            if (first.isPresent()) {
                FactoryMember annotation = (FactoryMember) first.get();
                if (annotation.key().equalsIgnoreCase(value))
                    return (T) o;
            }
        }

        throw new RuntimeException("FireFly not enabled");
    }
}
