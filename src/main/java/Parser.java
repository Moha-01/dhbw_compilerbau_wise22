/*
    Author: Franziska Rerich
    Created: 09.02.2022
    Edited From:

 */

public class Parser {
    private int position;
    private final String eingabe;

    public Parser(String eingabe) {
        this.eingabe = eingabe;
        this.position = 0;

    }
    public Visitable start (Visitable parameter) {
        if (eingabe.charAt(position) == '(') {
            match('('); /** check if the first enter '(', than inkrement. when not throw exception*/
            Visitable leaf = regexp(null);
            match(')'); /** check if the first enter ')', than inkrement. when not throw exception*/
            match('#'); /** check if the first enter '#', than inkrement. when not throw exception*/
            assertEndOfInput();/*in the same method */ /** check if reach the end, when not then throw exception */

            //return = new BinOpNode('°', RegExp.return, leaf)
            return new BinOpNode("°", leaf, new OperandNode("#")); /**leaf = new OperandNode '#'*/
        }
        else if (eingabe.charAt(position) == '#'){
            return new OperandNode("#"); /** Start.return = root*/
        }
        else {
            throw new RuntimeException("Syntax error !");
        }
    }
    /**one method per nonterminal
     nonterminal Start

     i) only that method publicy
     ii) only checking in the same method*/

    private Visitable regexp (Visitable parameter) {
        if (Character.isLetterOrDigit(eingabe.charAt(position)) || eingabe.charAt(position) == '(') {
            return regexpEnd(term(null)); /**Term.parameter = null*/
        }
        else {
            throw new RuntimeException("Syntax error !");/**error if Term not null*/
        }
    }
    private Visitable regexpEnd(Visitable parameter) {
        if (eingabe.charAt(position) == '|') {
            match('|');
            Visitable extendTree = new BinOpNode("|", parameter, term(null)); /**root = new BinOpNode ('|', RE'.parameter, Term.return)*/
            return regexpEnd(extendTree);/**RE'.parameter = root*/
        }
        else if (eingabe.charAt(position) == ')') {
            return parameter;/**RE'.return = RE'.return*/
        }
        else {
            throw new RuntimeException("Syntax error !");
        }
    }

    /**
     *
     * @param parameter
     * @return
     *
     * nonterminal symbolic on the right-> positionindex not allowed to be inkrement
     *
     * if terminal symbolic, then check current input, when true then inkrement
     *
     * (when the productionrule is a empty word = factor)
     */

    private Visitable term (Visitable parameter) {
        if (Character.isLetterOrDigit(eingabe.charAt(position)) || eingabe.charAt(position) == '(')  {
            if (parameter != null) { /**Term.parameter != null*/
                return term(new BinOpNode("°", parameter, factor(null))); /**root = BinOpNode('°', Term.parameter, Factor.return)*/
            }

            return term(factor(null));/**Term.parameter = root*/
        }
        else if (eingabe.charAt(position) == '|' || eingabe.charAt(position) == ')') {
            return parameter; /**Term_1.parameter = Factor.return; Term.return = Term_1.return*/
        }
        else {
            throw new RuntimeException("Syntax error !");
        }
    }
    private Visitable factor (Visitable parameter) {
        if (Character.isLetterOrDigit(eingabe.charAt(position)) || eingabe.charAt(position) == '(') {
            return HOp (elem(null)); /**Factor.return = HOp.return*/
        }
        else {
            throw new RuntimeException("Syntax error !");
        }
    }
    private Visitable HOp (Visitable parameter) {
        if (Character.isLetterOrDigit(eingabe.charAt(position)) || eingabe.charAt(position) == '(' || eingabe.charAt(position) == '|' || eingabe.charAt(position) == ')') {
            return parameter;
        }
        else if (eingabe.charAt(position) == '*') {
            match('*');
            return new UnaryOpNode("*", parameter); /**HOp.return = new UnaryOpNode('*', HOp.parameter);*/
        }
        else if (eingabe.charAt(position) == '+') {
            match('+');
            return new UnaryOpNode("+", parameter); /**HOp.return = new UnaryOpNode('+', HOp.parameter);*/
        }
        else if (eingabe.charAt(position) == '?') {
            match('?');
            return new UnaryOpNode("?", parameter); /**HOp.return = new UnaryOpNode('?', HOp.parameter);*/
        }
        else {
            throw new RuntimeException("Syntax error!");
        }
    }
    private Visitable elem (Visitable parameter) {
        if (Character.isLetterOrDigit(eingabe.charAt(position))) {
            return alphanum(null); /**Alphanum.parameter = null
             *  Elem.return = Alphanum.return
             * */
        }
        else if (eingabe.charAt(position) == '(') {
            match('(');
            Visitable leaf = regexp(null);/**RegExp.parameter = null
             *  Elem.return = RegExp.return
             * */
            match(')');
            return leaf;
        }
        else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>Alphanum</b>
     */
    private Visitable alphanum (Visitable parameter) {

        if (!Character.isLetterOrDigit(eingabe.charAt(position))) {
            throw new RuntimeException("An alphanumeric symbol is a letter or a digit");
        }

        OperandNode symbolNode = new OperandNode(String.valueOf(eingabe.charAt(position))); /**Alphanum.return = new OperandNode('A');*/
        match(eingabe.charAt(position));

        return symbolNode;
    }

    /**
     * Tests the next symbol in the input matches the given input and consumes it
     *
     * @param symbol
     *         The character that should be next in input
     */
    private void match (char symbol) {
        if ((eingabe == null) || ("".equals(eingabe))) {
            throw new RuntimeException("Syntax error!");
        }
        if (position >= eingabe.length()) {
            throw new RuntimeException("End of input reached!");
        }
        if (eingabe.charAt(position) != symbol) {
            throw new RuntimeException("Syntax error!");
        }
        position++;
    }

    /**
     * i) rule Start -> '(' RegExp ')''#'<br>
     * ii) rule Start -> '#'<br>
     * iii) otherwise not required for any other rule
     */
    //Start -> '(' RegExp ')''#'
    //
    private void assertEndOfInput () {
        if (position < eingabe.length()) {
            throw new RuntimeException("No end of input reached!");
        }
    }
}
