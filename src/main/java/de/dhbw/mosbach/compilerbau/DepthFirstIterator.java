public class DepthFirstIterator{

  public static void traverse(Visitable root, Visitor visitor){
    if(root instanceof OperandNode){
      root.accept(visitor);
      return;
    }
  }
}
