/*
    Author: Franziska Rerich
    Created: 09.02.2022
    Edited From:

 */



public class Compare {
    public static boolean equals (Visitable v1, Visitable v2) {
        if (v1 == v2)
            return true; /**Checking if Visitable 1 and 2 are equal to each other*/

        if (v1 == null)
            return false;/**Checking if Visitable 1 are equal to null*/

        if (v2 == null)
            return false;/**Checking if Visitable 2 are equal to null*/

        if (v1.getClass() != v2.getClass())
            return false; /**Checking if Visitable 1 and 2 are not equal to each other*/

        if (v1.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) v1;
            OperandNode op2 = (OperandNode) v2;
            return op1.position == op2.position && op1.symbol.equals(op2.symbol);
        }

        if (v1.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) v1;
            UnaryOpNode op2 = (UnaryOpNode) v2;
            return op1.operator.equals(op2.operator) && equals(op1.subNode, op2.subNode);
        }

        if (v1.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) v1;
            BinOpNode op2 = (BinOpNode) v2;
            return op1.operator.equals(op2.operator) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }

        throw new IllegalStateException("Ungueltiger Knotentyp");
    }
}