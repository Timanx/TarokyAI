import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, int index) {
        super(name, true, index);
    }

    public PlayType choosePlayType() {

        Integer input;

        boolean canPlaySecond = hand.contains(CardValue.PAGAT);
        PlayType playType = null;
        System.out.println();
        while(playType == null) {
            this.printHand();
            System.out.println("Choose play type:");
            System.out.println("First duty: 1");
            if (canPlaySecond) {
                System.out.println("Second duty: 2");
            }

            System.out.println("Preferanc: 3");
            System.out.println("Solo: 4");
            System.out.print("Write a number > ");
            Scanner in = new Scanner(System.in);
            try {
                input = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid play type.");
                continue;
            }

            switch (input) {
                case 1:
                    playType = PlayType.FIRST_DUTY;
                    System.out.println("You chose First duty.");
                    break;
                case 2:
                    if(canPlaySecond) {
                        playType = PlayType.SECOND_DUTY;
                        System.out.println("You chose Second duty.");
                    } else {
                        System.out.println("You don't have Pagat, so you can't play second duty.");
                    }
                    break;
                case 3:
                    playType = PlayType.PREFERANC;
                    System.out.println("You chose Preferanc.");
                    break;
                case 4:
                    playType = PlayType.SOLO;
                    System.out.println("You chose Solo.");
                    break;
                default:
                    System.out.println("Please enter a valid play type.");
                    break;
            }
        }

        return playType;
    }

    @Override
    public Card chooseCooperator() {
        Integer input;
        boolean hasXIX = hand.contains(CardValue.XIX);
        boolean hasXVIII = hand.contains(CardValue.XVIII);
        boolean hasXVII = hand.contains(CardValue.XVII);
        boolean hasXVI = hand.contains(CardValue.XVI);
        Card cooperator = null;

        if(!hasXIX || (hasXIX && hasXVIII && hasXVII && hasXVI)) {
            printHand();
            System.out.println("You will play with XIX as cooperator.");
            return Game.getDeck().get(CardValue.XIX);
        } else {
            while(cooperator == null) {
                printHand();
                System.out.println();
                System.out.println("Choose cooperator:");
                System.out.println("XIX: 1");
                if (!hasXVIII) {
                    System.out.println("XVIII: 2");
                } else if (!hasXVII) {
                    System.out.println("XVII: 3");
                } else if (!hasXVI) {
                    System.out.println("XVI: 4");
                }
                System.out.print("Write a number > ");
                Scanner in = new Scanner(System.in);
                try {
                    input = Integer.parseInt(in.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid card.");
                    continue;
                }

                switch (input) {
                    case 1:
                        cooperator = Game.getDeck().get(CardValue.XIX);
                        System.out.println("You chose XIX as your cooperator.");
                        break;
                    case 2:
                        if(!hasXVIII) {
                            cooperator = Game.getDeck().get(CardValue.XVIII);
                            System.out.println("You chose XVIII as your cooperator.");
                        } else {
                            System.out.println("You can't choose XVIII as you already have it.");
                        }
                        break;
                    case 3:
                        if(!hasXVII && !hasXVIII) {
                            cooperator = Game.getDeck().get(CardValue.XVII);
                            System.out.println("You chose XVII as your cooperator.");
                        } else {
                            System.out.println("You can't choose XVII as you already have it or you don't have XVIII.");
                        }
                        break;
                    case 4:
                        if(!hasXVII && !hasXVIII && !hasXVI) {
                            cooperator = Game.getDeck().get(CardValue.XVI);
                            System.out.println("You chose XVI as your cooperator.");
                        } else {
                            System.out.println("You can't choose XVI as you already have it or you don't have XVIII or XVII.");
                        }
                        break;
                    default:
                        System.out.println("Please enter a valid input.");
                        break;
                }
            }
        }

        return cooperator;
    }

    public void fold(int amount, PlayType playType) {

        String input;
        boolean repeat = true;

        while(repeat) {
            repeat = false;
            printHand();
            System.out.println();
            if (amount > 1) {
                System.out.println("Choose " + amount + " cards to fold.");
            } else {
                System.out.println("Choose a card to fold.");
            }
            for (int j = 0; j < hand.size(); j++) {
                System.out.print(hand.get(j).toString() + ": " + j);
                if (j != hand.size() - 1) {
                    System.out.print("\t\t");
                }
            }
            System.out.println();
            if (amount > 1) {
                System.out.print("Write " + amount + " numbers separated by space > ");
            } else {
                System.out.print("Write a number > ");
            }
            Scanner in = new Scanner(System.in);
            input = in.nextLine();

            String[] foldedCardStrings = input.split(" ", -1);
            ArrayList<Integer> foldedCardIndices = new ArrayList<>(6);
            int foldedTaroky = 0;

            for (String foldedCard : foldedCardStrings) {
                Integer foldedCardIndex;
                try {
                    foldedCardIndex = Integer.parseInt(foldedCard);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid card.");
                    break;
                }

                if (foldedCardIndex < 0 || foldedCardIndex > hand.size() - 1 || hand.get(foldedCardIndex).getPoints() == 5) {
                    System.out.println("You can't fold these cards.");
                    repeat = true;
                    break;
                } else if (hand.get(foldedCardIndex).getColor().equals(CardColor.TAROK)) {
                    foldedTaroky++;
                }
                foldedCardIndices.add(Integer.parseInt(foldedCard));
            }

            if (foldedTaroky > Math.max(amount - Game.CARDS_IN_HAND - hand.getUnfoldableCount(), 0)) {
                System.out.println("You can't fold so many taroky.");
                repeat = true;
            }

            if (!repeat) {
                CardsFan<Card> foldedCards = new CardsFan<>(6);
                System.out.print("You have folded ");
                for (int i = 0; i < foldedCardIndices.size(); i++) {
                    Integer foldedCardIndex = foldedCardIndices.get(i);
                    Card foldedCard = hand.get(foldedCardIndex);
                    System.out.print(foldedCard.toString());
                    if (i == foldedCardIndices.size() - 2) {
                        System.out.print(" and ");
                    } else if (i != foldedCardIndices.size() - 1) {
                        System.out.print(", ");
                    } else {
                        System.out.print(".");
                    }
                    System.out.println();
                    foldedCards.add(foldedCard);
                }
                this.foldCards(foldedCards);
            }
        }

    }

    public Card playCard(CardColor trickColor) {

        Integer input;

        while(true) {
            printHand();

            CardsFan<Card> playableCards = super.getPlayableCards(trickColor);
            System.out.println();
            System.out.println("You can play any of these cards:");
            for (int j = 0; j < playableCards.size(); j++) {
                System.out.print(playableCards.get(j).toString() + ": " + j);
                if (j != playableCards.size() - 1) {
                    System.out.print("\t\t");
                }
            }
            System.out.println();
            System.out.print("Write a number > ");

            Scanner in = new Scanner(System.in);

            try {
                input = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid card.");
                continue;
            }

            if(input >=  0 && input < playableCards.size()) {
                Card playedCard = playableCards.get(input);
                super.playCard(playedCard);
                return playedCard;
            } else {
                System.out.println("Please enter a valid card.");
            }
        }
    }

    private void printHand() {
        System.out.println("Your hand:");
        for (int j = 0; j < hand.size(); j++) {
            System.out.print(hand.get(j).toString());
            if (j != hand.size() - 1) {
                System.out.print("     ");
            }
        }
        System.out.println();
    }

    public int resolvePreferancTalon(CardsFan<Card> talon) {
        String input;

        while(true) {
            printHand();

            System.out.println("First cards:");

            for(int i = 0; i < talon.size()/2; i++) {
                System.out.print(talon.get(i).toString());
                if (i != talon.size()/2 - 1) {
                    System.out.print("     ");
                }
            }
            System.out.println();

            System.out.println("Do you want to play in first cards? (Y/N)");


            Scanner in = new Scanner(System.in);
            try {
                input = in.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Please enter Y (yes) or N (no).");
                continue;
            }

            if(input.equals("Y")) {
                hand.add(talon.get(0));
                hand.add(talon.get(1));
                hand.add(talon.get(2));
                System.out.println("You chose to play in first cards.");
                return 1;
            } else {
                while(true) {
                    printHand();

                    System.out.println("Second cards:");

                    for(int i = 3; i < talon.size(); i++) {
                        System.out.print(talon.get(i).toString());
                        if (i != talon.size() - 1) {
                            System.out.print("     ");
                        }
                    }
                    System.out.println();
                    System.out.println("Do you want to play in second cards? (Y/N)");

                    try {
                        input = in.nextLine();
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter Y (yes) or N (no).");
                        continue;
                    }

                    if(input.equals("Y")) {
                        hand.add(talon.get(3));
                        hand.add(talon.get(4));
                        hand.add(talon.get(5));
                        System.out.println("You chose to play in second cards.");
                        return 2;
                    } else {
                        hand.add(talon.get(0));
                        hand.add(talon.get(1));
                        hand.add(talon.get(2));
                        System.out.println("You chose to play in third cards.");
                        return 3;
                    }

                }
            }

        }


    }
}
