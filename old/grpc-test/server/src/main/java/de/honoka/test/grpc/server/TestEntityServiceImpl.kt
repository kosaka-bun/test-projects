package de.honoka.test.grpc.server

import com.google.protobuf.util.JsonFormat
import de.honoka.test.grpc.common.protobuf.GrpcResponse
import de.honoka.test.grpc.common.protobuf.TestEntity
import de.honoka.test.grpc.common.protobuf.TestEntityServiceGrpc
import io.grpc.stub.StreamObserver

class TestEntityServiceImpl : TestEntityServiceGrpc.TestEntityServiceImplBase() {

    override fun test(
        request: TestEntity,
        responseObserver: StreamObserver<GrpcResponse>
    ) {
        responseObserver.onNext(GrpcResponse.newBuilder().apply {
            status = true
            json = JsonFormat.printer().print(request)
        }.build())
        responseObserver.onCompleted()
    }
}