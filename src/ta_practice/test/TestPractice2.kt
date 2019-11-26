package ta_practice.test

import java.util.regex.Matcher
import java.util.regex.Pattern

fun main()
{
    val p: Pattern = Pattern.compile("[A-Z_a-z][A-Z_a-z 0-9]")
    val text: String = "x1=x2-z37"
    val matcher: Matcher = p.matcher(text)
    while(matcher.find())
    {
        val id = matcher.group()
        println(id)
        val s: Int = matcher.start()
        val e: Int = matcher.end()
    }
}
