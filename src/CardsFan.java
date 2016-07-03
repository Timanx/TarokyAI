import java.util.*;

public class CardsFan<E> extends ArrayList<E> {
    public CardsFan(int initialCapacity) {
        super(initialCapacity);
    }

    public CardsFan() {
        super();
    }

    public Card minFoldable() {
        int i = 0;
        Card min = (Card)this.get(0);
        while(min.getPoints() == 5) {
            i++;
            min = (Card)this.get(i);
        }
        i++;
        while(i < this.size()) {
            Card card = (Card)this.get(i);
            if(card.getStrength() < min.getStrength() && card.getPoints() != 5) {
                min = card;
            }
        }

        return min;
    }

    /**
     * Choose the most valuable card that can be folded
     * @return
     */
    public Card maxFoldablePoints() {
        Card maxPointCard = ((Card) this.get(0));
        for (Object card : this) {
            if(((Card) card).getPoints() > maxPointCard.getPoints() && ((Card) card).getPoints() != 5) {
                maxPointCard = (Card) card;
            }
        }
        return maxPointCard;
    }

    /**
     * Fold the weakest [amount] taroky
     * @param amount
     * @return
     */
    public CardsFan<Card> foldTaroky(int amount) {
        CardsFan<Card> folding = new CardsFan<>(amount);
        int i = 0;

        while(i < Math.min(this.size(), amount)) {
            int j  = 0;
            Card min = (Card)this.get(0);
            while(min.getPoints() == 5) {
                j++;
                min = (Card)this.get(j);
            }
            j++;
            Card card = (Card)this.get(j);
            if(card.getStrength() < min.getStrength() && card.getPoints() != 5) {
                min = card;
            }
            i++;

            folding.add(min);
            this.remove(min);
        }

        return folding;
    }

    /**
     * Fold all cards of color [key]
     * @param key color to fold
     * @return
     */
    public CardsFan<Card> foldColor(CardColor key) {
        CardsFan<Card> folding = new CardsFan<>();
        for (Object card : this) {
            if(((Card) card).getColor() == key) {
                folding.add((Card) card);

            }
        }
        return folding;
    }

    /**
     * Fold [amount] cards to get the most points
     * @param amount how many cards should be folded
     * @return
     */
    public CardsFan<Card> foldValuable(int amount) {
        CardsFan<Card> folding = new CardsFan<>(amount);
        while(amount > 0) {
            Card folded = this.maxFoldablePoints();
            this.remove(folded);
            folding.add(folded);
            amount--;
        }
        return folding;
    }

    /**
     * Return true if card of given parameters is present in CardsFan
     * @param color
     * @param value
     * @return
     */
    public boolean contains(CardColor color, CardValue value) {
        for (Object card : this) {
            if(((Card) card).getColor() == color && ((Card) card).getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(CardValue value) {
        for (Object card : this) {
            if(((Card) card).getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Card seekedCard) {
        for (Object card : this) {
            if(((Card) card).getColor().equals(seekedCard.getColor()) && ((Card) card).getValue().equals(seekedCard.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates points in CardsFan and empties it
     * @return how many points were in the CardsFan
     */
    public int calculatePoints() {
        int points = 0;

        while(!this.isEmpty()) {
            if(this.size() >= 3) {
                points += ((Card)this.remove(0)).getPoints();
                points += ((Card)this.remove(0)).getPoints();
                points += ((Card)this.remove(0)).getPoints();
                points -= 2;
            } else if(this.size() == 2) {
                points += ((Card)this.remove(0)).getPoints();
                points += ((Card)this.remove(0)).getPoints();
                points -= 1;
            } else {
                points += ((Card)this.remove(0)).getPoints();
                points -= 1;
            }
        }

        return points;
    }

    public int sumCardPoints() {
        int points = 0;
        for (Object card : this) {
            points += ((Card) card).getPoints();
        }
        return points;
    }

    public void addCards(CardsFan<Card> cards) {
        for (Object card : cards) {
                this.add((E) card);
        }
    }

    public void addCard(Card card) {
            this.add((E) card);
    }

    public void sort() {
        Collections.sort((ArrayList)this, new CardComparator());
    }

    public boolean containsTarok() {
        for (Object card : this) {
            if(((Card) card).getColor().equals(CardColor.TAROK)) {
                return true;
            }
        }
        return false;
    }

    public Card getMostValuableCard() {
        CardValue maxValue = ((Card) this.get(0)).getValue();
        Card mostValuableCard = (Card) this.get(0);
        for (Object card : this) {
            if(((Card) card).getValue().compareTo(maxValue) >= 0) {
                maxValue = ((Card) card).getValue();
                mostValuableCard = (Card) card;
            }
        }
        return mostValuableCard;
    }

    public Card getLeastValuableCard() {
        CardValue minValue = ((Card) this.get(0)).getValue();
        Card leastValuableCard = (Card) this.get(0);
        for (Object card : this) {
            if(((Card) card).getValue().compareTo(minValue) <= 0) {
                minValue = ((Card) card).getValue();
                leastValuableCard = (Card) card;
            }
        }
        return leastValuableCard;
    }

    public Card getGoldenMean() {
        CardsFan<E> cards = this;
        cards.sort();
        return (Card) cards.get(cards.size() / 2);
    }

    public Card getMostValuableCardByColor(CardColor color) {
        CardValue maxValue = CardValue.FOUR;
        Card mostValuableCard = null;
        for (Object card : this) {
            if(((Card) card).getColor().equals(color) && ((Card) card).getValue().compareTo(maxValue) >= 0) {
                maxValue = ((Card) card).getValue();
                mostValuableCard = (Card) card;
            }
        }
        return mostValuableCard;
    }

    public Card getLeastValuableCardByColor(CardColor color) {
        CardValue minValue = CardValue.SKYZ;
        Card leastValuableCard = null;
        for (Object card : this) {
            if(((Card) card).getColor().equals(color) && ((Card) card).getValue().compareTo(minValue) <= 0) {
                minValue = ((Card) card).getValue();
                leastValuableCard = (Card) card;
            }
        }
        return leastValuableCard;
    }

    public Card getMiddleValuableCardByColor(CardColor color) {
        CardValue minValue = CardValue.SKYZ;
        Card leastValuableCard = null;
        for (Object card : this) {
            if(((Card) card).getColor().equals(color) && ((Card) card).getValue().compareTo(minValue) <= 0) {
                minValue = ((Card) card).getValue();
                leastValuableCard = (Card) card;
            }
        }
        return leastValuableCard;
    }

    public Card getStrongestCardByLeadingColor(CardColor leadingColor) {
        int maxStrength = Integer.MIN_VALUE;
        Card strongestCard = null;
        for (Object card : this) {
            if(((Card) card).getStrength() > maxStrength &&
                    (((Card) card).getColor().equals(CardColor.TAROK) ||
                    ((Card) card).getColor().equals(leadingColor))) {
                maxStrength = ((Card) card).getStrength();
                strongestCard = (Card) card;
            }
        }
        if(strongestCard == null) {
            for (Object card : this) {
                if(((Card) card).getStrength() > maxStrength) {
                    maxStrength = ((Card) card).getStrength();
                    strongestCard = (Card) card;
                }
            }
        }

        return strongestCard;
    }

    public int getStrongestCardIndexByLeadingColor(CardColor leadingColor) {
        int strongestIndex = Integer.MIN_VALUE;
        int maxStrength = Integer.MIN_VALUE;

        for(int i = 0; i < this.size(); i++) {
            Card card = ((Card) this.get(i));
            if(card.getStrength() > maxStrength && (card.getColor().equals(CardColor.TAROK) || card.getColor().equals(leadingColor))) {
                maxStrength = card.getStrength();
                strongestIndex = i;
            }
        }

        return strongestIndex;
    }

    public Card getLowestTarok() {
        Card lowestTarok = null;
        int minStrength = Integer.MAX_VALUE;
        for (Object card : this) {
            if(((Card) card).getColor().equals(CardColor.TAROK) && ((Card) card).getStrength() < minStrength) {
                minStrength = ((Card) card).getStrength();
                lowestTarok = (Card) card;
            }
        }
        return lowestTarok;
    }

    public Card getLowestTarokWithoutPagat() {
        Card lowestTarok = null;
        Card pagat = null;
        int minStrength = Integer.MAX_VALUE;
        for (Object card : this) {
            if(((Card) card).getValue().equals(CardValue.PAGAT)) {
                pagat = (Card) card;
            } else if(((Card) card).getColor().equals(CardColor.TAROK) && ((Card) card).getStrength() < minStrength) {
                minStrength = ((Card) card).getStrength();
                lowestTarok = (Card) card;
            }
        }
        if(lowestTarok == null) {
            return pagat;
        } else {
            return lowestTarok;
        }
    }

    public Card getHighestTarokWithoutMond() {
        Card highestTarok = null;
        Card mond = null;
        int maxStrength = Integer.MIN_VALUE;
        for (Object card : this) {
            if(((Card) card).getValue().equals(CardValue.MOND)) {
                mond = (Card) card;
            } else if(((Card) card).getColor().equals(CardColor.TAROK) && ((Card) card).getStrength() > maxStrength) {
                maxStrength = ((Card) card).getStrength();
                highestTarok = (Card) card;
            }
        }
        if(highestTarok == null) {
            return mond;
        } else {
            return highestTarok;
        }
    }

    public Card getHighestTarok() {
        Card highestTarok = null;
        int maxStrength = Integer.MIN_VALUE;
        for (Object card : this) {
            if(((Card) card).getColor().equals(CardColor.TAROK) && ((Card) card).getStrength() > maxStrength) {
                maxStrength = ((Card) card).getStrength();
                highestTarok = (Card) card;
            }
        }
        return highestTarok;
    }

    public Card getLowestTarokToBeatCard(Card strongCard) {
        Card lowestTarok = null;
        int minStrength = Integer.MAX_VALUE;
        for (Object card : this) {
            if(((Card) card).getColor().equals(CardColor.TAROK) && ((Card) card).getStrength() > strongCard.getStrength() && ((Card) card).getStrength() < minStrength) {
                minStrength = ((Card) card).getStrength();
                lowestTarok = (Card) card;
            }
        }
        return lowestTarok;
    }

    public int contains(CardValue value, CardColor color) {
        for(int i = 0; i < this.size(); i++) {
            Card card = (Card) this.get(i);
            if(card.getValue().equals(value) && card.getColor().equals(color)) {
                return i;
            }
        }

        return -1;
    }

    public ArrayList<CardColor> getKingColors() {
        ArrayList<CardColor> kingColors = new ArrayList<>();
        for (Object card : this) {
            if(((Card) card).getValue().equals(CardValue.KING)) {
                kingColors.add(((Card) card).getColor());
            }
        }
        return kingColors;
    }

    public CardsFan<Card> getCardsByColors(ArrayList<CardColor> colors) {
        CardsFan<Card> colorsCards = new CardsFan<>();
        for (Object card : this) {
            if(colors.contains(((Card) card).getColor())) {
                colorsCards.add((Card) card);
            }
        }
        return colorsCards;
    }

    public CardColor getMostFrequentColor() {
        EnumMap<CardColor, Integer> colorCount = new EnumMap<>(CardColor.class);
        colorCount.put(CardColor.CLUBS, 0);
        colorCount.put(CardColor.SPADES, 0);
        colorCount.put(CardColor.DIAMONDS, 0);
        colorCount.put(CardColor.HEARTS, 0);
        colorCount.put(CardColor.TAROK, 0);
        for (Object card : this) {
            colorCount.put(((Card) card).getColor(), colorCount.get(((Card) card).getColor()) + 1);
        }

        int maxCards = 0;
        CardColor maxColor = CardColor.TAROK;
        for (CardColor color : colorCount.keySet()) {
            if(colorCount.get(color) > maxCards) {
                maxCards = colorCount.get(color);
                maxColor = color;
            }
        }
        //TODO: Optimize constant
        if(maxCards >= 3 || this.size() <= 6) {
            return maxColor;
        } else {
            return CardColor.TAROK;
        }
    }

    public boolean hasAnyOfThePlayersColor(ArrayList<CardsFan<Card>> cardsByPlayer, ArrayList<Integer> playerIndices, CardColor color, CardsFan<Card> talon, CardsFan<Card> hand) {
        ArrayList<Integer> playedColorCards = new ArrayList<>();
        playedColorCards.add(0);
        playedColorCards.add(0);
        playedColorCards.add(0);
        playedColorCards.add(0);
        int totalColorCards = 0;
        ArrayList<CardColor> tricksColors = new ArrayList<>();
        ArrayList<Boolean> canPlayersHaveColor = new ArrayList<>(Game.PLAYERS_COUNT);
        canPlayersHaveColor.add(true);
        canPlayersHaveColor.add(true);
        canPlayersHaveColor.add(true);
        canPlayersHaveColor.add(true);

        for (int i = 0; i < this.size(); i++) {
            if(i % 4 == 0) {
                tricksColors.add(((Card) this.get(i)).getColor());
            }
        }

        for(Card card : talon) {
            if(card.getColor().equals(color)) {
                totalColorCards++;
            }
        }

        for(Card card : hand) {
            if(card.getColor().equals(color)) {
                totalColorCards++;
            }
        }

        for(int i = 0; i < Game.PLAYERS_COUNT; i++) {
            for(int j = 0; j < cardsByPlayer.get(i).size(); j++) {
                Card card = cardsByPlayer.get(i).get(j);
                if(card.getColor().equals(color)) {
                    playedColorCards.set(i, playedColorCards.get(i) + 1);
                    totalColorCards++;
                }
                if(!card.getColor().equals(tricksColors.get(j))) {
                    canPlayersHaveColor.set(i, false);
                }
            }
        }

        if(totalColorCards >=  8) {
            return false;
        }

        for(Integer player : playerIndices) {
            if(!canPlayersHaveColor.get(player)) {
                continue;
            } else {
                return true;
            }
        }

        return false;
    }

    public boolean haveAllOfThePlayersColor(ArrayList<CardsFan<Card>> cardsByPlayer, ArrayList<Integer> playerIndices, CardColor color, CardsFan<Card> talon, CardsFan<Card> hand) {
        ArrayList<Integer> playedColorCards = new ArrayList<>();
        playedColorCards.add(0);
        playedColorCards.add(0);
        playedColorCards.add(0);
        playedColorCards.add(0);
        int totalColorCards = 0;
        ArrayList<CardColor> tricksColors = new ArrayList<>();
        ArrayList<Boolean> canPlayersHaveColor = new ArrayList<>(Game.PLAYERS_COUNT);
        canPlayersHaveColor.add(true);
        canPlayersHaveColor.add(true);
        canPlayersHaveColor.add(true);
        canPlayersHaveColor.add(true);

        for (int i = 0; i < this.size(); i++) {
            if(i % 4 == 0) {
                tricksColors.add(((Card) this.get(i)).getColor());
            }
        }

        for(Card card : talon) {
            if(card.getColor().equals(color)) {
                totalColorCards++;
            }
        }

        for(Card card : hand) {
            if(card.getColor().equals(color)) {
                totalColorCards++;
            }
        }

        for(int i = 0; i < Game.PLAYERS_COUNT; i++) {
            for(int j = 0; j < cardsByPlayer.get(i).size(); j++) {
                Card card = cardsByPlayer.get(i).get(j);
                if(card.getColor().equals(color)) {
                    playedColorCards.set(i, playedColorCards.get(i) + 1);
                    totalColorCards++;
                }
                if(!card.getColor().equals(tricksColors.get(j))) {
                    canPlayersHaveColor.set(i, false);
                }
            }
        }

        if(totalColorCards >=  8) {
            return false;
        }

        for(Integer player : playerIndices) {
            if(!canPlayersHaveColor.get(player)) {
                return false;
            }
        }

        return true;
    }

    public CardsFan<Card> getRemainingColorCards(CardColor color, CardsFan<Card> talon, CardsFan<Card> playedCards, CardsFan<Card> hand) {
        CardsFan<Card> remainingCards = new CardsFan<>();

        for(Object object : this) {
            Card card = (Card) object;

            if(!talon.contains(card) && !playedCards.contains(card) && !hand.contains(card)) {
                remainingCards.add(card);
            }
        }

        return remainingCards;

    }

    public Card get(CardValue value) {
        return this.get(value, CardColor.TAROK);
    }

    public Card get(CardValue value, CardColor color) {

        for(Object card : this) {
            if(((Card) card).getValue().equals(value) && ((Card) card).getColor().equals(color)) {
                return (Card) card;
            }
        }

        return null;
    }

    public int getTarokyCount() {
        int tarokyCount = 0;
        for(Object card : this) {
            if(((Card) card).getColor().equals(CardColor.TAROK)) {
                tarokyCount++;
            }
        }

        return tarokyCount;
    }

    public int getUnfoldableCount() {
        int unfoldableCount = 0;
        for(Object card : this) {
            if(((Card) card).getColor().equals(CardColor.TAROK) || ((Card) card).getPoints() == 5) {
                unfoldableCount++;
            }
        }

        return unfoldableCount;
    }
}
