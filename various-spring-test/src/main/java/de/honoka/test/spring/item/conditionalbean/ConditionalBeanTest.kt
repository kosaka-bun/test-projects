package de.honoka.test.spring.item.conditionalbean

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/conditionalBean")
@RestController
class ConditionalBeanTest(val conditionalBean: ConditionalBean) {
    
    @GetMapping("/test1")
    fun test1(): String = conditionalBean.a()
}

interface ConditionalBean {
    
    fun a(): String
}

//@ConditionalOnMissingBean(ConditionalBean::class)
@Component
class DefaultConditionalBean : ConditionalBean {
    
    override fun a(): String = this::class.simpleName!!
}
