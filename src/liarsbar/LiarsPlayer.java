package liarsbar;

import common.Card;
import common.SP;

import java.util.ArrayList;
import java.util.Random;

public class LiarsPlayer implements Liars {
    String name;
    ArrayList<Card> PlayerDeck = new ArrayList<>();
    Card FieldCard;
    String mainRank;
    Random random = new Random();
    LiarsStrategy strategy;

    public LiarsPlayer() {
        String[] FirstName = {"김", "이", "박", "최", "장", "조", "황","손","지"};
        String[] LastName = {"정태", "주언", "재호", "현범", "둘리", "흥민", "시현","준홍","선아","민규","창현"};
        this.name = FirstName[random.nextInt(9)] + LastName[random.nextInt(11)];
        setRandomStrategy();
        if (this.name.equals("조정태")) {
            SP.s("하이미디어 학생 조정태 등장", 300);
            setStrategy(LiarsStrategy.CHEATER);
        }
    }

    public void setRandomStrategy() {
        LiarsStrategy[] strategies = LiarsStrategy.values();
        this.strategy = strategies[random.nextInt(strategies.length)];
    }

    public void setStrategy(LiarsStrategy strategy) {
        this.strategy = strategy;
    }

    public LiarsStrategy getStrategy() {
        return strategy;
    }

    public ArrayList<Card> selectAct(ArrayList<Card> gameDeck) {
        ArrayList<Card> playerSelect = new ArrayList<>();
        while (playerSelect.isEmpty()) {
            switch (this.strategy) {
                case DEFENSIVE:
                    int roll = random.nextInt(10);
                    int randomCount = random.nextInt(2) + 1;
                    if (roll < 9) {
                        for (Card c : this.PlayerDeck) {
                            if (c.getRank().equals(this.mainRank) || c.getRank().equals("Joker")) {
                                playerSelect.add(c);
                                if (randomCount <= playerSelect.size()) {
                                    break;
                                }
                            }

                        }
                    } else {
                        for (Card c : this.PlayerDeck) {
                            playerSelect.add(c);
                            if (randomCount <= playerSelect.size()) {
                                break;
                            }
                        }

                    }
                    break;
                case AGGRESSIVE:
                    roll = random.nextInt(10);
                    randomCount = Math.max(2,random.nextInt(3) + 1);
                    if (roll < 2) {
                        for (Card c : this.PlayerDeck) {
                            if (c.getRank().equals(this.mainRank) || c.getRank().equals("Joker")) {
                                playerSelect.add(c);
                                if (randomCount <= playerSelect.size()) {
                                    break;
                                }
                            }

                        }
                    } else {
                        for (Card c : this.PlayerDeck) {
                            playerSelect.add(c);
                            if (randomCount <= playerSelect.size()) {
                                break;
                            }
                        }

                    }
                    break;
                case RANDOM:
                    int choice = random.nextInt(2);
                    randomCount = random.nextInt(3) + 1;
                    switch (choice) {
                        case 0:

                            for (Card c : this.PlayerDeck) {
                                if (c.getRank().equals(this.mainRank) || c.getRank().equals("Joker")) {
                                    playerSelect.add(c);
                                    if (randomCount <= playerSelect.size()) {
                                        break;
                                    }
                                }

                            }
                            break;
                        case 1:
                            for (Card c : this.PlayerDeck) {
                                playerSelect.add(c);
                                if (randomCount <= playerSelect.size()) {
                                    break;
                                }
                            }
                            break;

                    }
                    break;
                case CHEATER:
                    roll = random.nextInt(10);
                    randomCount = random.nextInt(2) + 1;
                    if (roll < 10) {
                        for (Card c : this.PlayerDeck) {
                            if (c.getRank().equals(this.mainRank) || c.getRank().equals("Joker")) {
                                playerSelect.add(c);
                                if (randomCount <= playerSelect.size()) {
                                    break;
                                }
                            }

                        }
                    } else {
                        for (Card c : this.PlayerDeck) {
                            playerSelect.add(c);
                            if (randomCount <= playerSelect.size()) {
                                break;
                            }
                        }

                    }
                    break;
            }
        }
        this.PlayerDeck.removeAll(playerSelect);
        SP.s(this.name + "(이)가 카드를 내면서 말합니다! 😁 [" + this.mainRank + "] " + playerSelect.size() + "장! ", 1000);
        return playerSelect;

    }


    public boolean StrikeLiar(ArrayList<Card> LastPlayerCard) {

        for (Card c : LastPlayerCard) {
            if (!c.getRank().equals(this.mainRank) && !c.getRank().equals("Joker")) {
                return true; // 하나라도 다르면 거짓말
            }
        }

        return false; // 전부 다 같으면 진실
    }

    public void showDeck() {
        SP.s(this.name + "의 패", 500);
        for (Card card : PlayerDeck) {
            card.toCardString();
        }
        System.out.println();
    }

    public ArrayList<Card> getHand() {
        return this.PlayerDeck;
    }

    public String getName() {
        return this.name;
    }
}