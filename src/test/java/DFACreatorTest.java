/*
    Author:    Mohamed Haji
    Created:   07.02.2022
    Edited From:

 */

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DFACreatorTest {

    @Test
    @Order(1)
    public void InputTest()
    {
        // Erstelle StartPosition
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // Erstelle Tabelle
        FollowPosTableEntry row_1 = new FollowPosTableEntry(1, "a");
        row_1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row_2 = new FollowPosTableEntry(2, "b");
        row_2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row_3 = new FollowPosTableEntry(3, "a");
        row_3.followpos.add(4);

        FollowPosTableEntry row_4 = new FollowPosTableEntry(4, "b");
        row_4.followpos.add(5);

        FollowPosTableEntry row_5 = new FollowPosTableEntry(5, "b");
        row_5.followpos.add(6);

        FollowPosTableEntry row_6 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> list = Arrays.asList(row_1, row_2, row_3, row_4, row_5, row_6);

        SortedMap<Integer, FollowPosTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowPosTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // Erstelle Erwartungstabelle
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>();

        DFAState state_1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState state_2 = new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4)));
        DFAState state_3 = new DFAState(3, false, new HashSet<>(Arrays.asList(1,2,3,5)));
        DFAState state_4 = new DFAState(4, true, new HashSet<>(Arrays.asList(1,2,3,6)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', state_2);
        innerEntries.put('b', state_1);
        stateTransitionTable.put(state_1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', state_2);
        innerEntries.put('b', state_3);
        stateTransitionTable.put(state_2, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', state_2);
        innerEntries.put('b', state_4);
        stateTransitionTable.put(state_3, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', state_2);
        innerEntries.put('b', state_1);
        stateTransitionTable.put(state_4, innerEntries);

        // Starte DEA-Erzeuger und fange an zu vergleichen
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }


    @Test
    @Order(2)
    public void Progress()
    {
        // Erstelle erste Position
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // Erstelle Tabelle
        FollowPosTableEntry row_1 = new FollowPosTableEntry(1, "a");
        row_1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row_2 = new FollowPosTableEntry(2, "b");
        row_2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row_3 = new FollowPosTableEntry(3, "c");
        row_3.followpos.addAll(Arrays.asList(4,5));

        FollowPosTableEntry row_4 = new FollowPosTableEntry(4, "d");
        row_4.followpos.addAll(Arrays.asList(4,5));

        FollowPosTableEntry row_5 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> list = Arrays.asList(row_1, row_2, row_3, row_4, row_5);

        SortedMap<Integer, FollowPosTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowPosTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>();

        DFAState state_1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState state_2 = new DFAState(2, true, new HashSet<>(Arrays.asList(4,5)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', state_1);
        innerEntries.put('b', state_1);
        innerEntries.put('c', state_2);
        innerEntries.put('d', null);
        stateTransitionTable.put(state_1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', state_2);
        stateTransitionTable.put(state_2, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }


    /**
     * Selbst gewaehltes Beispiel
     */
    @Test
    @Order(3)
    public void FinalTest()
    {
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3));

        // create FollowposTable
        FollowPosTableEntry row_1 = new FollowPosTableEntry(1, "a");
        row_1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row_2 = new FollowPosTableEntry(2, "b");
        row_2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowPosTableEntry row_3 = new FollowPosTableEntry(3, "c");
        row_3.followpos.addAll(Arrays.asList(4));

        FollowPosTableEntry row_4 = new FollowPosTableEntry(4, "d");
        row_4.followpos.addAll(Arrays.asList(5,6));

        FollowPosTableEntry row_5 = new FollowPosTableEntry(5, "e");
        row_5.followpos.addAll(Arrays.asList(5,6));

        FollowPosTableEntry row_6 = new FollowPosTableEntry(6, "#");

        List<FollowPosTableEntry> list = Arrays.asList(row_1, row_2, row_3, row_4, row_5, row_6);

        SortedMap<Integer, FollowPosTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowPosTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>();

        DFAState state_1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState state_2 = new DFAState(2, false, new HashSet<>(Arrays.asList(4)));
        DFAState state_3 = new DFAState(3, true, new HashSet<>(Arrays.asList(5,6)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', state_1);
        innerEntries.put('b', state_1);
        innerEntries.put('c', state_2);
        innerEntries.put('d', null);
        innerEntries.put('e', null);
        stateTransitionTable.put(state_1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', state_3);
        innerEntries.put('e', null);
        stateTransitionTable.put(state_2, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', null);
        innerEntries.put('e', state_3);
        stateTransitionTable.put(state_3, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }
}
