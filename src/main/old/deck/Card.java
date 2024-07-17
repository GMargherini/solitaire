package deck;

import static core.Constants.*;

public class Card {
	private final Rank rank;
	private final Suit suit;
	private final Color color;

	private boolean covered = true;
	public Card(Rank rank, Suit suit){
		this.rank=rank;
		this.suit=suit;
		if(suit == Suit.DIAMONDS || suit == Suit.HEARTS)
			this.color = Color.RED;
		else
			this.color = Color.BLACK;
	}

	public Rank getRank() {
		return rank;
	}

	public Color getColor() {
		return color;
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank nextRank(){
		return Rank.values()[rank.ordinal()+1];
	}
	@Override
	public String toString() {
		String string;
		if(covered){
			return WHITE_TEXT +"▇▇▇";
		}else{
			String rank = switch (this.rank){
				case ACE -> " A";
				case TEN -> "10";
				case JACK -> " J";
				case QUEEN -> " Q";
				case KING -> " K";
				default -> " " + (this.rank.ordinal() + 1);
			};
			char suit= this.suit.toString().charAt(0);

			if(color==Color.RED && !isCovered()){
				string= RED_TEXT +rank + suit;
			}
			else{
				string= WHITE_TEXT +rank + suit;
			}
		}
		return string;
	}

	public boolean isCovered() {
		return covered;
	}

	public void flip() {
		covered = !covered;
	}
	public boolean isNext(Card card){
		return this.getRank() == card.nextRank();
	}
	public boolean isPrevious(Card card){
		return card.getRank() == this.nextRank();
	}
}