/*
    Author:    Mohamed Haji
    Created:   07.02.2022
    Edited From:

 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class DFACreator {

    private int counter = 1;

    private final Set<Integer> positionsForStartState;
    private final SortedMap<Integer, FollowPosTableEntry> followposTable;
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable;


    /**
     * Man beachte ! Parameter <code>positionsForStartState</code> muss vom Aufrufer
     * mit der firstpos-Menge des Wurzelknotens des Syntaxbaums initialisiert werden !
     */
    public DFACreator (Set<Integer> positionsForStartState, SortedMap<Integer, FollowPosTableEntry> followposTable) {

        this.positionsForStartState = positionsForStartState;
        this.followposTable = followposTable;
        this.stateTransitionTable = new HashMap<>();
    }

    // Übergangsmatrix ausfüllen
    public void populateStateTransitionTable () {

        // ermittle das Alphabet
        Set<String> alphabet = new HashSet<>();
        for (FollowPosTableEntry entry: followposTable.values())
        {
            if (!entry.symbol.equals("#"))
            {
                alphabet.add(entry.symbol);
            }
        }

        // Erstelle eine Liste mit einem Startzustand
        List<DFAState> qStates = new ArrayList<>();
        int posOfTerminatingSymbol = followposTable.lastKey(); // Schluessel des letzten Eintrags
        DFAState startState = new DFAState(
                counter++,
                positionsForStartState.contains(posOfTerminatingSymbol),
                positionsForStartState
        );
        qStates.add(startState);

        // Durchlauf des Algorithmus
        Map<Character, DFAState> innerCell;
        while (!qStates.isEmpty())
        {
            // ersten Zustand aus qStates entnehmen und entfernen
            DFAState currentState = qStates.get(0);
            qStates.remove(0);

            // Erstelle neue Zeile mit einem Schlüssel
            innerCell = new HashMap<>();
            stateTransitionTable.put(currentState, innerCell);

            for (String symbol: alphabet)
            {
                // Folgezustand in eine new Zeile Speichern
                DFAState followState = calculateFollowState(currentState, symbol);
                innerCell.put(symbol.charAt(0), followState);

                // Überprüfen, ob Folgezustand schon erfasst bzw. verarbeitet wurde
                // Falls nicht, am Ende von qStates hinzufügen
                if(followState!=null
                        && !stateTransitionTable.containsKey(followState)
                        && !qStates.contains(followState))
                {
                    qStates.add(followState);
                }
            }
        }
    }

    private DFAState calculateFollowState(DFAState s, String a){

        Set<Integer> nextPositionsSet = new HashSet<>();

        for (FollowPosTableEntry entry : followposTable.values())
        {
            if(s.positionsSet.contains(entry.position) && entry.symbol.equals(a))
            {
                nextPositionsSet.addAll(entry.followpos);
            }
        }

        if(nextPositionsSet.isEmpty())
        {
            return null;
        }

        //Zustand Akzeptieren, wenn nextPositionsSet die Position von # enthält
        boolean isAcceptingState = nextPositionsSet.contains(followposTable.lastKey());

        return new DFAState(counter++, isAcceptingState, nextPositionsSet);
    }

    public Map<DFAState, Map<Character, DFAState>> getStateTransitionTable () {
        return stateTransitionTable;
    }
}