/*
  Author:    Lukas
  Created:   DD.MM.YYYY
  Edited From: Lukas, 30.01.2022
*/


import java.util.SortedMap;
import java.util.TreeMap;

public class FollowPosTableGenerator implements Visitor{

  private String konkatenation = "°";
  private String kleenscheHuelle = "*";
  private String postitiveHuelle = "+";


  public SortedMap<Integer, FollowPosTableEntry> followPosTableEntries = new TreeMap<>();

  /**
   * This method start the process which generates the followPos Table
   * @param root root of the syntax tree
   * @return generated followPosTable
   */
  public SortedMap<Integer, FollowPosTableEntry> generate(Visitable root){
    DepthFirstIterator.traverse(root, this);
    return followPosTableEntries;
  }

  /**
   * Create new row inside the table
   * new FollowPosTableEntry with position, symbol and an empty HashSet<Integer>
   * @param node node
   */
  public void visit(OperandNode node){    //leaf node
    followPosTableEntries.put(node.position, new FollowPosTableEntry(node.position, node.symbol));    //add entry in table
  }

  public void visit(BinOpNode node){      //inner node
    if(node.operator == konkatenation){   // check if operator is a concatenation
      SyntaxNode left = (SyntaxNode) node.left;
      SyntaxNode right = (SyntaxNode) node.right;
      for(int i : left.lastpos){          //iterating over each position i being part the last pos set of the left syntaxnode
        followPosTableEntries.get(i).followpos.addAll(right.firstpos);    //followpos(Position i) = followpos(Position i) OR firstpos(rechter Sohnknoten) for every i
      }
    }
    else{
      //no concatenation
    }
  }

  public void visit(UnaryOpNode node){    //kleensche shell and positive shell
    if(node.operator == kleenscheHuelle || node.operator == postitiveHuelle){
      for(int i : node.lastpos){
        followPosTableEntries.get(i).followpos.addAll(node.firstpos);
      }
    }
  }

}
