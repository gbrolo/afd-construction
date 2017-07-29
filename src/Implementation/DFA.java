package Implementation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gabriel Brolo on 28/07/2017.
 */
public class DFA {
    private HashMap<List<State>, HashMap<String, List<State>>> dfaTable;
    private List<List<State>> dfaStates;
    private HashMap<List<State>, Integer> dfaStatesWithNumbering;
    private List<Character> symbolList;
    private List<Transition> transitionsList;
    private List<Integer> finalStates;
    private List<Integer> initialStates;

    public DFA (
            HashMap<List<State>, HashMap<String, List<State>>> dfaTable,
            List<List<State>> dfaStates,
            HashMap<List<State>, Integer> dfaStatesWithNumbering,
            List<Character> symbolList
    ) {
        this.dfaTable = dfaTable;
        this.dfaStates = dfaStates;
        this.dfaStatesWithNumbering = dfaStatesWithNumbering;
        this.symbolList = symbolList;
        this.transitionsList = new LinkedList<>();
        this.finalStates = new LinkedList<>();
        this.initialStates = new LinkedList<>();

        setTransitionsList();
        setInitialAndFinalStates();
    }

    public String extendedDelta(String input) {
        long start = System.nanoTime();
        int currentState = initialStates.get(0);
        for (int i = 0; i < input.length(); i++) {
            String currChar = Character.toString(input.charAt(i)); // current char in sequence
            for (int j = 0; j < transitionsList.size(); j++) {
                if (transitionsList.get(j).getInitialState().getStateId() == currentState) {
                    if (transitionsList.get(j).getTransitionSymbol().equals(currChar)) {
                        currentState = transitionsList.get(j).getFinalState().getStateId();
                        j = transitionsList.size();
                    } else if ((transitionsList.get(j).getInitialState().getNextStates().size() == 0)) {
                        currentState = -1;
                    }
                }
            }
        }
        long finish = System.nanoTime();
        long duration = (finish - start);

        // traverse the list of final states to see if currentState is a final state
        String isInLanguage = "";
        if(finalStates.contains(currentState)) {
            isInLanguage = " the string belongs to the language.";
        } else {
            isInLanguage = " the string does not belong to the language.";
        }

        String output = "The input string's final state in the DFA is " + currentState + ", therefore" + isInLanguage + "\n" +
                "DFA search took " + duration + "ns.";
        return output;
    }

    private void setTransitionsList () {
        for (int i = 0; i < dfaStates.size(); i++) {
            List<State> currStateList = dfaStates.get(i);
            int currStateListID = dfaStatesWithNumbering.get(currStateList);
            State initialState = new State(currStateListID, true);

            HashMap<String, List<State>> currStateListInfo = dfaTable.get(currStateList);
            for (int j = 0; j < symbolList.size()-1; j++) {
                String currSymbol = Character.toString(symbolList.get(j));
                List<State> currStateListSymbolList  = currStateListInfo.get(currSymbol);

                if (currStateListSymbolList.size() > 0) {
                    State finalState = new State(dfaStatesWithNumbering.get(currStateListSymbolList), true);
                    Transition tmpTransition = new Transition(currSymbol, initialState, finalState);

                    // now add to transition list
                    if (!transitionsList.contains(tmpTransition)) {
                        transitionsList.add(tmpTransition);
                    }
                }
            }
        }
    }

    private void setInitialAndFinalStates() {
        for (int i = 0; i < dfaStates.size(); i++) {
            List<State> currStateList = dfaStates.get(i);
            for (int j = 0; j < currStateList.size(); j++) {
                if (currStateList.get(j).getFinal()) {
                    finalStates.add(dfaStatesWithNumbering.get(currStateList));
                }
                if (currStateList.get(j).getInitial()) {
                    initialStates.add(dfaStatesWithNumbering.get(currStateList));
                }
            }
        }
    }

    public List<Character> getSymbolList () {
        if (symbolList.contains('ε')) {
            symbolList.remove(symbolList.indexOf('ε'));
        }
        return  this.symbolList;
    }

    public List<Transition> getTransitionsList () { return this.transitionsList; }
    public List<Integer> getFinalStates () { return this.finalStates; }
    public List<Integer> getInitialStates () { return this.initialStates; }
    public HashMap<List<State>, Integer> getDfaStatesWithNumbering () { return this.dfaStatesWithNumbering; }
}
