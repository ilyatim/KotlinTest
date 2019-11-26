package ta_practice.data

import ta_practice.util.TokenType

data class Token(val type: TokenType, val text: String, var index: Int, var line: Int, var column: Int)
