package com.firefly.dp.helper;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FactoryMemberHelper {

    private List<Class> fireFlyClazz;

    private Class parentClass;

    public List<Class> accumulateByParent() {
        return this.fireFlyClazz.stream()
                .filter(clazz -> parentClass.isAssignableFrom(clazz))
                .collect(Collectors.toList());
    }
}
