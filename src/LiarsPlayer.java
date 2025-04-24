import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class LiarsPlayer implements Liars {
    String name;
    ArrayList<Card> PlayerDeck = new ArrayList<>();
    Card FieldCard;
    String mainRank;
    Random random = new Random();

    public LiarsPlayer() {
        String[] FirstName = {"김", "이", "박", "최", "장", "조", "황"};
        String[] LastName = {"정태", "주언", "재호", "현범", "둘리", "흥민", "시현"};
        this.name = FirstName[random.nextInt(7)] + LastName[random.nextInt(7)];
        if (this.name.equals("조정태"))
            SP.s("하이미디어 학생 조정태라고 한다 반갑노", 300);
    }

    public ArrayList<Card> selectAct(ArrayList<Card> gameDeck) {
        ArrayList<Card> playerSelect = new ArrayList<>();
        while (playerSelect.isEmpty()) {
            int choice = random.nextInt(2);
            int randomCount = random.nextInt(3)+1;
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
                }break;

            }
        }this.PlayerDeck.removeAll(playerSelect);
        SP.s(this.name + "(이)가 카드를 내면서 말합니다! 😁 [" + this.mainRank + "] " + playerSelect.size() + "장! ", 1000);
        return playerSelect;

    }

    public void DrawFirstDeck(Card card) {
        SP.s(this.name + "가 카드를 드로우 합니다", 300);
        this.PlayerDeck.add(card);
    }

    public ArrayList<Card> submitCard() {
        boolean choice = random.nextBoolean();
        ArrayList<Card> playerSelect = new ArrayList<>();
        if (choice) {
            int stop = random.nextInt(3);
            for (Card c : this.PlayerDeck) {
                if (c.getRank().equals(this.mainRank)) {
                    playerSelect.add(c);
                    stop++;
                }
                if (stop > 3 || playerSelect.size() > 3) {
                    break;
                }
            }
            if (playerSelect.isEmpty())
                return playerSelect;
            this.PlayerDeck.removeAll(playerSelect);
            SP.s(this.name + "(이)가 카드를 내면서 말합니다! 😁 [" + this.mainRank + "] " + playerSelect.size() + "장! ", 1000);
            return playerSelect;
        } else {
            int stop = random.nextInt(3);
            for (Card c : this.PlayerDeck) {
                if (c.getRank().equals(this.mainRank)) {
                    playerSelect.add(c);
                    stop++;
                }
                if (stop > 3 || playerSelect.size() > 3) {
                    break;
                }
            }
            this.PlayerDeck.removeAll(playerSelect);
            SP.s(this.name + "(이)가 카드를 내면서 말합니다! 😁 [" + this.mainRank + "] " + playerSelect.size() + "장! ", 1000);
            return playerSelect;
        }

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