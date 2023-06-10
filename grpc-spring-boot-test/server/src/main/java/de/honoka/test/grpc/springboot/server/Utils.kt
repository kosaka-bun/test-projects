package de.honoka.test.grpc.springboot.server

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver

inline fun <T> StreamObserver<T>.doResponse(block: () -> T) {
    var completed = false
    try {
        val response = block()
        onNext(response)
    } catch (t: Throwable) {
        onError(StatusRuntimeException(Status.INTERNAL.withDescription(t.message)))
        completed = true
    } finally {
        if(!completed) onCompleted()
    }
}