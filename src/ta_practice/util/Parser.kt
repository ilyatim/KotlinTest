package ta_practice.util

import ta_practice.data.Token
import ta_practice.data.node.*
import ta_practice.util.TokenType.*
import java.lang.RuntimeException

/**
 * Constructor
 * @param tokens - List of tokens of the expression
 */
class Parser(private val tokens: List<Token>) {
    /**
     * field denoting current position in list tokens
     */
    private var pos = 0

    /**
     * Analysis program
     * @return List of nodes
     */
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

    /**
     * @param types - one or more token types
     * @return match token in tokens
     */
    private fun require(vararg types: TokenType): Token {
        return match(*types) ?: error("Ожидался ${types.contentToString()}")
    }
    /**
     * throw RuntimeException with your message
     * shows position in expression
     */
    private fun error(message: String): Nothing {
        if (pos < tokens.size) {
            throw RuntimeException(message +
                    " в строке - ${tokens[pos - 1].line}," +
                    " позиции - ${tokens[pos - 1].column}")
        } else {
            throw RuntimeException("$message в конце выражения")
        }
    }
    /**
     * parse statement with WHILE, PRINT, ID
     * @return node with the found TokenType or error
     */
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
                    } else {
                        error("Неверное присвоение переменной - " + token.text)
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
    /**
     * causes expression()
     * @see expression
     * search comparison operators
     * @return one of four node
     */
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
    /**
     * causes addend()
     * @see addend
     * search ADD, SUB operators
     * @return one of four node
     */
    private fun expression(): ExprNode {
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
    /**
     * causes multiplier()
     * @see multiplier
     * search MUL, DIV operators
     * @return one of four node
     */
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
    /**
     * causes elem()
     * @see elem
     * search bracketed expression
     * @return one of four node
     */
    private fun multiplier(): ExprNode {
        return if (match(LPAR) != null) {
            val expNode = parseExpression()
            require(RPAR)
            expNode
        } else {
            elem()
        }
    }
    /**
     * @return ExprNode (number, neg bracketed expression or id) or error
     * @see error
     */
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
