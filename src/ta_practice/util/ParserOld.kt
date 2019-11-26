package ta_practice.util

import ta_practice.data.Token
import ta_practice.data.node.*
import ta_practice.util.TokenType.*
import java.lang.ArithmeticException
import java.lang.RuntimeException
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class ParserOld(private val tokens: List<Token>)
{

    private var pos = 0

    object Result
    {
        fun eval(n: ExprNode): Int
        {
            return when(n)
            {
                is NumberNode -> Integer.parseInt(n.number.text)
                is VarNode -> {
                    Integer.parseInt(n.id.text)
                }
                is NegativeNumberNode -> -1 * eval(n.number)
                is BinOpNode -> {
                    val l = eval(n.left)
                    val r = eval(n.right)
                    when(n.op.type)
                    {
                        ADD -> l + r
                        SUB -> l - r
                        MUL -> l * r
                        DIV -> {
                            if(r == 0)
                            {
                                throw ArithmeticException("Деление на ноль")
                            }
                            else
                            {
                                return l / r
                            }
                        }
                        else -> throw IllegalArgumentException("Неверный TokenType")
                    }
                }
            }
        }
    }

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
            val textPos = tokens[pos].index;
            throw RuntimeException("$message в $textPos позиции")
        } else
        {
            throw RuntimeException("$message в конце файла")
        }
    }

    fun parse(): ExprNode
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
        var e1 = multiplier()
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
