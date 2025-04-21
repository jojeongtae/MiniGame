import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackJack {
    Deck deck;
    List<Card> playerCard;
    List<Card> dealerCard;
    Scanner input = new Scanner(System.in);
    public void start(){
        while(true) {
            System.out.println("블랙잭 게임을 시작 하겠습니다.");
            deck = new Deck();
            playerCard = new ArrayList<>();
            dealerCard = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                playerCard.add(deck.drawCard());
                dealerCard.add(deck.drawCard());
            }
            System.out.println("내 카드 : " + myHand(playerCard));
            System.out.println("딜러 카드 : " + dealerCard.get(0) + " ???");
            while (true) {
                System.out.println("카드를 더 받으시겠습니까? Y/N");
                String yn = input.nextLine();
                if (yn.toUpperCase().equals("Y")) {
                    playerCard.add(deck.drawCard());
                    System.out.println("내 카드 : " + myHand(playerCard));
                    int total = totalHand(playerCard);
                    if (total > 21) {
                        System.out.println("버스트! 당신의 패배");
                        System.out.println("딜러 카드 : " + myHand(dealerCard));
                        break;
                    }
                } else {
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
                } else if (playerTotal < dealerTotal) {
                    System.out.println("딜러의 승리!");
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
        int ace = 0;
        for (int i = 0 ; i<hand.size();i++){
            int value = hand.get(i).getValue();
            total += value;
            if (value == 1){
                ace++;
            }
        }
        while(ace > 0 && total+10 < 21){
            total +=10;
            ace--;
        }
return total;
    }
}
