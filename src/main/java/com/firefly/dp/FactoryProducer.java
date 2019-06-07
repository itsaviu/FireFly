package com.firefly.dp;

import com.firefly.dp.annotations.FactoryMember;
import com.firefly.dp.helper.FactoryHelper;
import com.firefly.dp.helper.FactoryMemberHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class FactoryProducer {

    @Autowired
    private FactoryMemberHelper factoryMember;

    @Autowired
    private FactoryHelper factoryHelper;

    public <T> T getImplemention(Class<T> clazz, String value) {

        if (factoryHelper.isFireFlyEnabled()) {
            List<Object> implementedClazz = factoryMember.accumulateByParent(clazz);
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
        }

        throw  new RuntimeException("FireFly not enabled");
    }
}
