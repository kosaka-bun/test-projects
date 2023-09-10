package de.honoka.test.various

import de.marhali.json5.Json5
import de.marhali.json5.Json5Options
import org.junit.Test

class KotlinAllTest {

    @Test
    fun test1() {
        val json = """
            {
              // comments
              unquoted: 'and you can quote me on that',
              singleQuotes: 'I can use "double quotes" here',
              lineBreaks: "Look, Mom! \
            No \\n's!",
              hexadecimal: 0xdecaf,
              leadingDecimalPoint: .8675309, andTrailing: 8675309.,
              positiveSign: +1,
              trailingComma: 'in objects', andIn: ['arrays',],
              "backwardsCompatible": "with JSON",
            }
        """.trimIndent()
        val json5Utils = Json5(Json5Options(
            false, false, false, 4
        ))
        val element = json5Utils.parse(json)
        println(element.asJson5Object.get("hexadecimal").asInt)
        println(json5Utils.serialize(element))
    }
}