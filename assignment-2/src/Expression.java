/**
 * Created By: Alex Vallejo
 * Date: 9/14/13
 * Project: assignment-2
 * Email: amv49@pitt.edu
 * Peoplesoft: 357-8411
 */

public class Expression {

  private boolean value;
  private String line;
  private String rawLine;
  private boolean evaluated;
  Node root;

  public Expression(String line) throws ParseError{
    evaluated = false;
    this.rawLine = line;

    line = line.replaceAll(" ", "");
    this.line = line;

    this.root = buildTree(line);

  }

  //=================================
  //Methods Required by the assignment
  //==================================

  public static void setAtom(String atom, String value) {
  }

  public boolean evaluate() {
    //ToDo actually evaluate the value!
    return false;
  }

  public Expression copy() throws ParseError{
    return new Expression(this.rawLine);
  }

  public void normalize() {
  }

  public void displayNormalized() {
    TreeDisplay display = new TreeDisplay(this.rawLine);
    display.setRoot(this.root);
  }

  public String toString() {
    if (!this.evaluated)
      evaluate();

    return this.rawLine + " = " + this.value;
  }

  //==================================
  //Custom helper methods!
  //==================================

  private Node buildTree(String line) throws ParseError{

    line = rmParenthesis(line);

    //Base case: line => {atom}
    if (isAtom(line))
      return new Node(line);

    //line => !{exp}
    if(line.charAt(0) == '!')
      return new Node(strFrmChar('!'), null, buildTree(line.substring(1,
          line.length())));

    char first, op, last;
    first = line.charAt(0);
    op = line.charAt(1);
    last = line.charAt(2);

    //Base case: line => {exp} {op} {exp}
    if (isAtom(first) && isOp(op) && isAtom(last))
      return new Node(strFrmChar(op), new Node(first), new Node(last));

    return new Node(op(line),buildTree(leftExp(line)),buildTree(rightExp(line)));
  }

  /**
   * Returns the operator of an expression
   * @param line the expression to be examined for an operator
   * @return the operator found within line
   * @throws ParseError thrown if the operator is not found
   */
  private String op(String line) throws ParseError{

    int open = 0, closed = 0, pos = 0;

    do {
      char c = line.charAt(pos);

      if ('(' == c)
        open++;

      else if (')' == c)
        closed++;

      if (open == closed && open > 0)
        return strFrmChar(line.charAt(pos + 1));

      pos++;
    } while (pos < line.length());

    throw new ParseError("Parenthesis mismatch: " + line);
  }

  /**
   * Returns the left expression of the larger expression
   * @param line the line the left expression wil lbe extracted from
   * @return the left expression of the larger expression
   * @throws ParseError thrown if a left expression is not found
   */
  private String leftExp(String line) throws ParseError{
    int open = 0, closed = 0, pos = 0;

    do {
      char c = line.charAt(pos);

      if ('(' == c)
        open++;

      else if (')' == c)
        closed++;

      if (open == closed && open > 0)
        return line.substring(0, pos + 1); //ToDo includes parenthesis

      pos++;

    } while (pos < line.length());

    throw new ParseError("Parenthesis Mismatch: " + line);
  }

  /**
   * Extracts the right expression from a larger expression
   * @param line the line the right expression will be extracted from
   * @return the right expression in line
   * @throws ParseError thrown if no right expression is found
   */
  private String rightExp(String line) throws ParseError{
    int open = 0, closed = 0, pos = line.length() - 1;

    do {
      char c = line.charAt(pos);

      if ('(' == c)
        open++;

      else if (')' == c)
        closed++;

      if (open == closed && open > 0)
        return line.substring(pos, line.length()); //ToDo include parenthesis

      pos--;
    } while (pos >= 0);

    throw new ParseError("Parenthesis mismatch: " + line);
  }

  /**
   * Essentially, a missing constructor for string. Creates a string from a
   * single char
   * @param c the char to become the string
   * @return a string containing only c
   */
  private String strFrmChar(char c){
    String str = new String();
    str += c;
    return str;
  }

  /**
   * Determines if a character is an operator or not according to the grammar
   * defined by the assignment
   * @param c the character to be assessed
   * @return true if c is an operator, false otherwise
   */
  private boolean isOp(char c){
    if (c == 'v' || c == '^')
      return true;

    else
      return false;
  }

  /**
   * Decide if a character is an atom or not
   * @param c the character to be assessed
   * @return true if c is an atom, false otherwise
   */
  private boolean isAtom(char c){
    return Character.isLetter(c);
  }

  /**
   * Determines if the input is an atom or not
   * @param line the expression that is to be evaluated
   * @return true if an atom, false otherwise!
   */
  private boolean isAtom(String line){
    if (line.length() > 1)
      return false;

    char firstChar = line.charAt(0);

    return Character.isLetter(firstChar);
  }

  /**
   * Removes the parenthesis around an expression. EX: (AvB) -> AvB
   * @param line the line to be stripped
   * @return the expression without the enclosing parenthesis
   * @throws ParseError Thrown if parenthesis do not match on the ends of the
   * expression
   */
  private String rmParenthesis(String line) throws ParseError{

    //Base case: => {atom}
    if (isAtom(line))
      return line;

    //Base case: => !{exp}
    if (line.charAt(0) == '!')
      return line;

    char first, op, second;
    first = line.charAt(0);
    op = line.charAt(1);
    second = line.charAt(2);

    //Base case: => {atom} {exp} {atom}
    if (isAtom(first) && isOp(op) && isAtom(second))
      return line;

    //If valid, line MUST be of this form: => ({exp} {op} {exp})
    if (line.charAt(0) == '(' && ')' == line.charAt(line.length() - 1))
      return line.substring(1,line.length() - 1);

    throw new ParseError("Parenthesis mismatch: " + line);
  }
}
