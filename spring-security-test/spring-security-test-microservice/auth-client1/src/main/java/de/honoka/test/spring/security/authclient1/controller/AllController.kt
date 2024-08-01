package de.honoka.test.spring.security.authclient1.controller

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AllController {

    @GetMapping("/test1")
    fun test1(@RegisteredOAuth2AuthorizedClient client: OAuth2AuthorizedClient): OAuth2AuthorizedClient = client
}