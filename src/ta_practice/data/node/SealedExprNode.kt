package ta_practice.data.node

import ta_practice.data.Token

/**
 * wrapper class
 */
sealed class ExprNode

/**
 * data class
 * @constructor
 * @param op - operation with left and right node
 * @param op - Token stored TokenType, value, index in expression, line in expression, column in expression
 */
data class BinOpNode(var op: Token, var left: ExprNode, var right: ExprNode) : ExprNode()
/**
 * data class
 * @constructor
 * @param number - Token stored TokenType, value, index in expression, line in expression, column in expression
 */
data class NumberNode(var number: Token) : ExprNode()
/**
 * data class
 * @constructor
 * @param id - Token stored TokenType, value, index in expression, line in expression, column in expression
 */
data class VarNode(var id: Token) : ExprNode()
/**
 * data class
 * @constructor
 * @param number - ExprNode that can stored another Node (e.g. NumberNode)
 */
data class NegativeNumberNode(var number: ExprNode) : ExprNode()