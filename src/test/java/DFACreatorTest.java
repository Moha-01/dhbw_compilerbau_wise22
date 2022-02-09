/*
    Author:    Mohamed Haji
    Created:   07.02.2022
    Edited From:

 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DFACreatorTest {

    /**
     * Beispiel aus Kapitel 8 der Aufgabenstellung
     */
    @Test
    public void testDFACreator_Skript()
    {
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // create FollowposTable
        FollowPosTableEntry row1 = new FollowPosTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row2 = new FollowPosTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row3 = new FollowPosTableEntry(3, "a");
        row3.followpos.add(4);

        FollowPosTableEntry row4 = new FollowPosTableEntry(4, "b");
        row4.followpos.add(5);

        FollowPosTableEntry row5 = new FollowPosTableEntry(5, "b");
        row5.followpos.add(6);

        FollowPosTableEntry row6 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5, row6);

        SortedMap<Integer, FollowPosTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowPosTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>();

        DFAState s1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState s2 = new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4)));
        DFAState s3 = new DFAState(3, false, new HashSet<>(Arrays.asList(1,2,3,5)));
        DFAState s4 = new DFAState(4, true, new HashSet<>(Arrays.asList(1,2,3,6)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s1);
        stateTransitionTable.put(s1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s3);
        stateTransitionTable.put(s2, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s4);
        stateTransitionTable.put(s3, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s1);
        stateTransitionTable.put(s4, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }


    /**
     * Beispiel aus Protokoll MOS-TINF 19B 29_01_2021.pdf Seite 6
     */
    @Test
    public void testDFACreator_Skript_2()
    {
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // create FollowposTable
        FollowPosTableEntry row1 = new FollowPosTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row2 = new FollowPosTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row3 = new FollowPosTableEntry(3, "c");
        row3.followpos.addAll(Arrays.asList(4,5));

        FollowPosTableEntry row4 = new FollowPosTableEntry(4, "d");
        row4.followpos.addAll(Arrays.asList(4,5));

        FollowPosTableEntry row5 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5);

        SortedMap<Integer, FollowPosTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowPosTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>();

        DFAState s1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState s2 = new DFAState(2, true, new HashSet<>(Arrays.asList(4,5)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', s1);
        innerEntries.put('b', s1);
        innerEntries.put('c', s2);
        innerEntries.put('d', null);
        stateTransitionTable.put(s1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', s2);
        stateTransitionTable.put(s2, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }


    /**
     * Selbst gewaehltes Beispiel
     */
    @Test
    public void testDFACreator_Selbst()
    {
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // create FollowposTable
        FollowPosTableEntry row1 = new FollowPosTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row2 = new FollowPosTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row3 = new FollowPosTableEntry(3, "c");
        row3.followpos.addAll(Arrays.asList(4));

        FollowPosTableEntry row4 = new FollowPosTableEntry(4, "d");
        row4.followpos.addAll(Arrays.asList(5,6));

        FollowPosTableEntry row5 = new FollowPosTableEntry(5, "e");
        row5.followpos.addAll(Arrays.asList(5,6));

        FollowPosTableEntry row6 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5, row6);

        SortedMap<Integer, FollowPosTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowPosTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>();

        DFAState s1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState s2 = new DFAState(2, false, new HashSet<>(Arrays.asList(4)));
        DFAState s3 = new DFAState(3, true, new HashSet<>(Arrays.asList(5,6)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', s1);
        innerEntries.put('b', s1);
        innerEntries.put('c', s2);
        innerEntries.put('d', null);
        innerEntries.put('e', null);
        stateTransitionTable.put(s1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', s3);
        innerEntries.put('e', null);
        stateTransitionTable.put(s2, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', null);
        innerEntries.put('e', s3);
        stateTransitionTable.put(s3, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }
}
