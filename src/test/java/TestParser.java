import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

public class TestParser {
    /** J-Unit tests for the Parser*/
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class ParserTest {
        @Test
        public void testEmptyRegEx() {
            Parser testParser = new Parser("#");

            Visitable extendTree = new OperandNode("#");
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }
            /**Test von Klammern*/
        @Test
        public void testRegExpWithBrackets() {
            Parser testParser = new Parser("(b(1)G)#");

            // Manually build Tree
            Visitable extendTree = new OperandNode("b");
            extendTree = new BinOpNode("°", extendTree, new OperandNode("1"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("G"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }
        /**Test der Konkatenation*/
        @Test
        public void testRegExpWithConcatenation() {
            String input = "(aC2)#";
            Parser testParser = new Parser(input);

            // Manually build Tree
            Visitable extendTree = new OperandNode("a");
            extendTree = new BinOpNode("°", extendTree, new OperandNode("C"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("2"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }
        /**Test des Oders*/
        @Test
        public void testRegExpWithAlternative() {
            Parser testParser = new Parser("(D|H|BW)#");

            // Manually build Tree
            Visitable extendTree = new OperandNode("D");
            extendTree = new BinOpNode("|", extendTree, new OperandNode("H"));
            extendTree = new BinOpNode("|", extendTree,
                    new BinOpNode("°", new OperandNode("B"), new OperandNode("W"))
            );
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }
        /**Test der Kleenischen Hülle*/
        @Test
        public void testRegExpWithKleenHull() {
            Parser testParser = new Parser("(AHA*b*)#");

            // Manually build Tree
            Visitable extendTree = new OperandNode("A");
            extendTree = new BinOpNode("°", extendTree, new OperandNode("H"));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("*", new OperandNode("A")));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("*", new OperandNode("b")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }

        @Test
        public void testRegExpWithPositiveHull() {
            Parser testParser = new Parser("(IN+F19+B)#");

            // Manually build Tree
            Visitable extendTree = new OperandNode("I");
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("+", new OperandNode("N")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("F"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("1"));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("+", new OperandNode("9")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("B"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }


        @Test
        public void testRegExpWithOption() {
            Parser testParser = new Parser("(OTT?O)#");

            // Manually build Tree
            Visitable extendTree = new OperandNode("O");
            extendTree = new BinOpNode("°", extendTree, new OperandNode("T"));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("?", new OperandNode("T")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("O"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }

        @Test
        public void testRegExpWithAllOperators() {
            Parser testParser = new Parser("((T|D)e+s(d?)t1*02)#");

            // Manually build Tree
            Visitable extendTree = new BinOpNode("|", new OperandNode("T"), new OperandNode("D"));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("+", new OperandNode("e")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("s"));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("?", new OperandNode("d")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("t"));
            extendTree = new BinOpNode("°", extendTree, new UnaryOpNode("*", new OperandNode("1")));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("0"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("2"));
            extendTree = new BinOpNode("°", extendTree, new OperandNode("#"));

            // Parse input
            Visitable currentTree = testParser.start(null);

            Assertions.assertTrue(Compare.equals(extendTree, currentTree));
        }

        @Test
        public void testInvalidExpressionWithInputAfterHash() {
            Parser testParser = new Parser("(soivo)#oi?");
            Assertions.assertThrows(RuntimeException.class, () -> testParser.start(null));
        }

        @Test
        public void testNullInputIsInvalid() {
            Parser testParser = new Parser(null);
            Assertions.assertThrows(RuntimeException.class, () -> testParser.start(null));
        }

        @ParameterizedTest(name = "#{index} - Test SyntaxError with \"{0}\"")
        @MethodSource("inputProvider")
        public void testInvalidExpressionWithSyntaxErrors(String input) {
            Parser testParser = new Parser(input);
            Assertions.assertThrows(RuntimeException.class, () -> testParser.start(null));
        }

        private List<String> inputProvider() {
            return List.of(
                    // Empty input
                    "",
                    // Invalid Characters after different operators/operands
                    "(%def)#",
                    "(in(f)%)#",
                    "(te~st)#",
                    "(a|[bc])#",
                    "(no*}42)#",
                    "(69+=BieG)#",
                    "(71'?01)#",
                    "(5EB`)#",
                    "(!)#",
                    // Missing Characters
                    "(adf)",
                    "(bc#",
                    "zu)#",
                    "(D34+8(doi*?)#",
                    // Missplaced Characters
                    "(Ab3l)1#"
            );
        }
    }
}
