package ta_practice.util

import javafx.css.Match
import ta_practice.data.Map
import ta_practice.data.Token
import ta_practice.data.node.*
import ta_practice.util.TokenType.*
import java.lang.ArithmeticException
import java.lang.RuntimeException
import java.lang.IllegalArgumentException

class ParserNew(private val tokens: List<Token>)
{
    private var pos = 0

    private fun match(vararg types: TokenType): Token?
    {
        if(pos < tokens.size)
        {
            val token: Token = tokens[pos]
            if(types.contains(token.type))
            {
                pos++
                return token
            }
        }
        return null
    }

    private fun require(vararg types: TokenType): Token
    {
        return match(*types) ?: error("Ожидался ${types.contentToString()}")
    }

    private fun error(message: String): Nothing
    {
        if(pos < tokens.size)
        {
            val textPos = tokens[pos].index
            throw RuntimeException("$message в $textPos позиции")
        }
        else
        {
            throw RuntimeException("$message в конце файла")
        }
    }

    fun parseProgram(): List<StmtNode>
    {
        val condition: MutableList<StmtNode> = mutableListOf()
        while(pos < tokens.size)
        {
            val s = parseStatement()
            condition.add(s)
        }
        return condition
    }

    private fun parseStatement(): StmtNode
    {
        if(match(WHILE) != null)
        {
            val condition = parseCondition()
            require(DO)
            val body: MutableList<StmtNode> = mutableListOf()
            while (match(DONE) == null)
            {
                val s = parseStatement()
                body.add(s)
            }
            require(SEMICOLON)
            return WhileNode(condition, body)
        }
        if(match(PRINT) != null)
        {
            if(match(LPAR) != null)
            {
                val body = parseCondition()
                require(RPAR)
                require(SEMICOLON)
                return PrintNode(body)
            }
        }
        val id = match(ID)
        if(id != null)
        {
            if(match(EQUAL) != null)
            {
                val body = parse()
                require(SEMICOLON)
                return VariableNode(id, body)
            }
        }
        error("Ожидалось While или Print")
    }
    private fun parseCondition(): ExprNode
    {
        return parse()
    }
    private fun parse(): ExprNode
    {
        var e1 = addend()
        var token: Token?
        while(true)
        {
            token = match(ADD, SUB)
            if(token != null)
            {
                val e2 = addend()
                e1 = BinOpNode(token, e1, e2)
                continue
            }
            break
        }
        return e1
    }
    private fun addend(): ExprNode
    {
        var e1 = comparison()
        var token: Token?
        while(true)
        {
            token = match(MUL, DIV)
            if(token != null)
            {
                val e2 = multiplier()
                e1 = BinOpNode(token, e1, e2)
                continue
            }
            break
        }
        return e1
    }
    private fun comparison() : ExprNode
    {
        var e1 = multiplier()
        var token: Token?
        while(true)
        {
            token = match(MORE, LESS, MOREEQUAL, LESSEQUAL)
            if(token != null)
            {
                val e2 = multiplier()
                e1 = BinOpNode(token, e1, e2)
                continue
            }
            break
        }
        return e1
    }
    private fun elem(): ExprNode
    {
        val n = match(NUMBER)
        if(n != null)
        {
            return NumberNode(n)
        }
        val neg = match(SUB)
        if(neg != null)
        {
            val e1 = parse()
            return NegativeNumberNode(e1)
        }
        val id = match(ID)
        if(id != null)
        {
            return VarNode(id)
        }
        error("Ожидалось число или id")
    }
    private fun multiplier(): ExprNode
    {
        return if(match(LPAR) != null)
        {
            val expNode = parse()
            require(RPAR)
            expNode
        }
        else
        {
            elem()
        }
    }
}
