import java.util.ArrayList;


public class move {
	public int row;
	public int col;
	public int score = 0;
	
	public move(int r, int c){
		setMove(r,c);
	}
	
	public void setMove(int r, int c){
		row = r;
		col = c;
		score = 0;
	}
	
	public void setScore(int s){
		score = s;
	}
	
	public int getScore(){
		return score;
	}
	
	private ArrayList<move> findAvailableMoves(ticTacToe game){
		System.out.println("Adding moves at level 2!");
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
	
	public String toString(){
		return "(" + row + "," + col + ")";
	}
	
	
	
	
}
