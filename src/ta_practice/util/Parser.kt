package ta_practice.util

import ta_practice.data.Token
import ta_practice.data.node.*
import ta_practice.util.TokenType.*
import java.lang.RuntimeException

class Parser(private val tokens: List<Token>) {
    private var pos = 0

    fun parseProgram(): List<StmtNode> {
        val statements: MutableList<StmtNode> = mutableListOf()
        while (pos < tokens.size) {
            val s = parseStatement()
            statements.add(s)
        }
        return statements
    }
    private fun match(vararg types: TokenType): Token? {
        if (pos < tokens.size) {
            val token: Token = tokens[pos]
            if (types.contains(token.type)) {
                pos++
                return token
            }
        }
        return null
    }
    private fun require(vararg types: TokenType): Token {
        return match(*types) ?: error("Ожидался ${types.contentToString()}")
    }
    private fun error(message: String): Nothing {
        if (pos < tokens.size) {
            throw RuntimeException(message +
                    " в строке - ${tokens[pos - 1].line}," +
                    " позиции - ${tokens[pos - 1].column}")
        } else {
            throw RuntimeException("$message в конце выражения")
        }
    }
    private fun parseStatement(): StmtNode {
        val token: Token? = match(WHILE, PRINT, ID)
        token?.let {
            when (token.type) {
                WHILE -> {
                    val condition = parseCondition()
                    require(DO)
                    val body: MutableList<StmtNode> = mutableListOf()
                    while (match(DONE) == null) {
                        if (pos < tokens.size) {
                            val s = parseStatement()
                            body.add(s)
                        } else {
                            error("Ожидался $DONE")
                        }
                    }
                    require(SEMICOLON)
                    return WhileNode(token, condition, body)
                }
                PRINT -> {
                    if (match(LPAR) != null) {
                        val body = parseCondition()
                        require(RPAR)
                        require(SEMICOLON)
                        return PrintNode(token, body)
                    }
                }
                ID    -> {
                    if (match(EQUAL) != null) {
                        val body = parseExpression()
                        require(SEMICOLON)
                        return VariableNode(token, body)
                    } else if (match(TWOEQUAL) != null) {
                        error("Сравнение не является присвоением")
                    }
                }
                else -> {
                    return@let
                }
            }
        }
        error("Ожидалось While, Print или Variable")
    }
    private fun parseCondition(): ExprNode = parseExpression()
    private fun parseExpression(): ExprNode = comparison()
    private fun comparison() : ExprNode {
        var e1 = expression()
        var token: Token?
        while (true) {
            token = match(MORE, LESS, TWOEQUAL, MOREEQUAL, LESSEQUAL, NOTEQUAL)
            if (token != null) {
                val e2 = expression()
                e1 = BinOpNode(token, e1, e2)
                continue
            }
            break
        }
        return e1
    }
    private fun expression(): ExprNode {
        //TODO fix this path
        var e1 = addend()
        var token: Token?
        while (true) {
            token = match(ADD, SUB)
            if (token != null) {
                val e2 = addend()
                e1 = BinOpNode(token, e1, e2)
                continue
            }
            break
        }
        return e1
    }
    private fun addend() : ExprNode {
        var e1 = multiplier()
        var token: Token?
        while (true) {
            token = match(MUL, DIV)
            if (token != null) {
                val e2 = multiplier()
                e1 = BinOpNode(token, e1, e2)
                continue
            }
            break
        }
        return e1
    }
    private fun multiplier(): ExprNode {
        return if (match(LPAR) != null) {
            val expNode = parseExpression()
            require(RPAR)
            expNode
        } else {
            elem()
        }
    }
    private fun elem(): ExprNode {
        val token = match(NUMBER, SUB, ID)
        token?.let {
            when (token.type) {
                NUMBER -> {
                    return NumberNode(it)
                }
                SUB    -> {
                    val e1 = multiplier()
                    return NegativeNumberNode(e1)
                }
                ID     -> {
                    return VarNode(it)
                }
                else   -> {
                    return@let
                }
            }
        }
        error("Ожидалось число или id")
    }
}
