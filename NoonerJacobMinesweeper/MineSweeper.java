import java.util.Scanner;
import java.io.IOException;

//this class runs the classic game of minesweeper in the console
public class MineSweeper {
	
	//main structure of the game
	public static void main(String[] args) throws IOException {
		
		boolean playAgain = true;
		
		while(playAgain) {
			//ask player for board size and number of mines
			System.out.println("Enter board size as int between 5 and 26: ");
			int boardSize = inputBoardSize();
			
			System.out.println("Enter number of mines as int between 1 and "+((int)Math.pow(boardSize, 2)-1)+": ");
			int numMines = inputNumBombs(boardSize);
			Board.setBombs(numMines);
			//create the board to display the player and the one that shows the mines
			Board displayBoard = new Board(boardSize);
			Board actualBoard = new Board(boardSize);
			displayBoard.displayBoard(true);
			
			//sentinel value
			boolean gameOver = false;
			
			//main game loop
			while(!gameOver) {
				
				//ask for tile and action, then check if action can be done on tile, if not ask again
				boolean invalidInput = false;
				do {
					
					//get player chosen tile
					System.out.println("Entire tile as pair eg, A5: ");
					int[] tile = getPlayerTile(boardSize);
					int row = tile[0];
					int column = tile[1];
					
					//get player chosen tile
					System.out.println("Enter action as 'f' for flag or 'r' for reveal: ");
					char action = getPlayerAction();
					
					//determine if action is valid for tile
					invalidInput = false;
					switch(action) {
					//reveal
					case 'r':
						
						if(displayBoard.getTileValue(column,row) == 'F') {
							System.out.println("Cannot reveal flagged tiles");
							invalidInput=true;
							
						} else if(displayBoard.getTileValue(column, row) <= 57) {
							System.out.println("Cannot reveal already revealed tiles");
							invalidInput = true;
							
						} else {
							
							//fill board with bombs after determining the safe square
							if(actualBoard.getTileValue(0,0) == '?') {
								int safe = (column*Board.getSize())+row;
								Board tempBoard = new Board(Board.getSize(), Board.getNumBombs(),safe);
								actualBoard = tempBoard;
							}
							
							//reveal tile
							gameOver = !reveal(displayBoard, actualBoard, column, row);
							displayBoard.displayBoard(!gameOver);
							if(checkWin(displayBoard)) {
								gameOver=true;
							}
						}
						
						break;
					//flag
					case 'f':
						//unflag tile
						if(displayBoard.getTileValue(column,row) == 'F') {
							displayBoard.setTileValue(column, row, '?');
							displayBoard.displayBoard(true);
							
						} else if(displayBoard.getTileValue(column, row) <= 57) {
							System.out.println("Cannot flag revealed tiles");
							invalidInput = true;
							
						} else {
							//flag tile
							displayBoard.setTileValue(column, row, 'F');
							displayBoard.displayBoard(true);
						}
						
						break;
					}//switch action
					
				} while(invalidInput);//check valid action for tile
				
			}//game loop
			
			//displays all non flagged bombs and removes incorrect flags once game is over
			for(int i=0; i<Board.getSize(); i++) {
				for(int j=0; j<Board.getSize(); j++) {
					if(actualBoard.getTileValue(i,j) == '*' && displayBoard.getTileValue(i,j) == '?') {
						displayBoard.setTileValue(i,j,'*');
					}
					else if(actualBoard.getTileValue(i, j) != '*' && displayBoard.getTileValue(i, j) == 'F') {
						displayBoard.setTileValue(i, j,'/');
					}
				}
			}//change display board for end of game showing
			
			displayBoard.displayBoard(false);
			
			//ask player if they want to play again
			System.out.println("Play again? y/n: ");
			playAgain = playAgain();
		}//play again loop
	}//main
	
	//reveal chosen tile and surrounding tiles if tile is 0, return false if mine is revealed otherwise true
	public static boolean reveal(Board displayBoard, Board actualBoard, int column, int row) {
		
		boolean successful = true;
		//check if tile is a bomb
		if(actualBoard.getTileValue(column, row) == '*') {
			displayBoard.setTileValue(column, row, '*');
			System.out.println("You Chose a Bomb, Game Over");
			successful = false;
		}
		else {
			//else reveal tile by setting its value to corresponding value in actualboard
			displayBoard.setTileValue(column, row, actualBoard.getTileValue(column, row));
		}
		
		//if revealed tile is 0, reveal all surrounding tiles, if tile is not already revealed to 0 and is in bounds
		if(displayBoard.getTileValue(column, row) == '0') {
			
			if(column != Board.getSize()-1 && displayBoard.getTileValue(column+1,row) != '0') {
				reveal(displayBoard, actualBoard, column+1, row);
			}
			if(column != 0 && displayBoard.getTileValue(column-1,row) != '0') {
				reveal(displayBoard, actualBoard, column-1, row);
			}
			if(row != Board.getSize()-1 && displayBoard.getTileValue(column,row+1) != '0') {
				reveal(displayBoard, actualBoard, column, row+1);
			}
			if(row != 0 && displayBoard.getTileValue(column,row-1) != '0') {
				reveal(displayBoard, actualBoard, column, row-1);
			}
			if(column != Board.getSize()-1 && row != 0 && displayBoard.getTileValue(column+1,row-1) != '0') {
				reveal(displayBoard, actualBoard, column+1, row-1);
			}
			if(column != Board.getSize()-1 && row != Board.getSize()-1 && displayBoard.getTileValue(column+1,row+1) != '0') {
				reveal(displayBoard, actualBoard, column+1, row+1);
			}
			if(column != 0 && row != Board.getSize()-1 && displayBoard.getTileValue(column-1,row+1) != '0') {
				reveal(displayBoard, actualBoard, column-1, row+1);
			}
			if(column !=0 && row !=0 && displayBoard.getTileValue(column-1,row-1) != '0') {
				reveal(displayBoard, actualBoard, column-1, row-1);
			}
		}
		
		return successful;
	}//reveal
	
	//check if the board state is a winning state, return true or false
	public static boolean checkWin(Board displayBoard) {
		
		final int TILES_FOR_WIN = (int)Math.pow(Board.getSize(),2)-Board.getNumBombs();
		int numRevealedTiles = 0;
		
		for(int i=0; i<Board.getSize(); i++) {
			for(int j=0; j<Board.getSize(); j++) {
				char tileValue = displayBoard.getTileValue(i, j);
				if(tileValue != '?' && tileValue != 'F' && tileValue != '*') {
					numRevealedTiles++;
				}
			}
		}//for
		
		if(numRevealedTiles >= TILES_FOR_WIN) {
			System.out.println("You win Yay!!!");
			return true;
		}
		
		return false;
	}//checkwin
	
	//ask user for desired dimension of the board
	public static int inputBoardSize() {
		
		//initialize var and scanner
		int boardSize;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		//attempt to read input, otherwise call function again, recursion collapses once input is valid
		try {
			boardSize = input.nextInt();
			while(boardSize < 5 || boardSize > 26) {
				System.out.println("Invalid size, please try again: ");
				boardSize = input.nextInt();
			}//ensure input is in bounds
		} catch(Exception e) {
			System.out.println("Please enter a number, try again: ");
			boardSize = inputBoardSize();
		}
		
		return boardSize;
	}//inputBoardSize
	
	//ask user for desired number of mines on the board between 1 and half total tiles
	public static int inputNumBombs(int boardSize) {
		
		//initialize var and scanner
		int numMines;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		//attempt to read input, otherwise call function again, recursion collapses once input is valid
		try {
			numMines = input.nextInt();
			while(numMines < 1 || numMines > Math.pow(boardSize, 2)-1) {
				System.out.println("Invalid number of mines, please try again: ");
				numMines = input.nextInt();
			}//ensure input is in bounds
		} catch(Exception inputMismatchException) {
			System.out.println("Please enter a number, try again: ");
			numMines = inputNumBombs(boardSize);
		}
		
		return numMines;
	}//inputNumBombs
	
	//ask user for desired tile and return tile as array
	public static int[] getPlayerTile(int boardSize) {
		
		//initialize var and scanner
		int[] choice = new int[2];
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		//attempt to read input, otherwise call function again, recursion collapses once input is valid
		try {
			String tile = input.next();
			int row = (int)(Character.toUpperCase(tile.charAt(0)))-65;
			int column = (int)tile.charAt(1)-48;
			if(tile.length() > 2) {
				column*=10;
				column += (int)tile.charAt(2)-48;
			}
			while((row<0 || row>boardSize) || (column<0 || column>boardSize)) {
				System.out.println("Invalid pair, please try again: ");
				tile = input.next();
				row = (int)(Character.toUpperCase(tile.charAt(0)))-65;
				column = (int)tile.charAt(1)-48;
				
				if(tile.length() > 2) {
					column*=10;
					column += (int)tile.charAt(2)-48;
				}
			}//ensure input is in bounds
			
			//once input is correct put it in the array
			choice[0]=row;
			choice[1]=column;
		} catch(Exception e) {
			System.out.println("Please enter 2 or more ASCII characters, try again: ");
			choice = getPlayerTile(boardSize);
		}
		
		return choice;
	}//getPlayerTile
	
	//ask user for desired action and return char
	public static char getPlayerAction() {
		
		//initialize var and scanner
		char action;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		//attempt to read input, otherwise call function again, recursion collapses once input is valid
		try {
			action = Character.toLowerCase(input.next().charAt(0));
			while(action != 'r' && action != 'f') {
				System.out.println("Invalid action, please try again: ");
				action = Character.toLowerCase(input.next().charAt(0));
			}//ensure input is in bounds
		} catch(Exception e) {
			System.out.println("Please enter only ASCII characters, try again: ");
			action = getPlayerAction();
		}
		
		return action;
	}//getPlayerAction

	//ask user to play again and return true or false
	public static boolean playAgain() {
		
		//initialize vars and scanner
		boolean playAgain = false;
		char answer;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		//attempt to read input, otherwise call function again, recursion collapses once input is valid
		try {
			answer = input.next().charAt(0);
			if(answer  != 'y' && answer != 'n') {
				System.out.println("Invalid answer, try again");
			}
			switch(answer) {
			case 'y':
				playAgain = true;
				break;
			case 'n':
				playAgain = false;
				break;
			}
		} catch (Exception e) {
			System.out.println("Please enter only ASCII characters, try again: ");
			playAgain = playAgain();
		}
		
		return playAgain;
	}//playAgain
	
}//MineSweeper
