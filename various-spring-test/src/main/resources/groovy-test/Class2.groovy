//file:noinspection GrPackage

package de.honoka.test.spring.groovy.dynamic

class Class2 {

    static String fun1() {
        return "from Class1: ${Class1.fun1()}"
    }
}