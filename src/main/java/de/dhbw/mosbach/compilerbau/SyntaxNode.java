package de.dhbw.mosbach.compilerbau;
import java.util.HashSet;
import java.util.Set;

/*
        * Author:    Lukas
        * Created:   28.01.2022
        *
*/

public abstract class SyntaxNode {
    public Boolean nullable;
    public final Set<Integer> firstpos = new HashSet<>();
    public final Set<Integer> lastpos = new HashSet<>();
}

public class OperandNode extends SyntaxNode implements Visitable {
    public int position;
    public String symbol;

    public OperandNode(String symbol){
        position = -1;      //bedeutet: noch nicht initialisiert
        this.symbol = symbol;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}

public class BinOpNode extends SyntaxNode implements Visitable{
    public String operator;
    public Visitable left;
    public Visitable right;

    public BinOpNode(String operator, Visitable left, Visitable right){
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}

public class UnaryOpNode extends SyntaxNode implements Visitable{
    public String operator;
    public Visitable subNode;

    public UnaryOpNode(String operator, Visitable subNode){
        this.operator = operator;
        this.subNode = subNode;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}