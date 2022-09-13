package de.honoka.test.kotlin.various

@AllOpen
class Class1 {

    var dynamicField: String = "Field has inited"

    companion object {
        fun say() {
            println("OK, It's Kotlin.")
        }

        @JvmStatic
        fun jvmStaticMethod() {
            println("It's JVM Static Method!")
        }

        @JvmField
        var staticField: String = "Static Field";
    }

    fun dynamicMethod(arg: String) {
        println("Called Dynamic Method: $arg")
    }

    final inline fun inlineMethod(method: (Int) -> Int) {
        println("This is Inline Method ${method(123)}")
        "123".bindedMethodInClass()
    }

    //Only String objects in this class can call this method.
    fun String.bindedMethodInClass() {
        println("This is binded method in class: ${this.length}")
    }
}

fun main(args: Array<String>) {
    /*val window = ConsoleWindow("Kotlin", null) {
        println("lambda表达式中的程序退出")
    }
    window.screenZoomScale = 1.25
    window.isAutoScroll = true
    window.show()*/
    print("args: ")
    args.forEach { print("$it\t") }
    println()
    Class1.say()
    val obj = Class1()
    //obj.inlineMethod { x -> x + 2 }
    obj.inlineMethod(fun(x): Int {
        return x + 2
    })
    val text = """
         
        The static field is "${Class1.staticField}", 
         
         
        dynamic field is "${obj.dynamicField}".
         
    """
    println("-------------------------")
    println(text.trimIndent().trim())
    println("-------------------------")
    println(text.trimIndent().trim().replace(oldValue = "\n", newValue = ""))
    println("-------------------------")
    println(text.singleLine())
    println("-------------------------")
    /*val files = File("C:\\").listFiles { file -> file.isFile }
    /*for(file: File in files) {
        println(file.name)
    }*/
    files?.forEach { file: File -> println(file.name) }*/
    recvLambdaFun("hello") { println(this) }
    val lambdaFun1: () -> Unit = { println("This is Lambda Function saved in val!") }
    lambdaFun1()
    val lambdaFun2: (Int, Int) -> Unit = { x, y ->
        println("This is Lambda Function with parameter saved in val! Param: $x, $y")
    }
    lambdaFun2(10, 20)
    val lambdaFun3: (String) -> Int = {
        println("""
            This is Lambda Function with parameter and return value saved in val!
            Param: $it
        """.singleLine(true))
        it.length
    }
    println("lambda3 returns: ${lambdaFun3("hello")}")
    val lambdaFun4: String.() -> Int = {
        println("""
            This is Lambda Function binded on String type!
            After this definition, all objects of String type will be considered
            as having a member function named "lambdaFun4".
            This function will return the length of String object which called
            this lambda function.
        """.singleLine(true))
        this.length
    }
    println("""The length of "hello": ${"hello".lambdaFun4()}""")
    val lambdaFun5: (String) -> Unit = ::println
    lambdaFun5("""lambdaFun5 quoted "println" in stdlib.""")
}

fun String.singleLine(spaceAtLineEnd: Boolean = false): String {
    val str = trimIndent().trim()
    val lines = str.split("\n")
    val builder = StringBuilder()
    lines.forEach { line ->
        if(line.isNotBlank()) {
            builder.append(line)
            if(spaceAtLineEnd) builder.append(" ")
        }
    }
    return builder.toString()
}

fun recvLambdaFun(reciever: String, lambda: String.() -> Unit) {
    reciever.lambda()
}

//region IDEA特有的可折叠块，Java和Kotlin均适用

private data class NoName(val a: String)

//endregion