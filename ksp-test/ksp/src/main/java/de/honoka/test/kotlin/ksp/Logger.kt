package de.honoka.test.kotlin.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Logger

class LoggerProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    
    class Provider : SymbolProcessorProvider {
        
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = run {
            LoggerProcessor(environment)
        }
    }
    
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Logger::class.qualifiedName!!)
        symbols.filterIsInstance<KSClassDeclaration>().forEach {
            val file = environment.codeGenerator.createNewFile(
                Dependencies(false),
                "de.honoka.test.kotlin.ksp.generated",
                "Test"
            )
            val code = """
                package de.honoka.test.kotlin.ksp.generated
                
                import ${it.qualifiedName!!.asString()}
                import org.slf4j.Logger
                import org.slf4j.LoggerFactory
                
                val ${it.simpleName.asString()}.log: Logger
                    get() = LoggerFactory.getLogger(${it.simpleName.asString()}::class.java)
            """.trimIndent().trim()
            file.write(code.toByteArray())
        }
        return emptyList()
    }
}