package ta_practice.util

import ta_practice.data.node.*


class Interpreter {
    /**
     * analysis and implementation of the code passed as a parameter in the form of a list of node
     * @param program - List of node
     * @param vars - Map of variables
     */
    fun evalProgram(program: List<StmtNode>, vars: MutableMap<String, Int>) {
        for (stmtNode in program) {
            evalStatement(stmtNode, vars)
        }
    }
    /**
     * analysis and implementation of the code passed as a parameter
     * @param stmt - node
     * @param vars - Map of variables
     */
    private fun evalStatement(stmt: StmtNode, vars: MutableMap<String, Int>) {
        when (stmt) {
            is WhileNode    -> {
                while (evalCondition(stmt, vars)) {
                    for (stmtNode in stmt.body) {
                        evalStatement(stmtNode, vars)
                    }
                }
            }
            is PrintNode    -> {
                println(evalExpression(stmt.body, vars))
            }
            is VariableNode -> {
                vars[stmt.name.text] = evalExpression(stmt.body, vars)
            }
        }
    }
    /**
     * evaluating a while loop expression
     * @param whileNode
     * @param vars - Map of variables
     */
    private fun evalCondition(whileNode: WhileNode, vars: MutableMap<String, Int>) : Boolean {
        when (val condition = whileNode.condition) {
            is BinOpNode  -> {
                val p1 = evalExpression(condition.left, vars)
                val p2 = evalExpression(condition.right, vars)
                return when (condition.op.type) {
                    TokenType.MORE      -> {
                        p1 > p2
                    }
                    TokenType.LESS      -> {
                        p1 < p2
                    }
                    TokenType.MOREEQUAL -> {
                        p1 >= p2
                    }
                    TokenType.LESSEQUAL -> {
                        p1 <= p2
                    }
                    TokenType.TWOEQUAL  -> {
                        p1 == p2
                    }
                    TokenType.NOTEQUAL  -> {
                        p1 != p2
                    }
                    else                -> {
                        error("Неверное условие в цикле while." +
                                " Строка - ${whileNode.token.line}," +
                                " Столбец - ${whileNode.token.column}")
                    }
                }
            }
            is NumberNode -> {
                return evalExpression(condition, vars) > 0
            }
            else          -> {
                error("Ожидалось условие в цикле while." +
                        " Строка - ${whileNode.token.line}," +
                        " Столбец - ${whileNode.token.column}")
            }
        }
    }
    /**
     * throw RuntimeException with your message
     */
    private fun error(message: String): Nothing {
        throw RuntimeException(message)
    }

    /**
     * evaluating expression
     * @param n - ExprNode
     * @param vars - Map of variables
     */
    private fun evalExpression(n: ExprNode, vars: MutableMap<String, Int>): Int {
        return when (n) {
            is NumberNode          -> Integer.parseInt(n.number.text)
            is VarNode             -> vars[n.id.text]!!
            is NegativeNumberNode  -> -1 * evalExpression(n.number, vars)
            is BinOpNode           -> {
                val l = evalExpression(n.left, vars)
                val r = evalExpression(n.right, vars)
                when (n.op.type) {
                    TokenType.ADD -> l + r
                    TokenType.SUB -> l - r
                    TokenType.MUL -> l * r
                    TokenType.DIV -> {
                        if (r == 0) {
                            throw ArithmeticException("Деление на ноль в строке - " +
                                    "${n.op.line}, позиции - " +
                                    "${n.op.column}")
                        } else {
                            return l / r
                        }
                    }
                    else          -> {
                        throw IllegalArgumentException("Неверный TokenType в строке - " +
                                "${n.op.line}, позиции - " +
                                "${n.op.column}")
                    }
                }
            }
        }
    }
}