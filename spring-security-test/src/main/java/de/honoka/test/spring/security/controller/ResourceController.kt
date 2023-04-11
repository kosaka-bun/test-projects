package de.honoka.test.spring.security.controller

import de.honoka.sdk.json.api.JsonObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResourceController {

    @GetMapping("/hello")
    fun hello(): JsonObject = JsonObject.of().add("msg", "hello")
}