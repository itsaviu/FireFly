package com.firefly.dp.annotations;

import com.firefly.dp.FireFlyRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FireFlyRegistrar.class)
public @interface EnableFireFly {
}
