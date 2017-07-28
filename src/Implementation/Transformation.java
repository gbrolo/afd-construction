package Implementation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gabriel Brolo on 27/07/2017.
 */
public class Transformation {
    private List<Transition> transitionList; // Transition List coming from AFN
    private List<Character> symbolList; // AFN's symbol List
    private List<State> finalStates; // AFN's final state
    private List<State> initialState; // AFN's initial state
    private HashMap<Integer, HashMap<String, List<State>>> transitionTable; // The first transition table
    private HashMap<List<State>, HashMap<String, List<State>>> dfaTable; // The reduced transition table for the dfa

    public Transformation(List<Transition> transitionList, List<Character> symbolList, List<State> finalStates, List<State> initialState) {
        this.transitionList = transitionList;
        this.symbolList = symbolList;
        this.finalStates = finalStates;
        this.initialState = initialState;
        symbolList.add('ε');

        transitionTable = new HashMap<Integer, HashMap<String, List<State>>>();
        dfaTable = new HashMap<>();

        createTransitionTable();
        createDfaTable();
    }

    private void createDfaTable() {
        // Do this for the initial state only
        HashMap<String, List<State>> tmpCol = new HashMap<>();
        for (int i = 0; i < this.symbolList.size()-1; i++) {
            List<State> symbolEpsilonKleene = new LinkedList<State>();
            HashMap<String, List<State>> stateInfo = new HashMap<>();
            stateInfo = transitionTable.get(initialState.get(0).getStateId());

            List<State> currSymbolList = stateInfo.get(Character.toString(symbolList.get(i)));
            if (currSymbolList != null) {
                for (int j = 0; j < currSymbolList.size(); j++) {
                    int currentIndex = currSymbolList.get(j).getStateId();
                    symbolEpsilonKleene.addAll(transitionTable.get(currentIndex).get("ε"));
                }
            } else {
                symbolEpsilonKleene.add(null);
            }
            tmpCol.put(Character.toString(symbolList.get(i)), symbolEpsilonKleene);
        }
        dfaTable.put(initialState, tmpCol);
        System.out.println(dfaTable.toString());
    }

    private List<State> eClosure(State initialState, List<State> tmpClosure) {
        // initialState is finalState of current Transition
        for (int i = 0; i < this.transitionList.size(); i++) {
            if (transitionList.get(i).getInitialState() == initialState) {
                if (transitionList.get(i).getTransitionSymbol().equals("ε")) {
                    if (!tmpClosure.contains(transitionList.get(i).getFinalState())) {
                        tmpClosure.add(transitionList.get(i).getFinalState());
                    }
                    eClosure(transitionList.get(i).getFinalState(), tmpClosure);
                }
            }
        }
        return tmpClosure;
    }

    private void createTransitionTable() {
        // start transitionTable only with states
        for (int i = 0; i < this.transitionList.size(); i++) {
            if (!transitionTable.containsKey(transitionList.get(i).getInitialState().getStateId())){
                List<State> tmpClosure = new LinkedList<State>();
                HashMap<String, List<State>> tmpCol = new HashMap<String, List<State>>();
                // traverse symbol list
                for (int j = 0; j < symbolList.size(); j++) {
                    String currSymbol = Character.toString(symbolList.get(j)); // get current symbol in symbolList
                    Transition currTransition = transitionList.get(i);

                    if (currTransition.getTransitionSymbol().equals(currSymbol)) {
                        tmpClosure = currTransition.getInitialState().getNextStates();
                        // if it is epsilon
                        if (currSymbol.equals("ε")) {
                            tmpClosure = eClosure(currTransition.getInitialState(), tmpClosure); // find closures
                            tmpClosure.add(currTransition.getInitialState());
                        }
                    } else {
                        if (currSymbol.equals("ε")) {
                            tmpClosure = new LinkedList<State>();
                            tmpClosure = eClosure(currTransition.getInitialState(), tmpClosure); // find closures
                            tmpClosure.add(currTransition.getInitialState());
                        } else {
                            tmpClosure = null;
                        }
                    }
                    tmpCol.put(currSymbol,tmpClosure);
                }
                transitionTable.put(transitionList.get(i).getInitialState().getStateId(), tmpCol);
            }
        }
        System.out.println(transitionTable.toString());
        // add final state to transitionTable
        List<State> tmpClosure = new LinkedList<State>();
        HashMap<String, List<State>> tmpCol = new HashMap<String, List<State>>();
        for (int k = 0; k < symbolList.size(); k++) {
            if (Character.toString(symbolList.get(k)).equals("ε")) {
                tmpClosure = new LinkedList<State>();
                tmpClosure.add(finalStates.get(0));
            } else {
                tmpClosure = null;
            }
            tmpCol.put(Character.toString(symbolList.get(k)),tmpClosure);
        }
        transitionTable.put(finalStates.get(0).getStateId(), tmpCol);
        System.out.println(transitionTable.toString());
    }
}
