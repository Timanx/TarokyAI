import java.util.*;

public class Player {

    protected ArrayList<Announcement> announcements;
    protected ArrayList<Announcement> special_announcements;
    protected ArrayList<Announcement> doubles;
    protected CardsFan<Card> hand = new CardsFan<>(16);
    protected String name;
    protected CardsFan<Card> pile = new CardsFan<>();
    protected Strategy strategy;
    protected int index;
    protected boolean isBidder = false;
    public final boolean isHuman;
    public final static boolean allowPrint = true;

    public Player(String name, Strategy strategy, int index) {
        this.name = name;
        this.strategy = strategy;
        this.index = index;
        this.isHuman = false;
    }

    public Player(String name, boolean isHuman, int index) {
        this.name = name;
        this.index = index;
        this.isHuman = isHuman;
    }

    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public String getName() {
        return name;
    }


    public int getIndex() {
        return index;
    }

    public boolean isBidder() {
        return isBidder;
    }

    public void makeBidder() {
        isBidder = true;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public void resolveAnnouncements() {
        announcements = resolveAnnouncements(hand);
        printAnnouncements();
    }

    public boolean hasCard(CardColor color, CardValue value) {
        return hand.contains(color, value);
    }

    private ArrayList<Integer> getTeammates(ArrayList<Integer> bidderTeamIndices, boolean amIInBidderTeam) {
        ArrayList<Integer> teammates = new ArrayList<>(2);
        if(amIInBidderTeam) {
            bidderTeamIndices.remove((Integer) index);
            return bidderTeamIndices;
        } else {
            for(int i = 0; i < Game.PLAYERS_COUNT; i++) {
                if(bidderTeamIndices.contains(i)) {
                    teammates.add(i);
                }
            }
            return teammates;
        }
    }

    private ArrayList<Integer> getRemainingTeammates(ArrayList<Integer>teammates, int trickSize) {
        ArrayList<Integer> remainingTeammates = new ArrayList<>(2);
        ArrayList<Integer> playedIndices = new ArrayList<>(3);

        for (int i = 1; i <= trickSize; i++) {
            playedIndices.add((index - i) % Game.PLAYERS_COUNT);
        }

        for (Integer playerIndex : teammates) {
            if(!playedIndices.contains(playerIndex)) {
                remainingTeammates.add(playerIndex);
            }
        }

        return remainingTeammates;
    }

    private ArrayList<Integer> getRemainingPlayers(int trickSize) {
        ArrayList<Integer> remainingPlayers = new ArrayList<>(2);
        ArrayList<Integer> playedIndices = new ArrayList<>(3);

        for (int i = 1; i <= trickSize; i++) {
            playedIndices.add((index - i) % Game.PLAYERS_COUNT);
        }

        for (int i = 0; i < Game.PLAYERS_COUNT; i++) {
            if(!playedIndices.contains(i)) {
                remainingPlayers.add(i);
            }
        }

        return remainingPlayers;
    }

    public int getTarokyCount() {
        return getTarokyCount(hand);
    }

    public int getTarokyStrength() {
        return getTarokyStrength(hand);
    }

    public int getTarokyCount(CardsFan<Card> hand) {
        int tarokyCount = 0;

        for (Card card : hand) {
            switch (card.getColor()) {
                case TAROK:
                    tarokyCount++;
                    break;
                default:
                    break;
            }
        }
        return tarokyCount;
    }

    public int getTarokyStrength(CardsFan<Card> hand) {
        int tarokyStrength = 0;

        for (Card card : hand) {
            switch (card.getColor()) {
                case TAROK:
                    tarokyStrength += card.getStrength();
                    break;
                default:
                    break;
            }
        }
        return tarokyStrength;
    }

    public ArrayList<Announcement> resolveAnnouncements(CardsFan<Card> hand) {
        int tarokyCount = 0;
        int trulCount = 0;
        int kingsCount = 0;

        ArrayList<Announcement> announcements = new ArrayList<>(3);

        for (Card card : hand) {
            switch (card.getColor()) {
                case TAROK:
                    tarokyCount++;
                    break;
                default:
                    break;
            }

            switch (card.getValue()) {
                case PAGAT:
                    trulCount++;
                    break;
                case MOND:
                    trulCount++;
                    break;
                case SKYZ:
                    trulCount++;
                    break;
                case KING:
                    kingsCount++;
                    break;
                default:
                    break;
            }
        }

        if(tarokyCount >= 10) {
            announcements.add(new Announcement(AnnouncementName.TAROKY, 10));
        } else if(tarokyCount >= 8) {
            announcements.add(new Announcement(AnnouncementName.TAROCKY, 5));
        } else if(tarokyCount == 0 || (tarokyCount == 1 && trulCount == 1)) {
            announcements.add(new Announcement(AnnouncementName.BARVA, 10));
        } else if(tarokyCount <= 2) {
            announcements.add(new Announcement(AnnouncementName.BARVICKA, 5));
        }

        if(trulCount + kingsCount == 7) {
            announcements.add(new Announcement(AnnouncementName.KRALOVSKE_HONERY, 10));
            announcements.add(new Announcement(AnnouncementName.TRUL, 5));
        } else if(trulCount == 3 && kingsCount > 0) {
            announcements.add(new Announcement(AnnouncementName.TRULHONERY, 10));
        } else if(trulCount == 3) {
            announcements.add(new Announcement(AnnouncementName.TRUL, 5));
        } else if(trulCount + kingsCount >= 4) {
            announcements.add(new Announcement(AnnouncementName.HONERY, 5));
        } else if(kingsCount == 4) {
            announcements.add(new Announcement(AnnouncementName.KRALOVSKE_HONERY, 10));
        }

        return announcements;

    }

    public void playCard(Card card) {
        hand.remove(card);
    }

    public CardsFan<Card> getHand() {
        return hand;
    }

    public Card playLeadCard(CardsFan<Card> playedCards) {
        Card playedCard = null;
        CardsFan<Card> remainingTaroky = new CardsFan<>();
        CardsFan<Card> myTaroky = new CardsFan<>();
        CardsFan<Card> othersTaroky = new CardsFan<>();


        for(Card tarok : Game.taroky) {
            if(!playedCards.contains(tarok)) {
                remainingTaroky.add(tarok);
            }
        }
        remainingTaroky.sort();
        for(Card tarok : remainingTaroky) {
            if(hand.contains(tarok)) {
                myTaroky.add(tarok);
            } else {
                othersTaroky.add(tarok);
            }
        }
        //TODO: improve constant 4
        if(myTaroky.size() >= othersTaroky.size() - othersTaroky.size()/4) {
            boolean haveAllStrongestTaroky = true;
            for(int i = remainingTaroky.size() - 1; i >= remainingTaroky.size() - othersTaroky.size() + othersTaroky.size()/4; i--) {
                if(!myTaroky.contains(remainingTaroky.get(i))) {
                    haveAllStrongestTaroky = false;
                    break;
                }
            }
            if(haveAllStrongestTaroky) {
                playedCard = myTaroky.getHighestTarok();
            }
        }


        boolean haveSomeColorsIWantToKeep = false; //TODO
        boolean wantToWinTheTrick = true; //TODO

        ArrayList<CardColor> kingColors = playedCards.getKingColors();
        CardsFan<Card> kingColorsCards = hand.getCardsByColors(kingColors);
        if(playedCard == null) {
            if(!haveSomeColorsIWantToKeep && kingColorsCards.size() != 0) {
                boolean canStrongetOfTheseCardsWinTheseTrick = false; //TODO
                if(canStrongetOfTheseCardsWinTheseTrick) {
                    playedCard = kingColorsCards.getMostValuableCard();
                } else {
                    playedCard = kingColorsCards.getLeastValuableCard();
                }
            } else {
                CardColor mostFrequentColor = hand.getMostFrequentColor();
                if(!mostFrequentColor.equals(CardColor.TAROK)) {
                    playedCard = hand.getLeastValuableCardByColor(mostFrequentColor);
                } else {
                    if(myTaroky.size() < othersTaroky.size()) {
                        playedCard = hand.getLeastValuableCard();
                    } else {
                        if(hand.contains(CardValue.PAGAT)) {
                            playedCard = hand.getLowestTarokWithoutPagat();
                        } else {
                            if(!wantToWinTheTrick) {
                                playedCard = hand.getLowestTarok();
                            } else {
                                if(hand.contains(CardValue.MOND) && (hand.contains(CardValue.SKYZ) || playedCards.contains(CardValue.SKYZ))) {
                                    playedCard = hand.getHighestTarok();
                                } else {
                                    playedCard = hand.getHighestTarokWithoutMond();
                                }
                            }
                        }
                    }
                }
            }
        }

        playCard(playedCard);
        return playedCard;
    }

    public CardsFan<Card> getPlayableCards(CardColor trickColor) {
        CardsFan<Card> playableCards = new CardsFan<>();
        //Pick cards of the same color as the leading card
        if(trickColor != null && !trickColor.equals(CardColor.TAROK)) {
            for (Card card : hand) {
                if (card.getColor().equals(trickColor)) {
                    playableCards.add(card);
                }
            }
        }

        //If none are available, pick taroky
        if(playableCards.size() == 0 && trickColor != null) {
            for (Card card : hand) {
                if (card.getColor().equals(CardColor.TAROK)) {
                    playableCards.add(card);
                }
            }
        }

        //If none are available, any card is playable
        if(playableCards.size() == 0) {
            playableCards = hand;
        }

        return playableCards;
    }

    public Card playSuitableCard(CardsFan<Card> trick, ArrayList<Player> bidderTeamPlayers, int bidder, PlayType playType, boolean bidderTeamPublic, CardsFan<Card> playedCards, ArrayList<CardsFan<Card>> playedCardsByPlayer, CardsFan<Card> knownTalon) {
        //TODO: Different strategies for choosing suitable card
        ArrayList<Integer> bidderTeam = new ArrayList<>();
        for (Player bidderTeamPlayer : bidderTeamPlayers) {
            bidderTeam.add(bidderTeamPlayer.getIndex());
        }

        Card playedCard;
        CardColor trickColor = trick.get(0).getColor();
        Card strongestTrickCard = trick.getStrongestCardByLeadingColor(trickColor);

        CardsFan<Card> playableCards;

        boolean haveTrickColour = false; //False also when trick color is tarok

        playableCards = this.getPlayableCards(trickColor);

        playedCard = playableCards.get(0);
        if(playedCard.getColor().equals(trickColor) && !trickColor.equals(CardColor.TAROK)) {
            haveTrickColour = true;
        }

        Card strongestPlayableCard = playableCards.getStrongestCardByLeadingColor(trickColor);
        Card lowestTarok = playableCards.getLowestTarok();
        Card lowestTarokHopefullyWithoutPagat = playableCards.getLowestTarokWithoutPagat();
        Card lowestTarokToWinTheTrick = playableCards.getLowestTarokToBeatCard(strongestTrickCard);
        Card mostValuablePlayableCard = playableCards.getMostValuableCard();
        Card leastValuablePlayableCard = playableCards.getLeastValuableCard();
        Card goldenMean = playableCards.getGoldenMean();
        CardsFan<Card> remainingColorCards = Game.getDeck().getRemainingColorCards(trickColor, playedCards, knownTalon, hand);


        boolean amIInBidderTeam = bidderTeam.contains(index);
        ArrayList<Integer> teammates = this.getTeammates(bidderTeam, amIInBidderTeam);
        ArrayList<Integer> remainingTeammates = this.getRemainingTeammates(teammates, trick.size());
        ArrayList<Integer> remainingPlayers = this.getRemainingPlayers(trick.size());
        boolean doIKnowMyTeammate = amIInBidderTeam || bidderTeamPublic;
        boolean hasMyTeammatePlayed = remainingTeammates.size() == 0;
        boolean hasMyTeammateTrickColor = false;
        if(!hasMyTeammatePlayed) {
            hasMyTeammateTrickColor = playedCards.hasAnyOfThePlayersColor(playedCardsByPlayer, remainingTeammates, trickColor, knownTalon, hand);
        }
        boolean haveTarok = hand.containsTarok();
        boolean willTeamMateWin = doIKnowMyTeammate && !hasMyTeammatePlayed && trickColor != CardColor.TAROK && !hasMyTeammateTrickColor; //TODO: Can be improved
        boolean amILast = trick.size() == Game.PLAYERS_COUNT - 1;
        boolean canIBeatPlayedCards = strongestTrickCard.compareTo(strongestPlayableCard) < 0;
        boolean tarokInTrick = trick.containsTarok();
        boolean isTrickWorthWinning = trick.sumCardPoints() + (Game.PLAYERS_COUNT - trick.size())/Math.max(remainingColorCards.size(),1) * remainingColorCards.sumCardPoints() > 6; //TODO: Will other players play valuable cards?
        boolean wantToKeepPagat = playableCards.contains(CardValue.PAGAT, CardColor.TAROK) >= 0 && getTarokyCount() > Game.getDeck().getRemainingColorCards(CardColor.TAROK, playedCards, knownTalon, hand).size() * 0.75;
        int mondIndex = playableCards.contains(CardValue.MOND, CardColor.TAROK);
        boolean worthRiskingMond = mondIndex >= 0 && (remainingTeammates.size() >= (Game.PLAYERS_COUNT - 1 - trick.size()) || willTeamMateWin || playedCards.contains(CardValue.SKYZ) || hand.contains(CardValue.SKYZ));
        boolean otherPlayersHaveColor = playedCards.haveAllOfThePlayersColor(playedCardsByPlayer, remainingPlayers, trickColor, knownTalon, hand);
        boolean worthRiskingKing = remainingTeammates.size() >= (Game.PLAYERS_COUNT - 1 - trick.size()) || willTeamMateWin || otherPlayersHaveColor;
        boolean worthRiskingAnyPoints = remainingTeammates.size() >= (Game.PLAYERS_COUNT - 1 - trick.size()) || willTeamMateWin || (otherPlayersHaveColor && remainingColorCards.getMostValuableCard().getPoints() < playableCards.getGoldenMean().getPoints());





        if (haveTrickColour) {
            if (amILast) {
                if (canIBeatPlayedCards) {
                    playedCard = mostValuablePlayableCard;
                } else {
                    if (doIKnowMyTeammate) {
                        if (willTeamMateWin) {
                            playedCard = mostValuablePlayableCard;
                        } else {
                            playedCard = leastValuablePlayableCard;
                        }
                    } else {
                        playedCard = goldenMean;
                    }
                }
            } else {
                if (worthRiskingKing) {
                    playedCard = mostValuablePlayableCard;
                } else {
                    if (worthRiskingAnyPoints) {
                        playedCard = goldenMean;
                    } else {
                        playedCard = leastValuablePlayableCard;
                    }
                }
            }
        } else {
            if (haveTarok) {
                if (amILast) {
                    if (tarokInTrick) {
                        if (canIBeatPlayedCards) {
                            if (isTrickWorthWinning) {
                                playedCard = lowestTarokToWinTheTrick;
                            } else {
                                playedCard = lowestTarokHopefullyWithoutPagat;
                            }
                        } else {
                            if (willTeamMateWin) {
                                playedCard = lowestTarok;
                            } else {
                                playedCard = lowestTarokHopefullyWithoutPagat;
                            }
                        }
                    } else {
                        if (wantToKeepPagat) {
                            playedCard = lowestTarokHopefullyWithoutPagat;
                        } else {
                            playedCard = lowestTarokToWinTheTrick;
                        }
                    }
                } else {
                    if (canIBeatPlayedCards) {
                        if (worthRiskingMond) {
                            playedCard = playableCards.get(mondIndex);
                        } else {
                            if (isTrickWorthWinning) {
                                if (otherPlayersHaveColor) {
                                    playedCard = lowestTarokToWinTheTrick;
                                } else {
                                    playedCard = playableCards.getHighestTarokWithoutMond();
                                }
                            } else {
                                if (willTeamMateWin) {
                                    if (wantToKeepPagat) {
                                        playedCard = lowestTarokHopefullyWithoutPagat;
                                    } else {
                                        playedCard = lowestTarok;
                                    }
                                } else {
                                    playedCard = lowestTarokHopefullyWithoutPagat;
                                }
                            }
                        }
                    } else {
                        if (willTeamMateWin) {
                            if (wantToKeepPagat) {
                                playedCard = lowestTarokHopefullyWithoutPagat;
                            } else {
                                playedCard = lowestTarok;
                            }
                        } else {
                            playedCard = lowestTarokHopefullyWithoutPagat;
                        }
                    }
                }
            } else {
                if (willTeamMateWin) {
                    playedCard = mostValuablePlayableCard;
                } else {
                    playedCard = leastValuablePlayableCard;
                }
            }
        }
        //TODO: Choose suitable card in Warsaw
        playCard(playedCard);
        return playedCard;
    }

    public void addCardsToPile(CardsFan<Card> cards) {
        for (Card card : cards) {
            pile.add(card);
        }
    }

    public void sortHand() {
        hand.sort();
    }

    public void addCardToPile(Card card) {
        pile.add(card);
    }

    public void foldCards(CardsFan<Card> cards) {
        for (Card card : cards) {
            pile.add(card);
            hand.remove(card);
        }
    }

    public CardsFan<Card> getPile() {
        return pile;
    }

    public void clear() {
        pile.clear();
        hand.clear();
        announcements.clear();
    }

    public void printAnnouncements() {
        if(announcements.size() > 0) {
            if(allowPrint)System.out.print(this.name + "'s announcements:");
            for (int i = 0; i < announcements.size(); i++) {
                if(allowPrint)System.out.print(announcements.get(i).toString());
                if (i < announcements.size() - 1) {
                    if(allowPrint)System.out.print(", ");
                }
            }
            if(allowPrint)System.out.println();
        } else {
            if(allowPrint)System.out.println(this.name + " has no announcements.");
        }
    }
    
    public PlayType choosePlayType(ArrayList<PlayType> previousBids) {
        //TODO: Decide when to play second duty and when solo - don't forget to take previous bids into consideration!
        if(this.shallIPlayPreferanc()) {
            return PlayType.PREFERANC;
        } else {
            return PlayType.FIRST_DUTY;
        }
    }
    
    public boolean thinkAboutWarsaw() {
        //TODO: Decide when to play Warsaw
        return false;
    }

    /**
     * Iff the player has Barva or two-tarok Barvicka, he refuses to take card from talon
     * @return
     */
    public boolean wantCardFromTalon() {
        ArrayList<Announcement> announcements = resolveAnnouncements(hand);
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(AnnouncementName.BARVA)) {
                return false;
            }
        }

        int taroky_count = 0;
        for (Card card : hand) {
            if(card.getColor().equals(CardColor.TAROK)) {
                taroky_count++;
            }
        }
        return taroky_count != 2;
    }

    /**
     *
     * @param amount
     * @param playType
     */
    public void fold(int amount, PlayType playType) {
        //TODO: Decide which cards to keep and which add to pile based on current PlayType (e.g. in second duty you try to keep at least one card of each color)

        CardsFan<Card> hearts = new CardsFan<>();
        CardsFan<Card> spades = new CardsFan<>();
        CardsFan<Card> diamonds = new CardsFan<>();
        CardsFan<Card> clubs = new CardsFan<>();
        ArrayList<CardColor> kings = new ArrayList<>();
        Map<CardColor, Integer> counts = new HashMap<>();
        int tarokyCount = 0;

        for (Card card : hand) {
            switch (card.getColor()) {
                case TAROK:
                    tarokyCount++;
                    break;
                case HEARTS:
                    hearts.add(card);
                    break;
                case SPADES:
                    spades.add(card);
                    break;
                case DIAMONDS:
                    diamonds.add(card);
                    break;
                case CLUBS:
                    clubs.add(card);
            }
            if(card.getValue() == CardValue.KING) {
                kings.add(card.getColor());
            }
        }

        counts.put(CardColor.HEARTS, hearts.size());
        counts.put(CardColor.SPADES, spades.size());
        counts.put(CardColor.DIAMONDS, diamonds.size());
        counts.put(CardColor.CLUBS, clubs.size());

        /**
         * Folding taroky
         */
        int foldedTarokyAmount = tarokyCount + kings.size() - Game.CARDS_IN_HAND;
        if(foldedTarokyAmount > 0) {
            //TODO: Show folded taroky to other players
            this.foldCards(hand.foldTaroky(foldedTarokyAmount));
            amount -= foldedTarokyAmount;
        }

        /**
         * Folding all cards of some colors without kings
         */
        int i = 1;
        int initAmount = amount;
        while(amount >= i && i < Game.CARDS_IN_HAND + initAmount) {
            for (Map.Entry<CardColor, Integer> entry : counts.entrySet()) {
                CardColor key = entry.getKey();
                Integer count = entry.getValue();

                //TODO: Choose the best color to fold
                if(count == i && !kings.contains(key) && amount >= i) {
                    this.foldCards(hand.foldColor(key));
                    amount -= i;
                }
            }
            i++;
        }

        /**
         * Player can't fold all cards of some color now, so fold the most valuable cards
         */
        //TODO: Choose the best cards in some more sophisticated way
        this.foldCards(hand.foldValuable(amount));


    }

    /**
     * Decide whether it is a good idea to play preferanc
     * @return true if player wants to play preferanc, false otherwise
     */
    public boolean shallIPlayPreferanc() {
        int tarokyCount = 0;
        int tarokyStrength = 0;

        for (Card card : hand) {
            switch (card.getColor()) {
                case TAROK:
                    tarokyCount++;
                    tarokyStrength += card.getStrength();
                    break;
                default:
                    break;
            }
        }

        return strategy.shallIPlayPreferanc(tarokyCount, tarokyStrength);

    }

    public int resolvePreferancTalon(CardsFan<Card> talon) {
        final int PLAY_IN_FIRST_CARDS = 1;
        final int PLAY_IN_SECOND_CARDS = 2;
        final int PLAY_IN_THIRD_CARDS = 3;
        int tarokyCount = getTarokyCount();
        int tarokyStrength = getTarokyStrength();
        int[] talonTarokyCount = {0,0};
        int[] talonTarokyStrength = {0,0};
        int[] talonPoints = {0,0};
        int i = 0;
        for (Card card : talon) {
            if(card.getColor() == CardColor.TAROK) {
                talonTarokyCount[i / (Game.CARDS_IN_TALON / 2)]++;
                talonTarokyStrength[i / (Game.CARDS_IN_TALON / 2)] += card.getStrength();
            }
            talonPoints[i / (Game.CARDS_IN_TALON / 2)] += card.getPoints();
            i++;
        }

        //TODO: Take strategies into consideration (use different constants)
        if(talonTarokyCount[0] + tarokyCount >= 10 && talonTarokyCount[1] + tarokyCount < 10) {
            hand.addCard(talon.get(0));
            hand.addCard(talon.get(1));
            hand.addCard(talon.get(2));
            return PLAY_IN_THIRD_CARDS;
        } else if(talonTarokyCount[0] + tarokyCount < 6 && (tarokyStrength + talonTarokyStrength[0])/(talonTarokyCount[0] + tarokyCount) < 43 ) {
            hand.addCard(talon.get(0));
            hand.addCard(talon.get(1));
            hand.addCard(talon.get(2));
            return PLAY_IN_FIRST_CARDS;
        } else if(talonTarokyStrength[1] > 2 * talonTarokyStrength[0]) {
            hand.addCard(talon.get(3));
            hand.addCard(talon.get(4));
            hand.addCard(talon.get(5));
            return PLAY_IN_SECOND_CARDS;
        } else {
            hand.addCard(talon.get(0));
            hand.addCard(talon.get(1));
            hand.addCard(talon.get(2));
            return PLAY_IN_THIRD_CARDS;
        }

    }

    public int getAnnouncementPoints() {
        int points = 0;
        for (Announcement announcement : announcements) {
            points += announcement.getPoints();
        }
        return points;
    }

    //TODO: Bluffing
    public Card chooseCooperator() {
        if(!hand.contains(CardValue.XIX)) {
            return Game.getDeck().get(CardValue.XIX);
        } else if(!hand.contains(CardValue.XVIII)) {
            return Game.getDeck().get(CardValue.XVIII);
        } else if(!hand.contains(CardValue.XVII)) {
            return Game.getDeck().get(CardValue.XVII);
        } else if(!hand.contains(CardValue.XVI)) {
            return Game.getDeck().get(CardValue.XVI);
        } else {
            return Game.getDeck().get(CardValue.XIX);
        }
    }
}
