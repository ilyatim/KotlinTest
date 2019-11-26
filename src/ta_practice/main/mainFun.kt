package ta_practice.main

import ta_practice.data.Map
import ta_practice.data.node.ExprNode
import ta_practice.data.node.StmtNode
import ta_practice.util.*

fun main()
{
    val text: String = """x = 3 
        |while (x > 0) do 
        |  print(x); 
        |  x = x - 1; 
        |done; 
        |y = 2 / x;
        |""".trimMargin()
    val l: Lexeme = Lexeme(text)
    val tokens = l.getLex()
    tokens.removeIf { t -> t.type == TokenType.SPACE }
    val p = ParserNew(tokens)
    val numbers: List<StmtNode> = p.parseProgram()
    println(numbers)
    Interpreter().evalProgram(numbers, mutableMapOf())
}