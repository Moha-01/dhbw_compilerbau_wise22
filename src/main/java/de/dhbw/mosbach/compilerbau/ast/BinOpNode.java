package de.dhbw.mosbach.compilerbau.ast;

import de.dhbw.mosbach.compilerbau.visit.Visitable;
import de.dhbw.mosbach.compilerbau.visit.Visitor;

public class BinOpNode extends SyntaxNode implements Visitable {
    public String operator;
    public Visitable left;
    public Visitable right;

    public BinOpNode(String operator, Visitable left, Visitable right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
