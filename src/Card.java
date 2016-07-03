public class Card implements Comparable<Card> {


	private CardColor color;
	private CardValue value;
	private int points;

    //defines how many other cards does this one defeat
    private int strength;

    public Card() {
        //used only for comparator call
    }

	public Card(CardColor color, CardValue value, int points, int strength) {
		this.color = color;
		this.value = value;
		this.points = points;		
		this.strength = strength;
	}

    public CardColor getColor() {
		return color;
	}

	public CardValue getValue() {
		return value;
	}

	public int getPoints() {
		return points;
	}

	public int getStrength() {
        return strength;
    }


    public String toString() {
        if (color == CardColor.TAROK) {
            return translate_value();
        } else {
            return translate_value() + translate_color();
        }

    }

    private String translate_color() {
        switch (color) {
            case CLUBS:
                return " of Clubs";
            case SPADES:
                return " of Spades";
            case DIAMONDS:
                return " of Diamonds";
            case HEARTS:
                return " of Hearts";
            case TAROK:
                return "Tarok";
            default:
                return "Unknown";
        }
    }

    private String translate_value() {
        switch (value) {
            case ONE:
                return "1";
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            case JACK:
                return "J";
            case RIDER:
                return "R";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            case PAGAT:
                return "Pagat";
            case II:
                return "II";
            case III:
                return "III";
            case IIII:
                return "IIII";
            case V:
                return "V";
            case VI:
                return "VI";
            case VII:
                return "VII";
            case VIII:
                return "VIII";
            case IX:
                return "IX";
            case X:
                return "X";
            case XI:
                return "XI";
            case XII:
                return "XII";
            case XIII:
                return "XIII";
            case XIV:
                return "XIV";
            case XV:
                return "XV";
            case XVI:
                return "XVI";
            case XVII:
                return "XVII";
            case XVIII:
                return "XVIII";
            case XIX:
                return "XIX";
            case XX:
                return "XX";
            case MOND:
                return "Mond";
            case SKYZ:
                return "Skyz";
            default:
                return "Unknown";
        }
    }

    @Override
    public int compareTo(Card card) {
        if(strength > card.strength) {
            return 1;
        } else if(strength < card.strength) {
            return -1;
        } else {
            return 0;
        }
    }

    public static boolean compare(Card card1, Card card2){
        return card1.getStrength() > card2.getStrength();
    }
}
