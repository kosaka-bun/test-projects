package de.honoka.test.spring.transacational.component

import de.honoka.sdk.json.api.JsonObject
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus

@Component
class MyTransactionalManager : PlatformTransactionManager {

    override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
        println("获取事务")
        println("事务定义：$definition")
        println(JsonObject.of(mapOf(
                "name" to definition?.name,
                "propagation" to definition?.propagationBehavior,
                "isolation" to definition?.isolationLevel,
                "timeout" to definition?.timeout,
                "readOnly" to definition?.isReadOnly
        )).toPrettyString())
        return MyTransactionStatus()
    }

    override fun commit(status: TransactionStatus) {
        println("提交事务")
    }

    override fun rollback(status: TransactionStatus) {
        println("回滚事务")
    }
}
