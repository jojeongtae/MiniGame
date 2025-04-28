import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class User implements Liars {
    private String id;
    private String pw;
    private String userName;
    private int userMoney;
    String mainRank;
    ArrayList<Card> PlayerDeck = new ArrayList<>();
    Scanner input = new Scanner(System.in);
    LiarsStrategy strategy;
    public User(String id, String pw, String userName, int userMoney) {
        this.id = id;
        this.pw = pw;
        this.userName = userName;
        this.userMoney = userMoney;
    }

    public String getId() {
        return this.id;
    }

    public String getPw() {
        return this.pw;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getUserMoney() {
        return this.userMoney;
    }


    public int setUserMoney(int userMoney) {
        this.userMoney = userMoney;
        return userMoney;
    }

    public int addUserMoney(int addMoney) {
        this.userMoney += addMoney;
        return userMoney;
    }
    //이 밑으로 라이어 게임 전용 함수들!!!!🐭


    public void LoseBet(int userBet) {
        this.userMoney -= userBet;
    }

    public void showDeck() {
        SP.s(this.getUserName() + "의 패",500);
        for(Card card : PlayerDeck){
            card.toCardString();
        }
        System.out.println();
    }

    public String getName() {
        return this.userName;
    }
    public void getBet(int money){
        this.userMoney=money;
    }
    public ArrayList<Card> selectAct(ArrayList<Card> gameDeck) {
        ArrayList<Card> selected = new ArrayList<>();
        SP.s("제출 할 카드 번호들을 3장까지 선택하세요 (여러장 선택시 중간에 공백)",300);
        showDeck();
        while (true) {
            String select = input.nextLine().trim();
            if (select.isEmpty()) {
                System.out.println("입력값이 없습니다. 다시 입력해주세요.");
                continue;
            }
            String[] arr = select.split(" ");

            if (arr.length > 3) {
                System.out.println("카드는 3장까지 가능합니다");
            } else if (arr.length > 0) {
                boolean flag = true;
                for (int i = arr.length - 1; i >= 0; i--) {
                    int index = Integer.parseInt(arr[i]) - 1;
                    if (index >= 0 && index < PlayerDeck.size()) {
                        selected.add(PlayerDeck.get(index));
                        PlayerDeck.remove(index);
                    } else {
                        System.out.println("잘못된 카드 번호: " + arr[i]);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    break;
                } else {
                    System.out.println("다시 시도하세요. 1~" + PlayerDeck.size() + " 범위의 카드 번호를 입력해주세요.");
                }
            } else {
                System.out.println("1장 이상의 카드를 선택해야 합니다.");
            }
        }
        Collections.reverse(selected);
        SP.s(getName() + "(이)가 카드를 내면서 말합니다! 😁 [" + this.mainRank + "] " + selected.size() + "장! ", 1000);
        return selected;
    }




    public boolean StrikeLiar(ArrayList<Card> LastPlayerCard) {
        for (Card c : LastPlayerCard) {
            if (!c.getRank().equals(this.mainRank) && !c.getRank().equals("Joker")) {
                return true;
            }
        }

        return false;
    }
    public ArrayList<Card> getHand(){
        return  this.PlayerDeck;
    }
    public LiarsStrategy getStrategy(){
        return null;
    }
}
