package de.honoka.test.grpc.client

import cn.hutool.core.util.RandomUtil
import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil
import de.honoka.test.grpc.common.protobuf.TestEntity
import de.honoka.test.grpc.common.protobuf.TestEntityServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Client {

    private val service = run {
        val channel = ManagedChannelBuilder.forAddress("localhost",
            8081).usePlaintext().build()
        TestEntityServiceGrpc.newBlockingStub(channel)
    }

    @RequestMapping("/test")
    fun test(): JSONObject {
        val res = service.test(TestEntity.newBuilder().apply {
            id = RandomUtil.randomInt(1, 10)
            name = "grpc-test"
            description = "grpc-test:${id + 5}"
        }.build())
        return JSONUtil.parseObj(res.json)
    }
}