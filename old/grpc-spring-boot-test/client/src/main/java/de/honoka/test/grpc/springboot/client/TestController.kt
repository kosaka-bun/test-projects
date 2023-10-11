package de.honoka.test.grpc.springboot.client

import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil
import com.google.protobuf.util.JsonFormat
import de.honoka.test.grpc.springboot.proto.TestMessage
import de.honoka.test.grpc.springboot.proto.TestServiceGrpc.TestServiceBlockingStub
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GrpcClient("test-server")
    private lateinit var testService: TestServiceBlockingStub

    @PostMapping("/test")
    fun test(@RequestBody body: JSONObject): JSONObject {
        val request = TestMessage.newBuilder().apply {
            JsonFormat.parser().merge(body.toString(), this)
        }.build()
        return testService.test(request).run {
            JsonFormat.printer().print(this).run {
                JSONUtil.parseObj(this)
            }
        }
    }
}