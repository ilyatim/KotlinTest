package ta_practice.data

import ta_practice.util.TokenType

/**
 * @constructor
 * @param type - TokenType of current Token
 * @param text - value stored in Token
 * @param index - index in expression
 * @param line - row in expression
 * @param column - column in expression
 */
data class Token(val type: TokenType, val text: String, var index: Int, var line: Int, var column: Int)
