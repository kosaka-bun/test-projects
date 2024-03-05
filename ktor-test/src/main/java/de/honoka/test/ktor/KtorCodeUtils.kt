package de.honoka.test.ktor

import cn.hutool.json.JSONUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondJson(obj: Any?, httpStatus: HttpStatusCode = HttpStatusCode.OK) {
    if(obj is String) {
        respondText(obj, status = httpStatus)
        return
    }
    respondText(JSONUtil.toJsonStr(obj), ContentType.Application.Json, httpStatus)
}