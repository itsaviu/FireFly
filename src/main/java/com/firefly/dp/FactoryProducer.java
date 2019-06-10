package com.firefly.dp;

import com.firefly.dp.annotations.FactoryMember;
import com.firefly.dp.helper.FactoryMemberHelper;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Setter
public class FactoryProducer {

    private List<Class> factoryMembers;

    public <T> T getImplemention(Class<T> clazz, String value) throws IllegalAccessException, InstantiationException {

        List<Class> implementedClazz = new FactoryMemberHelper(this.factoryMembers, clazz).accumulateByParent();
        for (Class o : implementedClazz) {
            Annotation[] annotations = o.getAnnotations();
            Optional<Annotation> first = Stream.of(annotations)
                    .filter(annotation -> annotation.annotationType().equals(FactoryMember.class))
                    .findFirst();
            if (first.isPresent()) {
                FactoryMember annotation = (FactoryMember) first.get();
                if (annotation.key().equalsIgnoreCase(value))
                    return (T) o.newInstance();
            }
        }

        throw new RuntimeException("FireFly not enabled");
    }
}
