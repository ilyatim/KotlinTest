package ta_practice.main

import ta_practice.data.node.StmtNode
import ta_practice.util.*

fun main() {
    val text: String = """x = -(1 + 1) - 1;
        |print(x);
        |""".trimMargin()
    val l: Lexeme = Lexeme(text)
    val tokens = l.getLex()
    tokens.removeIf { t -> t.type == TokenType.SPACE }
    val p = Parser(tokens)
    val numbers: List<StmtNode> = p.parseProgram()
    println(numbers)
    Interpreter().evalProgram(numbers, mutableMapOf())
}