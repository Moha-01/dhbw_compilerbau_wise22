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
   * Neue Zeile in Tabelle anlegen
   * new FollowPosTableEntry mit Position, Symbol und leeres HashSet<Integer>
   * @param node Blattknoten
   */
  public void visit(OperandNode node){    //Blattknoten
    followPosTableEntries.put(node.position, new FollowPosTableEntry(node.position, node.symbol));    //Eintrag in Tabelle
  }

  public void visit(BinOpNode node){      //innerer Knoten
    if(node.operator == konkatenation){   //checken ob es sich beim operator um eine Konkatenation handelt
      SyntaxNode left = (SyntaxNode) node.left;
      SyntaxNode right = (SyntaxNode) node.right;
      for(int i : left.lastpos){          //iterieren über jede Position i von der lastpos Menge der linken SyntaxNode
        followPosTableEntries.get(i).followpos.addAll(right.firstpos);    //followpos(Position i) = followpos(Position i) ODER firstpos(rechter Sohnknoten) für alle i
      }
    }
    else{
      //keine Konkatenation
    }
  }

  public void visit(UnaryOpNode node){    //KleenscheHuelle und PositiveHuelle
    if(node.operator == kleenscheHuelle || node.operator == postitiveHuelle){
      for(int i : node.lastpos){
        followPosTableEntries.get(i).followpos.addAll(node.firstpos);
      }
    }
  }

}
