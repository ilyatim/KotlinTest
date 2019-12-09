package ta_practice.data.node

import ta_practice.data.Token

/**
 * wrapper class
 */
sealed class StmtNode

/**
 * data class
 * @constructor
 * @param token - Token stored TokenType, value, index in expression, line in expression, column in expression
 */
data class WhileNode(var token: Token, var condition: ExprNode, var body: List<StmtNode>) : StmtNode()
/**
 * data class
 * @constructor
 * @param token - Token stored TokenType, value, index in expression, line in expression, column in expression
 */
data class PrintNode(var token: Token, var body: ExprNode) : StmtNode()
/**
 * data class
 * @constructor
 * @param name - Token stored TokenType, value, index in expression, line in expression, column in expression
 */
data class VariableNode(var name: Token, var body: ExprNode): StmtNode()