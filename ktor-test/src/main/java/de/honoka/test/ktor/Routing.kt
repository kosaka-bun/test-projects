package de.honoka.test.ktor

import cn.hutool.core.exceptions.ExceptionUtil
import cn.hutool.json.JSONObject
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRouting() {
    //https://ktor.io/docs/requests.html
    routing {
        get("/") {
            call.respondJson("Hello World!")
        }
        get("/obj") {
            call.respondJson(TestEntity())
        }
        get("/arr") {
            call.respondJson(listOf(
                TestEntity(),
                TestEntity(2)
            ))
        }
        get("/headers") {
            val json = JSONObject()
            call.request.headers.forEach { key, value ->
                json[key] = if(value.size == 1) value[0] else value
            }
            call.respondJson(json)
        }
        get("/getParams") {
            val json = JSONObject()
            call.request.queryParameters.forEach { key, value ->
                json[key] = value[0]
            }
            call.respondJson(json)
        }
        post("/postBody") {
            call.respondJson(JSONObject().apply {
                set("body", call.receiveText())
            })
        }
        get("/error") {
            throw Exception("manual")
        }
        get("/sub/*") {
            call.respondJson(JSONObject().apply {
                set("type", "sub")
                set("url", call.request.path())
            })
        }
        get("/sub2/{...}") {
            call.respondJson(JSONObject().apply {
                set("type", "sub2")
                set("url", call.request.path())
            })
        }
    }
}

fun Application.configureHttpHandlers() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondJson(ApiResponse<Any>().apply {
                code = 404
                msg = "path: ${call.request.path()}"
            }, status)
        }
        exception<Throwable> { call, t ->
            call.respondJson(ApiResponse<Any>().apply {
                code = 500
                msg = "path: ${call.request.path()}"
                data = ExceptionUtil.stacktraceToString(t)
            }, HttpStatusCode.InternalServerError)
        }
    }
}

data class TestEntity(

    var id: Int = 1,

    var field1: String = "test",

    var field2: Date = Date()
)

data class ApiResponse<T>(

    var code: Int? = null,

    var msg: String? = null,

    var data: T? = null
)