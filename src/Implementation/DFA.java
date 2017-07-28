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

        setTransitionsList();
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
        System.out.println(transitionsList.toString());
    }
}
