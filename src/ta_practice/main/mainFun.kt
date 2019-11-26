package ta_practice.main

import ta_practice.data.node.StmtNode
import ta_practice.util.*

fun main()
{
    val text: String = """x = 3;
        |while(x > 1) do 
        |  x = x - 1;
        |  print(x);
        |done;
        |""".trimMargin()
    val l: Lexeme = Lexeme(text)
    val tokens = l.getLex()
    tokens.removeIf { t -> t.type == TokenType.SPACE }
    val p = Parser(tokens)
    val numbers: List<StmtNode> = p.parseProgram()
    Interpreter().evalProgram(numbers, mutableMapOf())
}