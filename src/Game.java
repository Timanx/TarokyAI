import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {

    public static final int CARDS_IN_DECK = 54;
    public static final int CARDS_IN_HAND = 12;
    public static final int CARDS_IN_TALON = 6;
    public static final int PLAYERS_COUNT = 4;
    public static final int TOTAL_POINTS = 70;
    public static final int END_GAME_POINTS = 1000000;
    public static final int MAX_GAMES = 10000;
    public static final int PREFERANC_FUNCTION_LINES = 529;


    private static CardsFan<Card> deck = new CardsFan<>(54);
    public static CardsFan<Card> taroky = new CardsFan<>(22);
    private ArrayList<Player> players = new ArrayList<>(4);
    private int[] points = {0, 0, 0, 0, 0};
    public static int[] preferanc_function = new int[PREFERANC_FUNCTION_LINES];


    Game() {
        initDeck();
        readPreferancFunction();
        addPlayers();

        int maxPoints = 0;
        int gamesCount = 0;
        while(maxPoints < END_GAME_POINTS && gamesCount < MAX_GAMES) {

            maxPoints = 0;
            Play play = new Play(this, players, gamesCount % PLAYERS_COUNT);


            int[] playPoints = play.play();

            System.out.println("Scores after previous round:");
            for(int i = 0; i < PLAYERS_COUNT + 1; i++) {

                if(i == PLAYERS_COUNT) {
                    System.out.println("Jew:\t\t" + points[i]);
                } else {
                    System.out.println(play.getPlayers().get(i).getName() + ":\t\t" + points[i]);
                }

                if(i < 4 && Math.abs(points[i]) > maxPoints) {
                    maxPoints = Math.abs(points[i]);
                }
            }
            System.out.println("Scores after this round (" + (gamesCount + 1) + "):");
            for(int i = 0; i < PLAYERS_COUNT + 1; i++) {
                points[i] += playPoints[i];

                if(i == PLAYERS_COUNT) {
                    System.out.println("Jew:\t\t" + points[i]);
                } else {
                    System.out.println(play.getPlayers().get(i).getName() + ":\t\t" + points[i]);
                }

                if(i < 4 && Math.abs(points[i]) > maxPoints) {
                    maxPoints = Math.abs(points[i]);
                }
            }
            System.out.println();

            play.clear();
            gamesCount++;
        }
    }

    public CardsFan<Card> getShuffledDeck() {
        Collections.shuffle(deck);
        return deck;
    }

    public static CardsFan<Card> getDeck() {
        return deck;
    }

    private void addPlayers() {
        //players.add(new HumanPlayer("Timan", 0));



/*
        System.out.println("Write your name");
        System.out.print("> ");
        String name;
        Scanner in = new Scanner(System.in);
        name = in.nextLine();*/

        players.add(new Player("Alice R", Strategy.CAREFUL, 0));
        players.add(new Player("Bob C", Strategy.CAREFUL, 1));
        //players.add(new HumanPlayer(name, 1));
        players.add(new Player("Cindy C", Strategy.PLAY_POSSUM, 2));
        players.add(new Player("Dean C", Strategy.CAREFUL, 3));
    }

    private void initDeck() {

        deck.add(new Card(CardColor.SPADES, CardValue.SEVEN, 1, 0));
        deck.add(new Card(CardColor.SPADES, CardValue.EIGHT, 1, 1));
        deck.add(new Card(CardColor.SPADES, CardValue.NINE, 1, 2));
        deck.add(new Card(CardColor.SPADES, CardValue.TEN, 1, 3));
        deck.add(new Card(CardColor.SPADES, CardValue.JACK, 2, 4));
        deck.add(new Card(CardColor.SPADES, CardValue.RIDER, 3, 5));
        deck.add(new Card(CardColor.SPADES, CardValue.QUEEN, 4, 6));
        deck.add(new Card(CardColor.SPADES, CardValue.KING, 5, 7));

        deck.add(new Card(CardColor.HEARTS, CardValue.ONE, 1, 0));
        deck.add(new Card(CardColor.HEARTS, CardValue.TWO, 1, 1));
        deck.add(new Card(CardColor.HEARTS, CardValue.THREE, 1, 2));
        deck.add(new Card(CardColor.HEARTS, CardValue.FOUR, 1, 3));
        deck.add(new Card(CardColor.HEARTS, CardValue.JACK, 2, 4));
        deck.add(new Card(CardColor.HEARTS, CardValue.RIDER, 3, 5));
        deck.add(new Card(CardColor.HEARTS, CardValue.QUEEN, 4, 6));
        deck.add(new Card(CardColor.HEARTS, CardValue.KING, 5, 7));

        deck.add(new Card(CardColor.DIAMONDS, CardValue.ONE, 1, 0));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.TWO, 1, 1));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.THREE, 1, 2));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.FOUR, 1, 3));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.JACK, 2, 4));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.RIDER, 3, 5));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.QUEEN, 4, 6));
        deck.add(new Card(CardColor.DIAMONDS, CardValue.KING, 5, 7));

        deck.add(new Card(CardColor.CLUBS, CardValue.SEVEN, 1, 0));
        deck.add(new Card(CardColor.CLUBS, CardValue.EIGHT, 1, 1));
        deck.add(new Card(CardColor.CLUBS, CardValue.NINE, 1, 2));
        deck.add(new Card(CardColor.CLUBS, CardValue.TEN, 1, 3));
        deck.add(new Card(CardColor.CLUBS, CardValue.JACK, 2, 4));
        deck.add(new Card(CardColor.CLUBS, CardValue.RIDER, 3, 5));
        deck.add(new Card(CardColor.CLUBS, CardValue.QUEEN, 4, 6));
        deck.add(new Card(CardColor.CLUBS, CardValue.KING, 5, 7));

        deck.add(new Card(CardColor.TAROK, CardValue.PAGAT, 5, 32));
        deck.add(new Card(CardColor.TAROK, CardValue.II, 1, 33));
        deck.add(new Card(CardColor.TAROK, CardValue.III, 1, 34));
        deck.add(new Card(CardColor.TAROK, CardValue.IIII, 1, 35));
        deck.add(new Card(CardColor.TAROK, CardValue.V, 1, 36));
        deck.add(new Card(CardColor.TAROK, CardValue.VI, 1, 37));
        deck.add(new Card(CardColor.TAROK, CardValue.VII, 1, 38));
        deck.add(new Card(CardColor.TAROK, CardValue.VIII, 1, 39));
        deck.add(new Card(CardColor.TAROK, CardValue.IX, 1, 40));
        deck.add(new Card(CardColor.TAROK, CardValue.X, 1, 41));
        deck.add(new Card(CardColor.TAROK, CardValue.XI, 1, 42));
        deck.add(new Card(CardColor.TAROK, CardValue.XII, 1, 43));
        deck.add(new Card(CardColor.TAROK, CardValue.XIII, 1, 44));
        deck.add(new Card(CardColor.TAROK, CardValue.XIV, 1, 45));
        deck.add(new Card(CardColor.TAROK, CardValue.XV, 1, 46));
        deck.add(new Card(CardColor.TAROK, CardValue.XVI, 1, 47));
        deck.add(new Card(CardColor.TAROK, CardValue.XVII, 1, 48));
        deck.add(new Card(CardColor.TAROK, CardValue.XVIII, 1, 49));
        deck.add(new Card(CardColor.TAROK, CardValue.XIX, 1, 50));
        deck.add(new Card(CardColor.TAROK, CardValue.XX, 1, 51));
        deck.add(new Card(CardColor.TAROK, CardValue.MOND, 5, 52));
        deck.add(new Card(CardColor.TAROK, CardValue.SKYZ, 5, 53));


        taroky.add(new Card(CardColor.TAROK, CardValue.PAGAT, 5, 32));
        taroky.add(new Card(CardColor.TAROK, CardValue.II, 1, 33));
        taroky.add(new Card(CardColor.TAROK, CardValue.III, 1, 34));
        taroky.add(new Card(CardColor.TAROK, CardValue.IIII, 1, 35));
        taroky.add(new Card(CardColor.TAROK, CardValue.V, 1, 36));
        taroky.add(new Card(CardColor.TAROK, CardValue.VI, 1, 37));
        taroky.add(new Card(CardColor.TAROK, CardValue.VII, 1, 38));
        taroky.add(new Card(CardColor.TAROK, CardValue.VIII, 1, 39));
        taroky.add(new Card(CardColor.TAROK, CardValue.IX, 1, 40));
        taroky.add(new Card(CardColor.TAROK, CardValue.X, 1, 41));
        taroky.add(new Card(CardColor.TAROK, CardValue.XI, 1, 42));
        taroky.add(new Card(CardColor.TAROK, CardValue.XII, 1, 43));
        taroky.add(new Card(CardColor.TAROK, CardValue.XIII, 1, 44));
        taroky.add(new Card(CardColor.TAROK, CardValue.XIV, 1, 45));
        taroky.add(new Card(CardColor.TAROK, CardValue.XV, 1, 46));
        taroky.add(new Card(CardColor.TAROK, CardValue.XVI, 1, 47));
        taroky.add(new Card(CardColor.TAROK, CardValue.XVII, 1, 48));
        taroky.add(new Card(CardColor.TAROK, CardValue.XVIII, 1, 49));
        taroky.add(new Card(CardColor.TAROK, CardValue.XIX, 1, 50));
        taroky.add(new Card(CardColor.TAROK, CardValue.XX, 1, 51));
        taroky.add(new Card(CardColor.TAROK, CardValue.MOND, 5, 52));
        taroky.add(new Card(CardColor.TAROK, CardValue.SKYZ, 5, 53));
    }

    private void readPreferancFunction() {
        String preferancFunction = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,98,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100";

        String[] strings = preferancFunction.split(",");

        int i = 0;
        for(String string : strings) {
            preferanc_function[i] = Integer.parseInt(string);
            i++;
        }
    }
}
