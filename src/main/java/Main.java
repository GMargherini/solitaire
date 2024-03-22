import core.Game;

import static core.Constants.WHITE_TEXT;

public class Main {
    public static void main(String[] args){
        Game game = new Game();

        while (!game.isGameOver()){
            Game.clearScreen();
            System.out.println(WHITE_TEXT +"score: " + game.getScore() + "\tmoves:" + game.getMoves());
            game.getTable().printTable();
            String nextMove = game.readCommand();
            if(!nextMove.isEmpty()) {
                game.parseMove(nextMove);
            }
        }
        Game.clearScreen();
        System.out.println(WHITE_TEXT +"Game Over\nscore: " + game.getScore() + " moves:" + game.getMoves());

    }
}
