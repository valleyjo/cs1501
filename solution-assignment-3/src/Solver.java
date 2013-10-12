import java.util.*;

public class Solver {
  private LinkedList<Board> goalSequence;
  private boolean solvable = false;
  int moves = 0;

  public Solver(Board initial)
  {
    goalSequence = new LinkedList<Board>();
    PriorityQueue<mNode> pq = new PriorityQueue<mNode>();
    PriorityQueue<mNode> clonepq = new PriorityQueue<mNode>();

    pq.add(new mNode(initial, moves, null));
    clonepq.add(new mNode(initial.cloneAndSwap(), moves, null));

    Board prevBoard = initial;

    while (!pq.isEmpty() && !clonepq.isEmpty()) {
      mNode min = pq.remove();
      Iterable<Board> neighbors = min.board.neighbors();

      if (min.prev != null)
        prevBoard = min.prev.board;

      for (Board board : neighbors) {
        if (!prevBoard.equals(board)) {
        pq.add(new mNode(board, min.numMoves + 1, min));
        }
      }

      if (min.board.isGoal()){
        this.moves = min.numMoves;
        solvable = true;
        goalSequence(min);
        break;
      }

      min = clonepq.remove();
      Iterable<Board> cloneNeighbors = min.board.neighbors();

      if (min.prev == null)
        prevBoard = min.board;
      else
        prevBoard = min.prev.board;

      for (Board board : cloneNeighbors)
        if (!prevBoard.equals(board))
        clonepq.add(new mNode(board, min.numMoves + 1, min));

      if (min.board.isGoal()){
        solvable = false;
        this.moves = -1;
        break;
      }
    }
  }

  private void goalSequence(mNode step) {

    while (step.prev != null) {
      goalSequence.addFirst(step.board);
      step = step.prev;
      //minMoves++;
    }
  }

  public boolean isSolvable(){
    return this.solvable;
  }

  public int moves(){
    return this.moves;
  }

  public Iterable<Board> solution(){
    if (this.moves != -1)
      return goalSequence;

    else
      return null;
  }

  private class mNode implements Comparable<mNode> {
    private int numMoves;
    private Board board;
    private mNode prev;

    public mNode(Board Board, int numMoves, mNode prev) {
      this.numMoves = numMoves;
      this.board = Board;
      this.prev = prev;

    }

    public int compareTo(mNode that) {
      if (this.board.manhattan() + this.numMoves > that.board.manhattan() +
          that.numMoves)
        return 1;

      else if (this.board.manhattan() + this.numMoves < that.board.manhattan()
          + that.numMoves)
        return -1;

      else return 0;
    }

  }

  public static void main(String[] args)
  {
    long startTime = System.currentTimeMillis();

    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();

    int blocks[][] = new int[N][N];

    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
        blocks[i][j] = in.readInt();

    Board initial = new Board(blocks);
    Solver Solver = new Solver(initial);

    // print solution to standard output
    if (!Solver.isSolvable())
      StdOut.println("No solution possible");

    else {
      StdOut.println("Minimum number of moves = " + Solver.moves());

      for (Board Board : Solver.solution())
        StdOut.println(Board);
    }

    long endTime   = System.currentTimeMillis();
    double totalTime = (double)(endTime - startTime)/1000.0;
    System.out.println("Time elapsed => " + totalTime);
  }
}
