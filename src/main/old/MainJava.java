import core.GameJava;

import static core.Constants.WHITE_TEXT;

public class MainJava {
    public static void main(String[] args){
        GameJava game = new GameJava();

        while (!game.isGameOver()){
            GameJava.clearScreen();
            System.out.println(WHITE_TEXT +"score: " + game.getScore() + "\tmoves:" + game.getMoves());
            game.getTable().printTable();
            String nextMove = game.readCommand();
            if(!nextMove.isEmpty()) {
                game.parseMove(nextMove);
            }
        }
        GameJava.clearScreen();
        System.out.println(WHITE_TEXT +"Game Over\nscore: " + game.getScore() + " moves:" + game.getMoves());

    }
}
