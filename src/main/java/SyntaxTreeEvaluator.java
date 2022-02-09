/*Author:    Dominic Merkle
        Created:   DD.MM.YYYY
        Edited From:  */
public class SyntaxTreeEvaluator implements Visitor {
    private int counter;
    public SyntaxTreeEvaluator() {
        this.counter = 1;
    }
    public void start(Visitable root) {
        DepthFirstIterator.traverse(root, this);
    }
// Declare OperandNode
    @Override
    public void visit(OperandNode node) {
        node.position = counter++;
        if (node.symbol != null) {
            node.nullable = false;
            node.firstpos.add(node.position);
            node.lastpos.add(node.position);
        } else {
            node.nullable = true;
            node.firstpos.add(null);
            node.lastpos.add(null);
        }
    }
    // Declare BinOpNode
    @Override
    public void visit(BinOpNode node) {
        SyntaxNode left, right;
        left = (SyntaxNode) node.left;
        right = (SyntaxNode) node.right;
        if (node.operator.equals("°")) {
            node.nullable = left.nullable && right.nullable;
            if (left.nullable) {
                node.firstpos.addAll(left.firstpos);
                node.firstpos.addAll(right.firstpos);
            } else {
                node.firstpos.addAll(left.firstpos);
            }
            if (right.nullable) {
                node.lastpos.addAll(left.lastpos);
            }
            node.lastpos.addAll(right.lastpos);
        }
        if (node.operator.equals("|")) {
            node.nullable = left.nullable || right.nullable;
            node.firstpos.addAll(left.firstpos);
            node.firstpos.addAll(right.firstpos);
            node.lastpos.addAll(left.lastpos);
            node.lastpos.addAll(right.lastpos);
        }
    }
    // Declare UnaryOpNode
    @Override
    public void visit(UnaryOpNode node) {
        SyntaxNode sub = (SyntaxNode) node.subNode;
        if (node.operator.equals("*")) {
            node.nullable = true;
            node.firstpos.addAll(sub.firstpos);
            node.lastpos.addAll(sub.lastpos);
        }
        if (node.operator.equals("+")) {
            node.nullable = sub.nullable;
            node.firstpos.addAll(sub.firstpos);
            node.lastpos.addAll(sub.lastpos);
        }
        if (node.operator.equals("?")) {
            node.nullable = true;
            node.firstpos.addAll(sub.firstpos);
            node.lastpos.addAll(sub.lastpos);
        }
    }
}