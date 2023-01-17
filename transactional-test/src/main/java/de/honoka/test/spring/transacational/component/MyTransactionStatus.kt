package de.honoka.test.spring.transacational.component

import org.springframework.transaction.TransactionStatus

class MyTransactionStatus : TransactionStatus {

    override fun isNewTransaction(): Boolean = true

    override fun setRollbackOnly() = println("setRollbackOnly")

    override fun isRollbackOnly(): Boolean = false

    override fun isCompleted(): Boolean = false

    override fun createSavepoint(): Any = println("createSavepoint")

    override fun rollbackToSavepoint(savepoint: Any) = println("rollbackToSavepoint")

    override fun releaseSavepoint(savepoint: Any) = println("releaseSavepoint")

    override fun flush() = println("flush")

    override fun hasSavepoint(): Boolean = false
}
