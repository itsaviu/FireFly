package com.firefly.dp.helper;


import com.firefly.dp.annotations.EnableFireFly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class FactoryHelper {

    @Autowired
    private ApplicationContext context;

    public List<Object> getFireFlyClass() {
        return new ArrayList<>(context.getBeansWithAnnotation(EnableFireFly.class).values());
    }

    public Boolean isFireFlyEnabled() {
        if (!CollectionUtils.isEmpty(getFireFlyClass()))
            return true;
        return false;
    }

}
