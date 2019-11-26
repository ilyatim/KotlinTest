package ta_practice.test

import ta_practice.util.Lexeme
import ta_practice.data.Token


fun main()
{
    val text: String = "10+20"
    val lexev = Lexeme(text)
    val tlist: MutableList<Token> = lexev.getLex()
    for(t: Token in tlist)
    {
        println("${t.type} ${t.text}")
    }
}