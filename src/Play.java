import java.util.ArrayList;

public  class Play {

    private CardsFan<Card> talon = new CardsFan<>(6);
    private CardsFan<Card> trick = new CardsFan<>(5);
    private Game game;
    private ArrayList<Player> players = new ArrayList<>(4);
    private PlayType playType;
    private ArrayList<Player> bidderTeam = new ArrayList<>(2);
    //TODO: bidder team public needs to be taken into consideration
    private boolean bidderTeamPublic = true;
    private boolean tookLastTrickWithPagat = false;
    private int obligatoryWarsaw = 0; //can stack multiple times

    private CardsFan<Card> playedCards = new CardsFan<>(54);
    private ArrayList<CardsFan<Card>> playedCardsByPlayer = new ArrayList<>();

    private static final boolean allowGameDataPrint = false;
    private static final boolean allowGameInfoPrint = false;

    //Card that highest bidder cooperates with during first or second duty
    private Card cooperation = null;

    //How many times will points form preferanc be multiplied
    private int preferancLevel;

    //initial dealer is the last player
    private int dealer;

    //player that leads the trick
    private int leader;

    //player that makes the highest bid
    private int bidder;



    Play(Game game, ArrayList<Player> players, int dealer) {
       this.game = game;
       this.players = players;
       this.dealer = dealer;
       this.leader = (dealer + 1) % Game.PLAYERS_COUNT;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Base method that simulates one play
     * @return array of points
     */
    public int[] play() {

        playedCardsByPlayer.add(new CardsFan<Card>());
        playedCardsByPlayer.add(new CardsFan<Card>());
        playedCardsByPlayer.add(new CardsFan<Card>());
        playedCardsByPlayer.add(new CardsFan<Card>());


        dealCards();
        bidHand();
        splitTalon();
        sortPlayerCards();
        calculateAnnouncements();
        draw();
        if(allowGameInfoPrint)System.out.println("-------------------------------------------------------");
        return calculatePoints();
    }

    /**
     * Give each player 12 cards and place 6 to the talon
     */
    private void dealCards() {
        //TODO: Take different kinds of cards dealing into consideration (Probably not - first, a special shuffling would have to be implemented)
        CardsFan<Card> deck = game.getShuffledDeck();
        for(int i = 0; i < Game.PLAYERS_COUNT * Game.CARDS_IN_HAND; i++) {
            players.get(i % Game.PLAYERS_COUNT).addToHand(deck.get(i));
        }
        for(int i = Game.PLAYERS_COUNT * Game.CARDS_IN_HAND; i < Game.CARDS_IN_DECK; i++) {
            talon.add(deck.get(i));
        }

        for(Player player : players) {
            player.getHand().sort();
        }

        if(allowGameInfoPrint)System.out.println("Cards have been dealt!");
        printInitialHands();
    }

    /**
     * Get announcements like Barvicka or Tarocky from each player
     */
    private void calculateAnnouncements() {
        for(Player player : players) {
            player.resolveAnnouncements();
        }
        printHandsBeforeGame();
    }

    /**
     * Choose playType for this play
     */
    private void bidHand() {
        ArrayList<PlayType> previousBids = new ArrayList<>(4);

        for(int i = 0; i < Game.PLAYERS_COUNT; i++) {
            if(players.get((i + leader) % Game.PLAYERS_COUNT).isHuman) {
                previousBids.add(((HumanPlayer) players.get((i + leader) % Game.PLAYERS_COUNT)).choosePlayType());
            } else {
                PlayType tmpType = players.get((i + leader) % Game.PLAYERS_COUNT).choosePlayType(previousBids);
                if(allowGameInfoPrint)System.out.println(players.get((i + leader) % Game.PLAYERS_COUNT).getName() + " plays " + tmpType.toString());
                previousBids.add(tmpType);
            }

        }

        PlayType highestBidder = PlayType.FIRST_DUTY;
        bidder = leader;
        for (int i = 0; i < Game.PLAYERS_COUNT; i++) {
            if(previousBids.get(i).compareTo(highestBidder) > 0) {
                highestBidder = previousBids.get(i);
                bidder = (i + leader) % Game.PLAYERS_COUNT;
                players.get(bidder).makeBidder();
            }
        }

        if(highestBidder == PlayType.FIRST_DUTY && (obligatoryWarsaw > 0 || players.get(leader).thinkAboutWarsaw())) {
            playType = PlayType.WARSAW;
        } else {
            playType = highestBidder;
        }

        if(playType == PlayType.PREFERANC || playType == PlayType.SOLO) {
            bidderTeamPublic = true;
        }

    }

    /**
     * Let each player take take specified amount of cards from the talon
     */
    private void splitTalon() {
        if(playType == PlayType.FIRST_DUTY || playType == PlayType.SECOND_DUTY) {
            Player currentPlayer = players.get(bidder);

            cooperation = currentPlayer.chooseCooperator();


            currentPlayer.addToHand(talon.remove(0));
            currentPlayer.addToHand(talon.remove(0));
            currentPlayer.addToHand(talon.remove(0));
            currentPlayer.addToHand(talon.remove(0));
            int remaining = 2;
            int leftForBidder = 0;
            currentPlayer = players.get((bidder + 1) % Game.PLAYERS_COUNT);
            if(currentPlayer.wantCardFromTalon()) {
                currentPlayer.addToHand(talon.remove(0));
                currentPlayer.fold(1, playType);
                remaining--;
            }
            currentPlayer = players.get((bidder + 2) % Game.PLAYERS_COUNT);
            if(currentPlayer.wantCardFromTalon()) {
                currentPlayer.addToHand(talon.remove(0));
                currentPlayer.fold(1, playType);
                remaining--;
            }
            currentPlayer = players.get((bidder + 3) % Game.PLAYERS_COUNT);
            if(remaining > 0 && currentPlayer.wantCardFromTalon()) {
                currentPlayer.addToHand(talon.remove(0));
                currentPlayer.fold(1, playType);
                remaining--;
            }
            currentPlayer = players.get(bidder);
            if(remaining > 0) {
                currentPlayer.addToHand(talon.remove(0));
                remaining--;
                leftForBidder++;
            }
            if(remaining > 0) {
                currentPlayer.addToHand(talon.remove(0));
                leftForBidder++;
            }
            currentPlayer.fold(4 + leftForBidder, playType);
        } else if(playType.equals(PlayType.PREFERANC)) {
            Player currentPlayer = players.get(bidder);
            int preferancChoice;
            if(currentPlayer.isHuman) {
                currentPlayer.sortHand();
                preferancLevel = ((HumanPlayer) currentPlayer).resolvePreferancTalon(talon);
                currentPlayer.sortHand();
                ((HumanPlayer) currentPlayer).fold(3, playType);
            } else {
                preferancChoice = currentPlayer.resolvePreferancTalon(talon);
                preferancLevel = preferancChoice;
                currentPlayer.fold(3, playType);
                if(preferancChoice == 1) {
                    printlnString(currentPlayer.getName() + " plays in first cards.");
                    for(int i = 3; i < talon.size(); i++) {
                        if (talon.get(i).getColor() == CardColor.TAROK || talon.get(i).getPoints() == 5) {
                            printlnString(talon.get(i).toString() + " is a public talon card.");
                        }
                    }
                } else if(preferancChoice == 2) {
                    printlnString(currentPlayer.getName() + " plays in second cards.");
                    for(int i = 0; i < talon.size()/2; i++) {
                        printlnString(talon.get(i).toString() + " is a public talon card.");
                    }
                } else {
                    printlnString(currentPlayer.getName() + " plays in third cards.");
                    for(int i = 3; i < talon.size(); i++) {
                        printlnString(talon.get(i).toString() + " is a public talon card.");
                    }
                }
            }
        }

        if(playType.equals(PlayType.PREFERANC)) {
            int a = 1;
        }

        printNewLine();
        printlnString(players.get(bidder).getName() + " plays " + playType.toString() + ".");
        if(cooperation != null) {
            if(allowGameInfoPrint)System.out.println(players.get(bidder).getName() + " has chosen " + cooperation.toString() + " as cooperator.");
        }
    }

    private void sortPlayerCards() {
        for (Player player : players) {
            player.sortHand();
        }
    }

    private void playTrick(int trickOrder) {
        Card currentlyPlayedCard;
        if(players.get(leader).isHuman) {
            currentlyPlayedCard = ((HumanPlayer) players.get(leader)).playCard((CardColor) null);
        } else {
            currentlyPlayedCard = players.get(leader).playLeadCard(playedCards);
        }
        playedCards.add(currentlyPlayedCard);
        trick.add(currentlyPlayedCard);
        printlnString(players.get(leader).getName() + " plays " + currentlyPlayedCard.toString());
        for(int j = 1; j < Game.PLAYERS_COUNT; j++) {
            if(players.get((leader + j) % Game.PLAYERS_COUNT).isHuman) {
                currentlyPlayedCard = ((HumanPlayer) players.get((leader + j) % Game.PLAYERS_COUNT)).playCard(trick.get(0).getColor());
            } else {
                currentlyPlayedCard =  players.get((leader + j) % Game.PLAYERS_COUNT).playSuitableCard(trick, bidderTeam, bidder, playType, bidderTeamPublic, playedCards, playedCardsByPlayer, talon); //TODO: fix talon to known talon
            }
            playedCards.add(currentlyPlayedCard);
            playedCardsByPlayer.get(players.get((leader + j) % Game.PLAYERS_COUNT).getIndex()).add(currentlyPlayedCard);
            if(currentlyPlayedCard.equals(cooperation)) {
                bidderTeamPublic = true;
            }
            trick.add(currentlyPlayedCard);
            printlnString(players.get((leader + j) % Game.PLAYERS_COUNT).getName() + " plays " + currentlyPlayedCard.toString());
        }

        if(findTrickWinner().getValue().equals(CardValue.PAGAT) && trickOrder == Game.CARDS_IN_HAND - 1) {
            tookLastTrickWithPagat = true;
        }
        printHandsDuringGame(Game.CARDS_IN_HAND - trickOrder - 1);
    }

    private void findCooperators() {
        //TODO: Add bluffing possibility when player has XIX and wants to cooperate with XIX nevertheless
        Player bidderPlayer = players.get(bidder);
        bidderTeam.add(bidderPlayer);
        if(playType == PlayType.FIRST_DUTY || playType == PlayType.SECOND_DUTY) {
            CardValue cooperatorCard;

            if(bidderPlayer.hasCard(CardColor.TAROK, CardValue.XIX)) {
                if(bidderPlayer.hasCard(CardColor.TAROK, CardValue.XVIII)) {
                    if(bidderPlayer.hasCard(CardColor.TAROK, CardValue.XVII)) {
                        if(bidderPlayer.hasCard(CardColor.TAROK, CardValue.XVI)) {
                            cooperatorCard = CardValue.XIX;
                        } else {
                            cooperatorCard = CardValue.XVI;
                        }
                    } else {
                        cooperatorCard = CardValue.XVII;
                    }
                } else {
                    cooperatorCard = CardValue.XVIII;
                }
            } else {
                cooperatorCard = CardValue.XIX;
            }

            for (Player player : players) {
                if(!player.isBidder() && player.hasCard(CardColor.TAROK, cooperatorCard)) {
                    bidderTeam.add(player);
                    return;
                }
            }
        }
    }

    private void draw() {
        findCooperators();

        for(int i = 0; i < Game.CARDS_IN_HAND; i++) {
            playTrick(i);
            if(allowGameInfoPrint)System.out.println("-------------------------------------------------------");
        }
    }

    private Card findTrickWinner() {
        CardColor trickColor = trick.get(0).getColor();
        Card trickWinnerCard = trick.get(0);
        int trickWinnerPlayer = leader;

        for (int i = 1; i < Game.PLAYERS_COUNT; i++) {
            Card currentCard = trick.get(i);
            if((currentCard.getColor() == trickColor || currentCard.getColor() == CardColor.TAROK) && Card.compare(currentCard, trickWinnerCard)) {
                trickWinnerCard = currentCard;
                trickWinnerPlayer = (i + leader) % 4;
            }
        }

        players.get(trickWinnerPlayer).addCardsToPile(trick);
        if(playType == PlayType.WARSAW && !talon.isEmpty()) {
            players.get(trickWinnerPlayer).addCardToPile(talon.remove(0));
        }
        leader = trickWinnerPlayer;
        trick.clear();
        printlnString(players.get(leader).getName() + " wins the trick.");
        printNewLine();
        return trickWinnerCard;
    }

    private int[] calculatePoints() {
        int points[] = {0,0,0,0,0};
        int bidderTeamPoints = 0;
        CardsFan<Card> bidderTeamPile = new CardsFan<>();
        int tmpPoints = 0;

        for (Player player : players) {
            if(!bidderTeam.contains(player)) {
                bidderTeamPoints -= player.getAnnouncementPoints();
            } else {
                bidderTeamPile.addAll(player.getPile());
                bidderTeamPoints += player.getAnnouncementPoints();
            }

        }

        if(playType != PlayType.WARSAW) {
            tmpPoints = bidderTeamPile.calculatePoints() - Game.TOTAL_POINTS / 2;
            if (tmpPoints == Game.TOTAL_POINTS / 2) {
                obligatoryWarsaw++;
            }

            if(playType == PlayType.FIRST_DUTY) {
                bidderTeamPoints += tmpPoints;
            } else if(playType == PlayType.SECOND_DUTY) {
                bidderTeamPoints += tmpPoints;
                //TODO: Calculating points in second duty - Pagat and Jew
            } else if(playType == PlayType.PREFERANC) {
                bidderTeamPoints += tmpPoints * preferancLevel;
            } else if(playType == PlayType.SOLO) {
                bidderTeamPoints += tmpPoints * 4;
            }

            for (Player player : players) {
                if(!bidderTeam.contains(player)) {
                    points[player.getIndex()] = -bidderTeamPoints;
                } else {
                    if(bidderTeam.size() == 2) {
                        points[player.getIndex()] = bidderTeamPoints;
                    } else {
                        points[player.getIndex()] = 3 * bidderTeamPoints;
                    }
                }
            }
        } else {
            if(obligatoryWarsaw > 0) {
                obligatoryWarsaw--;
            }

            int multiplication = 1;
            for (Player player : players) {
                points[player.getIndex()] = player.getPile().calculatePoints();
                if(points[player.getIndex()] == 0) {
                    multiplication *= 2;
                }
            }
            for (Player player : players) {
                points[player.getIndex()] *= multiplication;
            }
        }

        return points;
    }

    public void clear() {
        for (Player player : players) {
            player.clear();
        }
        talon.clear();
        trick.clear();
        playType = null;
        bidderTeam.clear();
    }

    /**
     * PRINTS
     */

    private void printInitialHands() {
        if(allowGameDataPrint) {
            for (int i = 0; i < Game.PLAYERS_COUNT; i++) {
                System.out.print(players.get(i).getName() + ": ");
                for (int j = 0; j < Game.CARDS_IN_HAND; j++) {
                    System.out.print(players.get(i).getHand().get(j).toString());
                    if (j != Game.CARDS_IN_HAND - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
            System.out.print("Talon: ");
            for (int i = 0; i < Game.CARDS_IN_TALON; i++) {
                System.out.print(talon.get(i).toString());
                if (i != Game.CARDS_IN_TALON - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    private void printNewLine() {
        System.out.println();
    }

    private void printlnString(String string) {
        if(allowGameInfoPrint)System.out.println(string);
    }

    private void printString(String string) {
        System.out.print(string);
    }

    private void printHandsBeforeGame() {
        if(allowGameDataPrint) {
            System.out.println("Hands after drawing talon.");
            for (int i = 0; i < Game.PLAYERS_COUNT; i++) {
                System.out.print(players.get(i).getName() + ": ");
                for (int j = 0; j < Game.CARDS_IN_HAND; j++) {
                    System.out.print(players.get(i).getHand().get(j).toString());
                    if (j != Game.CARDS_IN_HAND - 1) {
                        System.out.print(", ");
                    }
                }
                if (players.get(i).getAnnouncements().size() > 0) {
                    System.out.print(" - Announcements: ");
                    players.get(i).printAnnouncements();
                }
                System.out.println();
            }
        }
    }

    private void printHandsDuringGame(int cardsInHand) {
        if(allowGameDataPrint) {
            System.out.println("Hands after round " + (Game.CARDS_IN_HAND - cardsInHand) + ".");
            for (int i = 0; i < Game.PLAYERS_COUNT; i++) {
                System.out.print(players.get(i).getName() + ": ");
                for (int j = 0; j < cardsInHand; j++) {
                    System.out.print(players.get(i).getHand().get(j).toString());
                    if (j != Game.CARDS_IN_HAND - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }


}


