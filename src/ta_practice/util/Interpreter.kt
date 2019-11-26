package ta_practice.util

import ta_practice.data.node.*

class Interpreter
{
    private fun eval(n: ExprNode, vars: MutableMap<String, Int>): Int
    {
        return when(n)
        {
            is NumberNode -> Integer.parseInt(n.number.text)
            is VarNode -> vars[n.id.text]!!
            is NegativeNumberNode -> -1 * eval(n.number, vars)
            is BinOpNode -> {
                val l = eval(n.left, vars)
                val r = eval(n.right, vars)
                when(n.op.type)
                {
                    TokenType.ADD -> l + r
                    TokenType.SUB -> l - r
                    TokenType.MUL -> l * r
                    TokenType.DIV -> {
                        if(r == 0)
                        {
                            throw ArithmeticException("Деление на ноль в строке - ${n.op.line}, позиции - ${n.op.column}")
                        }
                        else
                        {
                            return l / r
                        }
                    }
                    else -> throw IllegalArgumentException("Неверный TokenType в строке - ${n.op.line}, позиции - ${n.op.column}")
                }
            }
        }
    }

    fun evalProgram(program: List<StmtNode>, vars: MutableMap<String, Int>)
    {
        for (stmtNode in program)
        {
            evalStatement(stmtNode, vars)
        }
    }
    private fun evalStatement(stmt: StmtNode, vars: MutableMap<String, Int>)
    {
        when(stmt)
        {
            is WhileNode -> {
                while(evalCondition(stmt, vars))
                {
                    for(stmtNode in stmt.body)
                    {
                        evalStatement(stmtNode, vars)
                    }
                }
            }
            is PrintNode -> {
                println(eval(stmt.body, vars))
            }
            is VariableNode -> {
                vars[stmt.name.text] = eval(stmt.body, vars)
            }
        }
    }
    private fun evalCondition(whileNode: WhileNode, vars: MutableMap<String, Int>) : Boolean
    {
        when(val condition = whileNode.condition)
        {
            is BinOpNode -> {
                val p1 = eval(condition.left, vars)
                val p2 = eval(condition.right, vars)
                return when(condition.op.type)
                {
                    TokenType.MORE -> {
                        p1 > p2
                    }
                    TokenType.LESS -> {
                        p1 < p2
                    }
                    TokenType.MOREEQUAL -> {
                        p1 >= p2
                    }
                    TokenType.LESSEQUAL -> {
                        p1 <= p2
                    }
                    TokenType.EQUAL -> {
                        p1 == p2
                    }
                    TokenType.NOTEQUAL -> {
                        p1 != p2
                    }
                    else -> {
                        error("Неверное условие в цикле while." +
                                " Строка - ${whileNode.token.line}," +
                                " Столбец - ${whileNode.token.column}")
                    }
                }
            }
            else -> {
                error("Ожидалось условие в цикле while." +
                        " Строка - ${whileNode.token.line}," +
                        " Столбец - ${whileNode.token.column}")
            }
        }
    }
    private fun error(message: String): Nothing
    {
        throw RuntimeException(message)
    }
}