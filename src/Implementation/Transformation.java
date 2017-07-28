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
    private HashMap<Integer, HashMap<String, List<State>>> transitionTable; // The first transition table

    public Transformation(List<Transition> transitionList, List<Character> symbolList) {
        this.transitionList = transitionList;
        this.symbolList = symbolList;
        symbolList.add('ε');

        transitionTable = new HashMap<Integer, HashMap<String, List<State>>>();

        createTransitionTable();
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
                            tmpClosure.add(currTransition.getInitialState());
                        }
                    } else {
                        if (currSymbol.equals("ε")) {
                            tmpClosure = new LinkedList<State>();
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

    }
}
