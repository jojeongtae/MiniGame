import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackJack {
    private Deck deck;
    private User user;
    private List<Card> playerCard;
    private List<Card> dealerCard;
    private Scanner input = new Scanner(System.in);
    private List<Card> savedCards = new ArrayList<>();

    public BlackJack(User user) {
        this.user = user;
    }

    public void start() {
        while (true) {
            BetAmount bet = new BetAmount(user);
            int count = 0;
            System.out.println("블랙잭 게임을 시작 하겠습니다.");
            System.out.print("판돈을 입력해주세요 : ");
            int stake = 0; //판 돈
            stake = input.nextInt();
            input.nextLine();
            if (stake > user.getUserMoney()) {
                System.out.println("판돈이 모자랍니다.");
                continue;
            } else {
                user.addUserMoney(-stake);
                deck = new Deck();
                playerCard = new ArrayList<>();
                dealerCard = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    if (!savedCards.isEmpty()) {
                        playerCard.add(savedCards.remove(0)); // 제일 앞 카드 꺼내고 제거
                    } else {
                        playerCard.add(deck.drawCard()); // 부족하면 새로 뽑기
                    }

                    dealerCard.add(deck.drawCard()); // 딜러는 무조건 새로 뽑음
                }
                System.out.println("내 카드 : " + myHand(playerCard));
                System.out.println("딜러 카드 : " + dealerCard.get(0) + " ???");
                checkAce(playerCard);
                while (true) {
                    System.out.println("1.힛(H) 2.더블 다운(D) 3.스플릿(S) 4.스탠드(Enter)");
                    String yn = input.nextLine();
                    if (yn.toUpperCase().equals("H") || yn.equals("1")) {
                        playerCard.add(deck.drawCard());//카드 한장 드로우
                        System.out.println("내 카드 : " + myHand(playerCard));
                        checkAce(playerCard); //패에 A카드 체크
                        int total = totalHand(playerCard);
                        if (total > 21) { //점수 기준으로 21점 넘으면 와일문 탈출
                            break;
                        }
                    } else if (yn.toUpperCase().equals("D") || yn.equals("2")) {
                        if (stake > user.getUserMoney()) {
                            System.out.println("판돈이 모자랍니다.");
                            break;
                        } else { // 더블다운
                            playerCard.add(deck.drawCard()); //카드 한장 드로우
                            System.out.println("내 카드 : " + myHand(playerCard));
                            user.addUserMoney(-stake);
                            checkAce(playerCard); //카드 보여주고 판돈을 2배더 늘려 총 4배 그리고 에이스체크
                            count = 2;
                            break;
                        }
                    } else if (yn.toUpperCase().equals("S") || yn.equals("3")) {
                        System.out.println("다음 판으로 넘길 카드를 골라 번호를 입력하세요");
                        int selectCard = input.nextInt()-1;
                        input.nextLine();
                        if (selectCard < 0 || selectCard >= playerCard.size()) {
                            System.out.println("없는 카드입니다.");
                            break;
                        }
                        Card selectedCard = playerCard.remove(selectCard); //플레이어 카드 한장을 선택한 번호를 눌러서
                        splitCard(selectedCard); //삭제후 그걸 selectedCard 여기에 보관했다가 넘겨줌
                        playerCard.add(deck.drawCard()); //그리고 카드 한장 뽑고
                        System.out.println("내 카드 : " + myHand(playerCard));
                        checkAce(playerCard); //출력후 에이스 체크 여기서 보관하는 카드는 다음판에 적용
                        count++;
                    } else {
                        break;
                    }
                }
                System.out.println("딜러 차례...");
                while (totalHand(dealerCard) < 17) {
                    dealerCard.add(deck.drawCard()); //딜러는 카드 숫자가 17이 넘어가면 뽑지않음
                    dealerAce(dealerCard);
                }

                int playerTotal = totalHand(playerCard); //총점 체크
                int dealerTotal = totalHand(dealerCard); //총점 체크

                if (playerTotal <= 21) { //내패가 버스트(21점 넘지않았을떄)
                    System.out.println();
                    System.out.println("내 패 : " + myHand(playerCard) + " 내 점수 : " + playerTotal);
                    System.out.println("딜러 패 : " + myHand(dealerCard) + " 딜러 점수: " + dealerTotal);

                    if (dealerTotal > 21 || playerTotal > dealerTotal) {
                        System.out.println("당신의 승리!");
                        if (count == 2) { //위에 더블다운은 카운트2로 해놨는데 판돈 계산때매 체크
                            bet.doubleDown(stake);
                        } else if (count == 1) { //스플릿하면 그냥 판돈 2배 기존보상이랑 동일
                            bet.split(stake);
                        }
                        if (playerCard.size() == 2 && playerTotal == 21) {
                            System.out.println("블랙잭!"); //블랙잭 A카드 한장과 10 K Q J 가 있을시 판돈 3배 적용
                            bet.blackjack(stake);
                        }if (count == 0){ //기본 보상
                            user.addUserMoney(stake*2);
                        }
                        System.out.println("남은 금액 : " + user.getUserMoney());
                    } else if (playerTotal < dealerTotal) {
                        System.out.println("딜러의 승리!"); //딜러 승리시 판돈을 내 돈에서 차감 이건 위에서 차감했기에 없음 그런거
                        System.out.println("남은 금액 : " + user.getUserMoney());
                    } else {
                        System.out.println("무승부!");
                        user.addUserMoney(stake); //무승부 시 판돈 돌려줌
                        System.out.println("남은 금액 : " + user.getUserMoney());

                    }
                } else {
                    System.out.println("버스트! 당신의 패배");
                    System.out.println("내 패 : " + myHand(playerCard) + " 내 점수 : " + playerTotal);
                    System.out.println("딜러 패 : " + myHand(dealerCard) + " 딜러 점수: " + dealerTotal);
                    System.out.println("남은 금액 : " + user.getUserMoney());
                }
                if (user.getUserMoney() < 100) { //돈 100원도 없으면 쫒겨남
                    System.out.println(" 어이 " + user.getName() + " 돈없으면 나가쇼");
                    break;
                }
                System.out.println("다시 하시려면 Y");
                System.out.println("그만하시면 아무키나 눌러주세요");
                String restart = input.nextLine();
                if (!restart.toUpperCase().equals("Y")) {
                    break;
                }
            }
        }
    }

    private String myHand(List<Card> hand) {
        String result = "";
        for (int i = 0; i < hand.size(); i++) { //내 패의 길이만큼반복
            result += hand.get(i) + " "; // 카드 한 장씩 꺼내서 문자열로 붙이기
        }
        return result;
    }


    private int totalHand(List<Card> hand) {
        int total = 0; //점수 계산때매 따로 함수로 빼놓음
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.get(i).getValue();
            total += value;

        }

        return total;
    }

    private void checkAce(List<Card> hand) { //Ace 1 or 11
        for (int i = 0; i < hand.size(); i++) { //Ace 체크하는 과정
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

    private void dealerAce(List<Card> hand) {
        for (int i = 0; i < hand.size(); i++) { //딜러 Ace체크하는 과정
            Card card = hand.get(i);
            if (card.getRank().equals("A") && card.getValue() == 1) {
                if (totalHand(dealerCard) < 11) {
                    card.setValue(11);
                    break;
                }
            }
        }
    }

    private void splitCard(Card card) {
        savedCards.add(card); //위에 스플릿한 카드를 여기로 넘겨받음
        System.out.println("넘김 받은 카드 : " + card);
    }
}
