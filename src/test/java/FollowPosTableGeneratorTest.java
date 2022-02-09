/*Author:    Lukas Hautzinger
        Created:   09.02.2022
        Edited From:  */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class FollowPosTableGeneratorTest {

    private FollowPosTableGenerator followPosGen;

    @Test
    public void createFollowPosTable(){
        // input: ((a|b)*abb)#
        //actualTree
            OperandNode node1 = new OperandNode("a");

        OperandNode node2 = new OperandNode("b");

        BinOpNode altNode = new BinOpNode("|", node1, node2);

        UnaryOpNode kleenNode = new UnaryOpNode("*", altNode);

        OperandNode node3 = new OperandNode("a");

        BinOpNode concatNode1 = new BinOpNode("°", kleenNode, node3);

        OperandNode node4 = new OperandNode("b");

        BinOpNode concatNode2 = new BinOpNode("°", concatNode1, node4);

        OperandNode node5 = new OperandNode("b");

        BinOpNode concatNode3 = new BinOpNode("°", concatNode2, node5);

        OperandNode node6 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°", concatNode3, node6);


        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        altNode = new BinOpNode("|", node1, node2);
        altNode.firstpos.addAll(Arrays.asList(1, 2));
        altNode.lastpos.addAll(Arrays.asList(1, 2));
        altNode.nullable = false;

        kleenNode = new UnaryOpNode("*", altNode);
        kleenNode.firstpos.addAll(Arrays.asList(1, 2));
        kleenNode.lastpos.addAll(Arrays.asList(1, 2));
        kleenNode.nullable = true;

        node3 = new OperandNode("a");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        concatNode1 = new BinOpNode("°", kleenNode, node3);
        concatNode1.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode1.lastpos.add(3);
        concatNode1.nullable = false;

        node4 = new OperandNode("b");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        concatNode2 = new BinOpNode("°", concatNode1, node4);
        concatNode2.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode2.lastpos.add(4);
        concatNode2.nullable = false;

        node5 = new OperandNode("b");
        node5.position = 5;
        node5.firstpos.add(5);
        node5.lastpos.add(5);
        node5.nullable = false;

        concatNode3 = new BinOpNode("°", concatNode2, node5);
        concatNode3.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode3.lastpos.add(5);
        concatNode3.nullable = false;

        node6 = new OperandNode("#");
        node6.position = 6;
        node6.firstpos.add(6);
        node6.lastpos.add(6);
        node6.nullable = false;

        inputSyntaxTree = new BinOpNode("°", concatNode3, node6);

        BinOpNode expectedSyntaxTree = new BinOpNode("°", concatNode3, node6);
        expectedSyntaxTree.firstpos.addAll(Arrays.asList(1, 2, 3));
        expectedSyntaxTree.lastpos.add(6);
        expectedSyntaxTree.nullable = false;

        //create dummy followPosTable
        FollowPosTableEntry index1 = new FollowPosTableEntry(1, "a");
        index1.followpos.addAll(Arrays.asList(1,2,3));

        FollowPosTableEntry index2 = new FollowPosTableEntry(2, "b");
        index2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry index3 = new FollowPosTableEntry(3, "a");
        index3.followpos.add(4);

        FollowPosTableEntry index4 = new FollowPosTableEntry(4, "b");
        index4.followpos.add(5);

        FollowPosTableEntry index5 = new FollowPosTableEntry(5, "b");
        index5.followpos.add(6);

        FollowPosTableEntry index6 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> dummyList = Arrays.asList(index1, index2, index3, index4, index5, index6);
        SortedMap<Integer, FollowPosTableEntry> dummyMap = new TreeMap<>();

        for(FollowPosTableEntry entry : dummyList){
            dummyMap.put(entry.position, entry);
        }

        followPosGen = new FollowPosTableGenerator();
        followPosGen.generate(inputSyntaxTree);

        Assertions.assertEquals(dummyMap, followPosGen.followPosTableEntries);
    }

}
