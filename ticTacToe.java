//Ken Schroeder
// August 2nd, 2015, 1101
// Starbucks, 5th Ave Portland, OR
// TicTacToe
// ticTacToe.java

/*------------------------------------------------------------------------------
	Tic-Tac-Toe:
	Program Purpose: 
		This program is intended to create and maintain the board for a game of
		tic-tac-toe. It checks for legal moves and keeps track of won/drawn games.

		Much of the code for this program was taken from a project I did at Seattle
		University in August 2014.

	Instructions:
		This program is a class designed to be run with playTicTacToe.java and 
		CPUPlayer.java. With a few simple modifiations it could be run without the 
		CPU.	
	
	Program Includes The Following Methods
		displayBoard -> Prints the current state of the board to the screen
		resetBoard -> Clears the board and readies it for a new game
		gameStats -> Displays the runnin game stats
		welcome -> Displays a friendly welcome message to the user
		goodBye -> Displays a friendly good by message to the user
		placePiece -> Places a piece on a board, if possible. 
		gameOver -> Checks if the game has ended
		boardFilled -> checks if the board is filled, if it is, game is a draw
		getXPiece -> returns the character of the x piece
		getOPiece -> returns the character of the o piece
		getXMove -> returns the numeric value of the x player
		getOMove -> returns the numeric value of the o player
		isFree -> determines if a row, column pair is free
		getBoardSize -> returns size of board in rows
		getBoard -> returns the game board
		gameWon -> returns whether the game has been won or not


	Implementation and Assumptions:
		The board is an n x n array of ints
		When a player plays a piece, that int value is stored in the array
		When printing the board, number values are turned into characters
		Game uses number values to check for when players have won or have a
			potential win

	Future Development Ideas
		-Copy constructor
		-Better evaluation function

	References
		http://neverstopbuilding.com/minimax
		http://stackoverflow.com/questions/17907255/make-the-computer-never-lose-at-tic-tac-toe
	----------------------------------------------------------------------------*/
public class ticTacToe {
	private final String SEPERATOR = "-----";
	private final int boardSize = 3;
	private boolean gameWon = false;
	private int[][] board;
	private int xWins;
	private int draws;
	private int oWins;
	private char xPiece = 'X';
	private char oPiece = 'O';
	private int xMove = 5;
	private int oMove = 7;
	private boolean firstMoveX = false;
	private int turn = 0;
	private int totalMoves;
	private ticTacToe masterGame;

	//----------------------------------------------------------------------------
	// constructor
	//----------------------------------------------------------------------------
	public ticTacToe(){
		xWins = 0;
		draws = 0;
		oWins = 0;
		board = new int[boardSize][boardSize];
		masterGame = this;
		resetBoard();
	}

	public void copy(ticTacToe t){
		turn = t.turn;
		totalMoves = t.totalMoves;
		masterGame = t.masterGame;
		
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				board[i][j] = t.getBoardVal(i, j);
			}
		}
	}
	//----------------------------------------------------------------------------
	// getXPiece
	// getter function for XPiece variable
	//----------------------------------------------------------------------------
	public char getXPiece(){
		return xPiece;
	}
	
	//----------------------------------------------------------------------------
	// getOPiece
	// getter function for OPiece variable
	//----------------------------------------------------------------------------
	public char getOPiece(){
		return oPiece;
	}
	
	//----------------------------------------------------------------------------
	// getXMove
	// getter function for XMove variable
	//----------------------------------------------------------------------------
	public int getXMove(){
		return xMove;
	}
	
	//----------------------------------------------------------------------------
	// get OMove
	// getter function for YMove variable
	//----------------------------------------------------------------------------
	public int getOMove(){
		return oMove;
	}

	//----------------------------------------------------------------------------
	// getBoardSize
	// getter function for boardSize variable
	//----------------------------------------------------------------------------
	public int getBoardSize(){
		return boardSize;
	}
	
	//----------------------------------------------------------------------------
	// getBoard
	// returns the board, used for CPUPlayer class
	//----------------------------------------------------------------------------
	public int[][] getBoard(){
		return board;
	}
	
	//----------------------------------------------------------------------------
	// isWinner
	// getter function for gameWon variable
	//----------------------------------------------------------------------------
	public boolean isWinner(){
		return gameWon;
	}

	//----------------------------------------------------------------------------
	// welcome
	// prints a welcome message to the screen
	//----------------------------------------------------------------------------
	public void welcome(){
		System.out.println("Welcom to the game of tic-tac-toe");
		System.out.println("The object is to get three ina row");
		System.out.println("If both players are smart, and shart in the eye");
		System.out.println("I regret to say, this will end in a tie");
		System.out.println();

		System.out.println("Player 1, it's your turn!\n\n");
	}

	//----------------------------------------------------------------------------
	// goodBye
	// prints a farewell message to the screen
	//----------------------------------------------------------------------------
	public void goodBye(){
		gameStats();
		System.out.println("Thanks for playing! Good bye!");
	}


	//----------------------------------------------------------------------------
	// displayBoard
	// displays the current state of the board
	// numeric values are turned into X's and O's for display
	//----------------------------------------------------------------------------
	public void displayBoard(){
		System.out.println("Total Moves: " + totalMoves);
		System.out.print("  " + "| ");
		
		for (int k = 0; k < boardSize; k++){
			System.out.print(k + " | ");
		}
		System.out.println();
		//System.out.println(EMPTY + EMPTY);

		for (int w = 0; w < boardSize; w++){
			System.out.print(SEPERATOR);
		}
		System.out.println();

		for (int i = 0; i < boardSize; i++){
			System.out.print(i + " |");
			for (int j = 0; j < boardSize; j++){
				if(board[i][j] == xMove){
					System.out.print(" " + xPiece + " |");
				}
				else if(board[i][j] == oMove){
					System.out.print(" " + oPiece + " |");
				}
				else{
					System.out.print("   |");
				}
			}
			System.out.println();
			for (int h = 0; h < boardSize; h++){
				System.out.print(SEPERATOR);
			}
			System.out.println();
		}
		int position = evaluate();
		System.out.println("\nCurrent Position: " + position);
	}

	//----------------------------------------------------------------------------
	// resetBoard
	// clears the board
	//----------------------------------------------------------------------------
	public void resetBoard(){
		gameWon = false;
		
		if (firstMoveX){
			firstMoveX = false;
			turn = oMove;
		}
		else{
			firstMoveX = true;
			turn = xMove;
		}
		
		totalMoves = 0;
		
		for (int i = 0; i < boardSize; i++){
			for (int j = 0; j < boardSize; j++){
				board[i][j] = 1;
			}
		}
	}

	//----------------------------------------------------------------------------
	// gameStats
	// prints the game stats, wins, draws, losses and win% for player 1
	//----------------------------------------------------------------------------
	public void gameStats(){
		double winPerc = (double)(2*xWins + draws) / (double)(2* (xWins + draws + oWins));
		System.out.println("\n");
		System.out.println("     GAME STATISTICS");
		System.out.println("--------------------------");
		System.out.println("Player 1 Wins:      " + xWins);
		System.out.println("Games Drawn:        " + draws);
		System.out.println("Player 1 Losses:    " + oWins);
		System.out.print("Win Percentage:     ");
		System.out.printf("%.3f", winPerc);
		System.out.println();
		System.out.println("--------------------------\n");
	}

	
	//----------------------------------------------------------------------------
	// placePiece
	// attempts to place a piece on the board, returns false if space is occupied.
	//----------------------------------------------------------------------------
	public boolean placePiece(int piece, int row, int col){
		if (row >= boardSize || col >= boardSize || row < 0  || col < 0){
			return false;
		}

		if(board[row][col] == 1){
			board[row][col] = piece;
			totalMoves++;
			if(turn == xMove){
				turn = oMove;
			}else{
				turn = xMove;
			}
			return true;
		}else{
			System.out.print("That square is already occupied, please select ");
			System.out.print("another square");
			return false;
		}
	}

	//----------------------------------------------------------------------------
	// gameOver
	// checks if the game is over, the game is over if there are no places left to
	//   play, or if a player has 3 in a row
	//----------------------------------------------------------------------------
	public boolean gameOver(int piece){
		int product;
		for(int i = 0; i < boardSize; i++){
			product = rowProduct(i);
			if(product == Math.pow(piece,boardSize)){
				wonGame(piece);
				return true;
			}
			product = colProduct(i);
			if(product == Math.pow(piece,boardSize)){
				wonGame(piece);
				return true;
			}
		}
		product = upperLeftDiagProduct();
		if(product == Math.pow(piece,boardSize)){
			wonGame(piece);
			return true;
		}
		product = upperRightDiagProduct();
		if(product == Math.pow(piece,boardSize)){
			wonGame(piece);
			return true;
		}
		if(boardFilled()){
			draws++;
			System.out.println("It's a draw");
			return true;
		}
		return false;
	}

	//----------------------------------------------------------------------------
	// boardFilled
	// returns true if all spaces on the board are filled
	//----------------------------------------------------------------------------
	public boolean boardFilled(){
		return totalMoves == 9;
	}

	public int getBoardVal(int row, int col){
		return board[row][col];
	}
		
	//----------------------------------------------------------------------------
	// isFree
	// returns true if a position on the board is free
	//----------------------------------------------------------------------------
	public boolean isFree(int row, int col){
		if (board[row][col] == 1){
			System.out.println("(" + row + "," + col + ") is free.");
			return true;
		}
		return false;
	}

	//----------------------------------------------------------------------------
	// rowProduct
	// returns the product of a row
	// if the product == the piece cubed, the game has been won
	// if the product == the piece squared, the game has the potential to be won
	//----------------------------------------------------------------------------
	public int rowProduct(int row){
		int product = 1;
		for (int i = 0; i < boardSize; i++){
			product *= board[row][i];
		}
		return product;
	}

	//----------------------------------------------------------------------------
	// colProduct
	// returns the product of a column
	// if the product == the piece cubed, the game has been won
	// if the product == the piece squared, the game has the potential to be won
	//----------------------------------------------------------------------------
	public int colProduct(int col){
		int product = 1;
		for (int i = 0; i < boardSize; i++){
			product *= board[i][col];
		}
		return product;
	}

	//----------------------------------------------------------------------------
	// upperLeftDiagProduct
	// returns the product of the upper left to bottom right diagonal
	// if the product == the piece cubed, the game has been won
	// if the product == the piece squared, the game has the potential to be won
	//----------------------------------------------------------------------------
	public int upperLeftDiagProduct(){
		int product = 1;
		for (int i = 0; i < boardSize; i++){
			product *= board[i][i];
		}
		return product;
	}

	//----------------------------------------------------------------------------
	// upperRightDiagProduct
	// returns the product of the upper right to bottom left diagonal
	// if the product == the piece cubed, the game has been won
	// if the product == the piece squared, the game has the potential to be won
	//----------------------------------------------------------------------------
	public int upperRightDiagProduct(){
		int product = 1;
		for (int i = 0; i < boardSize; i++){
			product *= board[2-i][i];
		}
		return product;
	}

	//----------------------------------------------------------------------------
	// wonGame
	// Marks a game as won
	//----------------------------------------------------------------------------
	private void wonGame(int piece){
		if (piece == xMove){
			xWins++;
		}
		else{
			oWins++;
		}
		gameWon = true;
	}

	public int evaluate(){
		//find the forks, the forks are key!
		int position = 0;
		int oneInARow = 3;
		int twoInARow = 10;
		int threeInARow = 100;
		int fork = 50;
		
		for (int i = 0; i < boardSize; i++){
			if(rowProduct(i) == xMove){
				position += oneInARow;
			}
			if(rowProduct(i) == Math.pow(xMove,2)){
				position += twoInARow;
			}
			if(rowProduct(i) == Math.pow(xMove,3)){
				//position = threeInARow;
				return threeInARow;
			}
			if(rowProduct(i) == oMove){
				position -= oneInARow;
			}
			if(rowProduct(i) == Math.pow(oMove,2)){
				position -= twoInARow;
			}
			if(rowProduct(i) == Math.pow(oMove,3)){
				//position -= threeInARow;
				return 0 - threeInARow;
			}
			if(colProduct(i) == xMove){
				position += oneInARow;
			}
			if(colProduct(i) == Math.pow(xMove,2)){
				position += twoInARow;
			}
			if(colProduct(i) == Math.pow(xMove,3)){
				//position += threeInARow;
				return threeInARow;
			}
			if(colProduct(i) == oMove){
				position -= oneInARow;
			}
			if(colProduct(i) == Math.pow(oMove,2)){
				position -= twoInARow;
			}
			if(colProduct(i) == Math.pow(oMove,3)){
				//position -= threeInARow;
				return 0-threeInARow;
			}
		}

		if(upperLeftDiagProduct() == xMove){
				position += oneInARow;
		}
		if(upperLeftDiagProduct() == Math.pow(xMove,2)){
			position += twoInARow;
		}
		if(upperLeftDiagProduct() == Math.pow(xMove,3)){
			return threeInARow;
		}
		if(upperLeftDiagProduct() == oMove){
			position -= oneInARow;
		}
		if(upperLeftDiagProduct() == Math.pow(oMove,2)){
			position -= twoInARow;
		}
		if(upperLeftDiagProduct() == Math.pow(oMove,3)){
			return 0 - threeInARow;
		}
		if(upperRightDiagProduct() == xMove){
				position += oneInARow;
		}
		if(upperRightDiagProduct() == Math.pow(xMove,2)){
			position += twoInARow;
		}
		if(upperRightDiagProduct() == Math.pow(xMove,3)){
			return threeInARow;
		}
		if(upperRightDiagProduct() == oMove){
			position -= oneInARow;
		}
		if(upperRightDiagProduct() == Math.pow(oMove,2)){
			position -= twoInARow;
		}
		if(upperRightDiagProduct() == Math.pow(oMove,3)){
			return 0 - threeInARow;
		}	

		return position;
	}
	
	public void placePiece(move M, int whosMove){
		//System.out.println("Placing piece " + M.toString());
		placePiece(whosMove, M.row, M.col);
	}
	
	public void removePiece(move M){
		board[M.row][M.col] = 1;
		totalMoves--;
	}
	
	public boolean isGameOver(){
		int xWins = xMove * xMove * xMove;
		int oWins = oMove * oMove * oMove;
		
		if(boardFilled()){
			return true;
		}
		
		for (int i = 0; i < 3; i++){
			if(rowProduct(i) == xWins || rowProduct(i) == oWins){
				return true;
			}
			else if(colProduct(i) == xWins || colProduct(i) == oWins){
				return true;
			}
		}
		if(upperLeftDiagProduct() == xWins || upperLeftDiagProduct() == oWins){
			return true;
		}
		if(upperRightDiagProduct() == xWins || upperRightDiagProduct() == oWins){
			return true;
		}
		
		return false;
	}

	public int getTurn(){
		return turn;
	}
	
	public boolean isMasterGame(){
		return masterGame == this;
	}
	
}
