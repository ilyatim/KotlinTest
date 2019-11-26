package ta_practice.util

import ta_practice.data.Token
import java.util.regex.Matcher
import java.lang.RuntimeException

class Lexeme(private val src: String)
{
    var pos = 0
    var line = 1
    var column = 1

    private val tokens: MutableList<Token> = ArrayList()

    private fun nextToken(): Boolean
    {
        if(pos >= src.length)
        {
            return false
        }
        for(tt: TokenType in TokenType.values())
        {
            val m: Matcher = tt.pattern.matcher(src)
            m.region(pos, src.length)
            if(m.lookingAt())
            {
                val text = m.group()
                val t = Token(tt, text, pos, line, column)
                tokens.add(t)
                pos = m.end()
                if(tt == TokenType.SPACE)
                {
                    for (c in text)
                    {
                        if(c == '\n')
                        {
                            line++
                            column = 1
                        }
                        else
                        {
                            column++
                        }
                    }
                } else {
                    column += text.length
                }
                return true
            }
        }
        throw RuntimeException("unknown symbol")
    }
    fun getLex(): MutableList<Token>
    {
        while(nextToken()) {}
        return tokens
    }

}