package ta_practice.util

import ta_practice.data.Token
import java.util.regex.Matcher
import java.lang.RuntimeException

/**
 * @constructor
 * @param src - an expression that needs to be broken down into tokens
 */
class Lexeme(private val src: String) {
    var line = 1    // current row in src
    var column = 1  // current column in src

    private var pos = 0
    private val tokens: MutableList<Token> = arrayListOf()

    /**
     * parse token in string
     */
    fun getLex(): MutableList<Token> {
        while (nextToken()) {

        }
        return tokens
    }
    private fun nextToken(): Boolean {
        if (pos >= src.length) return false

        for (tt: TokenType in TokenType.values()) {
            val m: Matcher = tt.pattern.matcher(src)
            m.region(pos, src.length)
            if (m.lookingAt()) {
                val text = m.group()
                val t = Token(tt, text, pos, line, column)
                tokens.add(t)
                pos = m.end()
                if (tt == TokenType.SPACE) {
                    for (c in text) {
                        if (c == '\n') {
                            line++
                            column = 1
                        } else {
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
}