package de.honoka.test.spring.crt.test2

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
class TestBean

@Component
class UsageBean : ApplicationContextAware, ApplicationRunner {

    lateinit var context: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    override fun run(args: ApplicationArguments) {
        val testBean = try {
            context.getBean(TestBean::class.java)
        } catch(t: Throwable) {
            null
        }
        println(testBean)
    }
}

@Configuration
class BeanConfig : BeanDefinitionRegistryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {}

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        registry.beanDefinitionNames.forEach {
            val def = registry.getBeanDefinition(it)
            if(def.beanClassName == TestBean::class.java.name) {
                registry.removeBeanDefinition(it)
            }
        }
    }
}
