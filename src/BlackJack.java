import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackJack {
    Deck deck;
    User user;
    List<Card> playerCard;
    List<Card> dealerCard;
    Scanner input = new Scanner(System.in);
    public BlackJack(User user){
        this.user = user;
    }
    public void start(){
        while(true) {
            System.out.println("블랙잭 게임을 시작 하겠습니다.");
            System.out.println("판돈은 판당 1만원 입니다.");
            user.setUserMoney(user.getUserMoney()-10000);
            deck = new Deck();
            playerCard = new ArrayList<>();
            dealerCard = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                playerCard.add(deck.drawCard());
                dealerCard.add(deck.drawCard());
            }
            System.out.println("내 카드 : " + myHand(playerCard));
            System.out.println("딜러 카드 : " + dealerCard.get(0) + " ???");
            checkAce(playerCard);
            while (true) {
                System.out.println("1.힛(H) 2.더블 다운(D) 3.스테이(Enter)");
                String yn = input.nextLine();
                if (yn.toUpperCase().equals("H")) {
                    playerCard.add(deck.drawCard());
                    checkAce(playerCard);
                    System.out.println("내 카드 : " + myHand(playerCard));
                    int total = totalHand(playerCard);
                    if (total > 21) {
                        user.setLoseUserMoney(user.getUserMoney());
                        System.out.println("버스트! 당신의 패배");
                        System.out.println("내 카드 : " + myHand(playerCard));
                        System.out.println("딜러 카드 : " + myHand(dealerCard));
                        System.out.println("남은 금액 : " + user.getUserMoney());
                        break;
                    }
                } else if (yn.toUpperCase().equals("D")){
                    playerCard.add(deck.drawCard());
                    user.setUserMoney(user.getUserMoney()-10000);
                    break;
                }else {
                    break;
                }
            }
            System.out.println("딜러 차례...");
            while (totalHand(dealerCard) < 17) {
                dealerCard.add(deck.drawCard());
            }
            System.out.println("딜러카드 : " + myHand(dealerCard));

            int playerTotal = totalHand(playerCard);
            int dealerTotal = totalHand(dealerCard);

            if (playerTotal <= 21) {
                System.out.println("내 점수: " + playerTotal);
                System.out.println("딜러 점수: " + dealerTotal);

                if (dealerTotal > 21 || playerTotal > dealerTotal) {
                    System.out.println("당신의 승리!");
                    user.setWinUserMoney(user.getUserMoney());
                    System.out.println("남은 금액 : " + user.getUserMoney());
                } else if (playerTotal < dealerTotal) {
                    System.out.println("딜러의 승리!");
                    user.setLoseUserMoney(user.getUserMoney());
                    System.out.println("남은 금액 : " + user.getUserMoney());
                } else {
                    System.out.println("무승부!");
                }
            }

            System.out.println("다시 하시겠습니까? Y/N");
            String restart = input.nextLine();
            if (restart.toUpperCase().equals("Y")){
                continue;
            }else{
                break;
            }
        }
    }

    private String myHand(List<Card> hand){
        String result = "";
        for (int i = 0; i < hand.size(); i++) { //내 패의 길이만큼반복
            result += hand.get(i) + " "; // 카드 한 장씩 꺼내서 문자열로 붙이기
        }
        return result;
    }


    private int totalHand(List<Card> hand){
        int total = 0;
        for (int i = 0 ; i<hand.size();i++){
            int value = hand.get(i).getValue();
            total += value;
            if (value == 1){
            }
        }

return total;
    }
    private void checkAce(List<Card> hand) {
        for (int i = 0 ; i<hand.size();i++) {
            Card card = hand.get(i);
            if (card.getRank().equals("A") && card.getValue() == 1) {
                System.out.println("A카드를 11점으로 바꾸시겠습니까? (Y/N)");
                String ayn = input.nextLine();
                if (ayn.equalsIgnoreCase("Y")) {
                    card.setValue(11);
                    break;
                }
            }
        }
    }
}
