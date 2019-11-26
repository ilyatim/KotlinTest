package ta_practice.data.node

import ta_practice.data.Token

sealed class StmtNode

data class WhileNode(var condition: ExprNode, var body: List<StmtNode>) : StmtNode()
data class PrintNode(var body: ExprNode) : StmtNode()
data class VariableNode(var name: Token, var body: ExprNode): StmtNode()