

interface Visitor{
//Node is declared in SyntaxTeeEvaluator and is only used for the interface
  public void visit(OperandNode node);
  public void visit(BinOpNode node);
  public void visit(UnaryOpNode node);
}
