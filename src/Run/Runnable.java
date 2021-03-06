package Run;

import Implementation.AFN;
import Implementation.DFA;
import Implementation.Transformation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Runnable.
 * Use this file to generate @AFN.txt, the textfile with the contents of the AFN.
 * Created by Gabriel Brolo on 22/07/2017.
 *
 * NOTE:
 * Valid characters:
 *      Use any symbol rather than '|', '*', '+', '?', '^', '.'
 *      You MUST use ε in your expression for representation of an empty word. (Just copy it from here)
 *
 * Example of valid regexps:
 *      (a|ε)b(a+)c?
 *      (b|b)*abb(a|b)*
 *      (a*|b*)c
 *      (a|b)*a(a|b)(a|b)
 *      b+abc+
 *      ab*ab*
 */
public class Runnable {
    public static String regexp;
    public static AFN afn;
    public static DFA dfa;
    public static void main(String []args) {
        //regexp = "(a|ε)b(a+)c?"; // This is the regexp you need to supply

        // supply the regexp through user input
        System.out.println("Welcome. Please enter a regexp.");
        System.out.println("NOTE:\n" +
                " * Valid characters:\n" +
                " *      Use any symbol rather than '|', '*', '+', '?', '^', '.'\n" +
                " *      You MUST use ε in your expression for representation of an empty word. (Just copy it from here) \n" +
                " * Example of regexps: \n" +
                " *      ab*ab* \n" +
                " *      0?(1|ε)?0* \n" +
                " * Accepts abbreviations and concatenation by yuxtaposition \n" +
                "Enter your regexp after this line:");
        Scanner sc = new Scanner(System.in);

        regexp = sc.nextLine();
        System.out.println("File written to: your_current_directory/AFN.txt");
        System.out.println("© 2017. brolius (Gabriel Brolo)");

        writeFile();

        boolean finish = false;

        while(!finish) {
            System.out.println("Please insert a string to test if it belongs to the language generated by the NFA/DFA");
            String input = sc.nextLine();
            // do stuff
            System.out.println(dfa.extendedDelta(input));
            System.out.println(afn.extendedDelta(input));

            System.out.println("Do you wish to add another string? (1 yes, 0 no)");
            input = sc.nextLine();

            if (input.equals("0")) {
                finish = true;
            }
        }
    }

    /**
     * Writes @AFN.txt
     */
    public static void writeFile() {
        try{
            long afnStartTime = System.nanoTime();
            afn = new AFN(regexp);
            long afnEndTime = System.nanoTime();
            long duration = (afnEndTime - afnStartTime)/ 1000000; // time in miliseconds

            PrintWriter writer = new PrintWriter("AFN.txt", "UTF-8");
            writer.println("REGULAR EXPRESSION: "+regexp);
            writer.println("REGULAR EXPRESSION IN POSTFIX: "+afn.getPostFixRegExp());
            writer.println("SYMBOL LIST: "+afn.getSymbolList());
            writer.println("TRANSITIONS LIST: "+afn.getTransitionsList());
            writer.println("FINAL STATE: "+afn.getFinalStates());
            writer.println("STATES: "+afn.getStates());
            writer.println("INITIAL STATE: "+afn.getInitialState());
            writer.println("GENERATION TIME: "+duration + " ms");
            writer.close();

            Transformation transformation = new Transformation(afn.getTransitionsList(),afn.getSymbolList(), afn.getFinalStates(), afn.getInitialState());

            long dfaStartTime = System.nanoTime();
            dfa = new DFA(transformation.getDfaTable(), transformation.getDfaStates(),transformation.getDfaStatesWithNumbering(),transformation.getSymbolList());
            long dfaEndTime = System.nanoTime();
            long dfaDuration = (dfaEndTime - dfaStartTime);

            PrintWriter dfaWriter = new PrintWriter("DFA.txt", "UTF-8");
            dfaWriter.println("REGULAR EXPRESSION: "+regexp);
            dfaWriter.println("REGULAR EXPRESSION IN POSTFIX: "+afn.getPostFixRegExp());
            dfaWriter.println("SYMBOL LIST: "+dfa.getSymbolList());
            dfaWriter.println("STATES ([NFA STATE LIST]=DFA STATE ID): "+dfa.getDfaStatesWithNumbering());
            dfaWriter.println("TRANSITIONS LIST: "+dfa.getTransitionsList());
            dfaWriter.println("FINAL STATE(S): "+dfa.getFinalStates());
            dfaWriter.println("INITIAL STATE: "+dfa.getInitialStates());
            dfaWriter.println("GENERATION TIME: "+dfaDuration + " ns");
            dfaWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
