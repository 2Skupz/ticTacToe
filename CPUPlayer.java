import java.util.ArrayList;

//Ken Schroeder
// August 2nd, 2015, 1101
// Starbucks, 5th Ave Portland, OR
// TicTacToe
// CPUPlayer.java

/*------------------------------------------------------------------------------
	Tic-Tac-Toe:
	Program Purpose: 
		This program is intended to simulate artificial intelligence in the game of
		tic-tac-toe. The computer will search unti the end of the game and choose
		the best move via the minimax method.

	Instructions:
		This program is a class designed to be run with playTicTacToe.java and 
		ticTacToe.java. 
	
	Program Includes The Following Methods
		Public:
			getMove -> runs the minimax method to find a move and plays it
		Private:
			minimax -> runs the minimax algorithm to find the best move
		

	Implementation and Assumptions:
		The computer plays perfect tic-tac-toe and sees the game through to the end
		The computer does not care about winning quickly, all it cares about is the
			end score

	Future Development Ideas
		-Recommend moves for the human player
		-Keep track of possible winning positions, expected value of moves
		-Have the computer choose a move more likely to cause disruption (more win possibilites)
		-Have the computer choose the quickest winning move
		-Alpha Beta Pruning

	References
		http://neverstopbuilding.com/minimax
		http://stackoverflow.com/questions/17907255/make-the-computer-never-lose-at-tic-tac-toe
	----------------------------------------------------------------------------*/

public class CPUPlayer {
	//recommended moves
	//better display (moves that'll win, lose, etc)
	//adjust the score so that a quick win is better than a long win
	//adjust the scores so that a position more likely to result in a win is better
	//randomly choose from all the moves that result in the same score
	
	
	//Member Variable
	private ticTacToe game;
	private int oMove;
	private int xMove;
		
	public CPUPlayer(ticTacToe g){
		game = g;
		xMove = g.getXMove();
		oMove = g.getOMove();
	}
	
	public int minimax(ticTacToe t, int depth){
		int score;
		move bestMove;
		//If the game is won or it is officially a cat (no more moves left), evaluate and return
		if(t.isGameOver() || depth == 0){
				return t.evaluate();
		}
		
		//find all the available moves
		ArrayList<move> availableMoves = findAvailableMoves(t);
		
		//fore very move you found, run the minimax algorithm on it...yep, that's it!
		for (int i = 0; i < availableMoves.size(); i++){
			ticTacToe possibleGame = new ticTacToe();
			possibleGame.copy(t);
			possibleGame.placePiece(availableMoves.get(i), possibleGame.getTurn());
			availableMoves.get(i).setScore(minimax(possibleGame, depth - 1));
		}
		
		printPossibleMoves(t,availableMoves);
		bestMove = getBestMove(t.getTurn(),availableMoves);
		t.placePiece(bestMove,t.getTurn());
		return bestMove.getScore();
	
		/*
		//Initialize the score
		score = availableMoves.get(0).getScore();
		//bestMove = availableMoves.get(0);
		
		//Initialize the ArrayList that will hold all the best moves
		//Best Moves = moves that return the best score
		ArrayList<move> bestPossibleMoves = new ArrayList<move>();
		
		//find the best score for x, looking for the biggest score for x
		if(t.getTurn() == t.getXMove()){
			//find the best score
			for (int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() > score){
					//bestMove = availableMoves.get(i);
					score = availableMoves.get(i).getScore();
				}
			}
			//add all scores that equal the best score to the bestPossibleMoves Array
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() == score){
					bestPossibleMoves.add(availableMoves.get(i));
				}
			}
			//randomly choose a "best" move
			int randomMove = (int)(Math.random() * bestPossibleMoves.size());
			//place that piece
			t.placePiece(availableMoves.get(randomMove), t.getTurn());
			return score;
		}
		//Repeat, but with o
		else{
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() < score){
					//bestMove = availableMoves.get(i);
					score = availableMoves.get(i).getScore();
				}
			}
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() == score){
					bestPossibleMoves.add(availableMoves.get(i));
				}
			}
			
			int randomMove = (int)(Math.random() * bestPossibleMoves.size());
			
			t.placePiece(bestPossibleMoves.get(randomMove), t.getTurn());
			// should I return the move which has the score?
			return score;	
		}
		*/
	}	
	
	
	//Alpha Beta
	//https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe_AI.html
	public int alphaBeta(ticTacToe t, int depth, int alpha, int beta){
		//initialize score variable -- why?
		int score;
		
		//If the game is won or it is officially a cat (no more moves left), evaluate and return
		if(t.isGameOver() || depth == 0){
			return t.evaluate();
		}
		
		//find all the available moves
		ArrayList<move> availableMoves = findAvailableMoves(t);
		
		//fore very move you found, run the minimax algorithm on it...yep, that's it!
		for (int i = 0; i < availableMoves.size(); i++){
			ticTacToe possibleGame = new ticTacToe();
			possibleGame.copy(t);
			possibleGame.placePiece(availableMoves.get(i), possibleGame.getTurn());
			availableMoves.get(i).setScore(alphaBeta(possibleGame, depth - 1,alpha,beta));
			if(possibleGame.getTurn() == xMove){
				if(availableMoves.get(i).getScore() > alpha){
					alpha = availableMoves.get(i).getScore();
				}
			}
			else{
				if(beta < availableMoves.get(i).getScore()){
					beta = availableMoves.get(i).getScore();
				}
			}
			
			if(alpha >= beta){
				System.out.println("****\n\n**Alpha >= Beta\n\n****");
				break;
			}
		}
		
		//this code is just for fun, it displays all the possible moves and their respective scores
		if(t.isMasterGame()){
			System.out.println("---------------------------");
			t.displayBoard();
			System.out.println("The moves for this board are :");
			for (int i = 0; i < availableMoves.size(); i++){
				System.out.println("Move #" + i + availableMoves.get(i).toString() + " Score: " + availableMoves.get(i).getScore());
			}
				System.out.println("---------------------------");
		}
			
		//Initialize the score
		score = availableMoves.get(0).getScore();
		//bestMove = availableMoves.get(0);
	
		//Initialize the ArrayList that will hold all the best moves
		//Best Moves = moves that return the best score
		ArrayList<move> bestPossibleMoves = new ArrayList<move>();
		
		//find the best score for x, looking for the biggest score for x
		if(t.getTurn() == t.getXMove()){
			//find the best score
			for (int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() > score){
					//bestMove = availableMoves.get(i);
					score = availableMoves.get(i).getScore();
				}
			}
			//add all scores that equal the best score to the bestPossibleMoves Array
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() == score){
					bestPossibleMoves.add(availableMoves.get(i));
				}
			}
			//randomly choose a "best" move
			int randomMove = (int)(Math.random() * bestPossibleMoves.size());
			//place that piece
			t.placePiece(availableMoves.get(randomMove), t.getTurn());
			return score;
		}
		//Repeat, but with o
		else{
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() < score){
					//bestMove = availableMoves.get(i);
					score = availableMoves.get(i).getScore();
				}
			}
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() == score){
					bestPossibleMoves.add(availableMoves.get(i));
				}
			}
			
			int randomMove = (int)(Math.random() * bestPossibleMoves.size());
			
			t.placePiece(bestPossibleMoves.get(randomMove), t.getTurn());
			// should I return the move which has the score?
			return score;	
		}
	}
		
	private ArrayList<move> findAvailableMoves(ticTacToe game){
		//System.out.println("Adding all available moves");
		ArrayList<move> movesToMake = new ArrayList<move>();
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if(game.getBoardVal(i, j) == 1){
					move newMove = new move(i,j);
					movesToMake.add(newMove);
				}
			}
		}
		return movesToMake;
	}
	
	//----------------------------------------------------------------------------
	// getMove
	// find CPU move and move there!
	// logic for move is:
	//   1. Can CPU win
	//   2. Can opponent win
	//   3. Move at random
	//----------------------------------------------------------------------------
	public void getMove(){
		System.out.println("\nLooking for minimax");
		minimax(game, 8);
		//alphaBeta(game,2,-10000,10000);
	}
	
	private void printPossibleMoves(ticTacToe t, ArrayList<move> availableMoves){
		//this code is just for fun, it displays all the possible moves and their respective scores
		if(t.isMasterGame()){
			System.out.println("---------------------------");
			t.displayBoard();
			System.out.println("The moves for this board are :");
			for (int i = 0; i < availableMoves.size(); i++){
				System.out.println("Move #" + i + availableMoves.get(i).toString() + " Score: " + availableMoves.get(i).getScore());
			}

			System.out.println("---------------------------");
		}
	}
	
	private move getBestMove(int turn, ArrayList<move> availableMoves){
		int bestScore = availableMoves.get(0).getScore();
		ArrayList<move> bestPossibleMoves = new ArrayList<move>();
		if(turn == xMove){
			for(int i = 1; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() > bestScore){
					bestScore = availableMoves.get(i).getScore();
				}
			}
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() == bestScore){
					bestPossibleMoves.add(availableMoves.get(i));
				}
			}
		}
		else{
			for(int i = 1; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() < bestScore){
					bestScore = availableMoves.get(i).getScore();
				}
			}
			for(int i = 0; i < availableMoves.size(); i++){
				if(availableMoves.get(i).getScore() == bestScore){
					bestPossibleMoves.add(availableMoves.get(i));
				}
			}
		}
		
		//randomly choose a "best" move
		int randomMove = (int)(Math.random() * bestPossibleMoves.size());
		
		return bestPossibleMoves.get(randomMove);
	}
	
}