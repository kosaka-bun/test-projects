package de.honoka.test.spring.security.auth.controller

import cn.hutool.json.JSONObject
import de.honoka.sdk.util.framework.web.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RequestMapping("/oauth2")
@RestController
class Oauth2Controller {

    data class ConsentInfo(val clientId: String, val state: String, val scope: String)

    private val waitForConsentMap = HashMap<String, ConsentInfo>()

    @GetMapping("/consent")
    fun consent(
        @RequestParam("client_id") clientId: String,
        @RequestParam("state") state: String,
        @RequestParam("scope") scope: String
    ): ApiResponse<*> {
        val id = UUID.randomUUID().toString()
        val info = ConsentInfo(clientId, state, scope)
        waitForConsentMap[id] = info
        return ApiResponse.success(JSONObject().also {
            it["id"] = id
            it["consent"] = info
        })
    }
}