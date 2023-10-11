package de.honoka.test.grpc.springboot.server

import de.honoka.test.grpc.springboot.proto.TestMessage
import de.honoka.test.grpc.springboot.proto.TestServiceGrpc
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class TestServiceImpl : TestServiceGrpc.TestServiceImplBase() {

    override fun test(
        request: TestMessage,
        responseObserver: StreamObserver<TestMessage>
    ) = responseObserver.doResponse {
        TestMessage.newBuilder().apply {
            id = request.id + 1
            msg = "Received: \"${request.msg}\""
        }.build()
    }
}