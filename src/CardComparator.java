import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

    @Override
    public int compare(Card card1, Card card2){
        int colorComparation = card1.getColor().compareTo(card2.getColor());

        if(colorComparation > 0) {
            return 1;
        } else if(colorComparation < 0) {
            return -1;
        } else {
            return card1.getStrength() - card2.getStrength();
        }
    }
}
