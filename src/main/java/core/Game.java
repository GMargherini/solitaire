package core;

import deck.*;
import piles.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static core.Constants.*;

public class Game {
    private int score;
    private int moves;
    private Table table;
    private boolean gameOver;
    private Scanner in = new Scanner(System.in);

    public Game(){
        score=0;
        moves=0;
        gameOver=false;
        table = new Table();
    }

    private void move(int number, Pile from, Pile to, String input){
        try{
            if(input.equals("D")) {
                table.drawCard();
            }
            else if(input.equals("Q")){
                clearScreen();
                System.exit(0);
            }
            else{
                boolean isValid=checkScore(number,from,to);
                Table.moveCards(number,from, to);
                updateScore(number, from, to, isValid);
            }
            moves++;
        }catch (InvalidMoveException | IndexOutOfBoundsException e){
            System.err.println(RED_TEXT +"Illegal move, press enter to continue"+ WHITE_TEXT);
            in.nextLine();
        }
    }

    public void autoMove(Pile from, Pile to){
        try{
            for(int i=1;i<=from.getSize();i++){
                try{
                    boolean checkScore=checkScore(i,from,to);
                    Table.moveCards(i,from,to);
                    updateScore(i, from, to, checkScore);
                    moves++;
                    i = from.getSize();
                }catch (InvalidMoveException ignored){
                    if(i == from.getSize()){
                        throw new InvalidMoveException();
                    }
                }
            }
        }catch (InvalidMoveException e){
            System.err.println(RED_TEXT +"Illegal move, press enter to continue"+ WHITE_TEXT);
            in.nextLine();
        }
    }

    public String readCommand(){
        System.out.print(WHITE_TEXT +"Enter move> ");
        String move=in.nextLine();
        String mv=move;
        if(move.length()>=3 && move.substring(2).matches("[0-9]{1,2}")){
            mv=move.substring(0,2);
        }
        if(!mv.matches("[CDHPScdhps1-7]{2}") && !mv.matches("[DQdq]")){
            System.err.println(RED_TEXT +"Invalid Command, press enter to continue"+ WHITE_TEXT);
            in.nextLine();
            move="";
        }
        return move.toUpperCase();
    }
    public static String readCommand(String move){
        String mv=move;
        if(move.length()>=3 && move.substring(2).matches("[0-9]{1,2}")){
            mv=move.substring(0,2);
        }
        if(!mv.matches("[CDHPScdhps1-7]{2}") && !mv.matches("[DQdq]")){
            move="";
        }
        return move.toUpperCase();
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static boolean checkScore(int number, Pile from, Pile to){
        boolean isValid=true;
        Card movedCard = from.getCard(from.getSize()-number);
        Card previousToTopCard = to.getTopCard();
        Card newFromTopCard = from.getCard(from.getSize()-number-1);
        if(from.getSize()==number){
            if(movedCard.getRank() == Rank.KING){
                isValid=false;
            }
        }
        else if (previousToTopCard!=null && newFromTopCard!= null){
            if((newFromTopCard.getRank() == previousToTopCard.getRank() &&
                    newFromTopCard.getColor() == previousToTopCard.getColor()) &&
                    !newFromTopCard.isCovered()){
                isValid=false;
            }
        }
        return isValid;
    }
    private void updateScore(int number, Pile from, Pile to, boolean isValid){
        if(to instanceof SuitPile){
            if(from instanceof Lane){
                score+=20;
            } else if (from instanceof UncoverdPile) {
                score+=10;
            }
        } else if (from instanceof Lane && to instanceof Lane && isValid) {
            score+=(5*number);
        }
    }
    public void parseMove(String move){
        List<Pile> piles= new LinkedList<>();
        String fromTo = switch(move.length()){
            case 1, 2 -> move;
            case 3, 4 -> move.substring(0, 2);
            default -> "";
        };
        int number=switch(move.length()){
            case 2 -> 1;
            case 3, 4 -> Integer.parseInt(move.substring(2));
            default -> 0;
        };

        for(char c: fromTo.toCharArray()){
            piles.add(switch (c){
                case 'P' -> table.getUncoveredPile();
                case 'C' -> table.getSuitPile(Suit.CLUBS);
                case 'D' -> table.getSuitPile(Suit.DIAMONDS);
                case 'H' -> table.getSuitPile(Suit.HEARTS);
                case 'S' -> table.getSuitPile(Suit.SPADES);
                case '1' -> table.getLane(0);
                case '2' -> table.getLane(1);
                case '3' -> table.getLane(2);
                case '4' -> table.getLane(3);
                case '5' -> table.getLane(4);
                case '6' -> table.getLane(5);
                case '7' -> table.getLane(6);
                default -> null;
            });
        }
        if(piles.size()==2){
            if (piles.get(0) instanceof Lane && piles.get(1) instanceof Lane && number==1){
                autoMove(piles.get(0),piles.get(1));
            }else{
                move(number, piles.get(0),piles.get(1), move);
            }
        }
        else{
            move(number, null, null, move);
        }
    }



    public boolean isGameOver() {
        boolean[] completedPiles= {false,false,false,false};
        int i=0;
        for(Suit suit : Suit.values()){
            if(table.getSuitPile(suit).getSize()==13){
                completedPiles[i]=true;
            }
            i++;
        }
        if(completedPiles[0] && completedPiles[1] && completedPiles[2] && completedPiles[3]){
            gameOver=true;
        }
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public int getMoves() {
        return moves;
    }

    public Table getTable() {
        return table;
    }
}
