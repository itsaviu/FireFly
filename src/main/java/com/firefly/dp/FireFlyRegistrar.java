package com.firefly.dp;

import com.firefly.dp.annotations.FactoryMember;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FireFlyRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {


    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        try {
            List<Class> classes = returnRegisteredFireFlies(metadata);
            registerBean(registry, classes);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void registerBean(BeanDefinitionRegistry registry, List<Class> clazz) throws BeansException {

        BeanDefinition dynamicBean = BeanDefinitionBuilder
                .rootBeanDefinition(FactoryProducer.class)
                .addPropertyValue("factoryMembers", clazz)
                .getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(dynamicBean, FactoryProducer.class.getName(), new String[]{"Factory-Producer"});

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

    }


    public List<Class> returnRegisteredFireFlies(AnnotationMetadata metadata) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FactoryMember.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        String packageName = ClassUtils.getPackageName(metadata.getClassName());
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(packageName);
        List<Class> clazzList = new ArrayList<>();

        for (BeanDefinition bean : candidateComponents) {
            clazzList.add(Class.forName(bean.getBeanClassName()));
        }

        return clazzList;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

}
