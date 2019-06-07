package com.firefly.dp.helper;

import com.firefly.dp.annotations.FactoryMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FactoryMemberHelper {

    @Autowired
    private ApplicationContext context;

    private List<Object> fireFlyClazz;

    @PostConstruct
    public void initiator() {
        this.fireFlyClazz = new ArrayList<>(context.getBeansWithAnnotation(FactoryMember.class).values());
    }

    public List<Object> accumulateByParent(Class parent) {
        return this.fireFlyClazz.stream()
                .filter(clazz -> parent.isAssignableFrom(clazz.getClass()))
                .collect(Collectors.toList());
    }
}
