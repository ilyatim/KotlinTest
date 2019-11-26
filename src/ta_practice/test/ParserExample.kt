package ta_practice.test

import java.lang.RuntimeException

sealed class StateWrapper
{
    enum class State {
        S_0, S_FINAL, S_LOOP
    }
}


class ParserExample(private val symbols: CharArray, var pos: Int = 0)
{
    fun error(message: String)
    {
        throw RuntimeException("$message at $pos")
    }
    fun match(expected: Char): Boolean
    {
        if(pos < symbols.size)
        {
            val c = symbols[pos]
            if(c == expected)
            {
                pos++
                return true
            }
        }
        return false
    }
    fun run1():Boolean
    {
        var s: StateWrapper.State = StateWrapper.State.S_0
        while(pos < symbols.size)
        {
            s = this.newState(s)!!
        }
        return s == StateWrapper.State.S_FINAL
    }
    fun run2()
    {
        while(pos < symbols.size)
        {
            require('+')
            require('ч')
        }
    }
    fun require(c: Char)
    {
        if(!match(c))
        {
            error("Ожидался $c")
        }
    }
    fun newState(s: StateWrapper.State): StateWrapper.State?
    {
        when (s)
        {
            StateWrapper.State.S_0 ->
            {
                return StateWrapper.State.S_0
            }
            StateWrapper.State.S_LOOP ->
            {
                if (match('ч'))
                {
                    return StateWrapper.State.S_FINAL
                }
                error("Ожидалось ч")
            }
            StateWrapper.State.S_FINAL ->
            {
                if (match('+'))
                {
                    return StateWrapper.State.S_LOOP
                }
                error("Ожидалось +")
            }
        }
        return null
    }

}