import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackJack {
    Deck deck;
    User user;
    Card card;
    List<Card> playerCard;
    List<Card> dealerCard;
    Scanner input = new Scanner(System.in);
    private List<Card> savedCards = new ArrayList<>();

    public BlackJack(User user) {
        this.user = user;
    }

    public void start() {
        while (true) {
            BetAmount bet = new BetAmount(user, deck);
            int count = 0;
            System.out.println("블랙잭 게임을 시작 하겠습니다.");
            System.out.print("판돈을 입력해주세요 : ");
            int stake = 0;
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
                        playerCard.add(deck.drawCard());
                        System.out.println("내 카드 : " + myHand(playerCard));
                        checkAce(playerCard);
                        int total = totalHand(playerCard);
                        if (total > 21) {
                            break;
                        }
                    } else if (yn.toUpperCase().equals("D") || yn.equals("2")) {
                        if (stake > user.getUserMoney()) {
                            System.out.println("판돈이 모자랍니다.");
                            break;
                        } else {
                            playerCard.add(deck.drawCard());
                            System.out.println("내 카드 : " + myHand(playerCard));
                            user.setUserMoney(user.getUserMoney() - stake);
                            checkAce(playerCard);
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
                        Card selectedCard = playerCard.remove(selectCard);
                        splitCard(selectedCard);
                        playerCard.add(deck.drawCard());
                        System.out.println("내 카드 : " + myHand(playerCard));
                        checkAce(playerCard);
                        count++;
                    } else {
                        break;
                    }
                }
                System.out.println("딜러 차례...");
                while (totalHand(dealerCard) < 17) {
                    dealerCard.add(deck.drawCard());
                    dealerAce(dealerCard);
                }

                int playerTotal = totalHand(playerCard);
                int dealerTotal = totalHand(dealerCard);

                if (playerTotal <= 21) {
                    System.out.println();
                    System.out.println("내 패 : " + myHand(playerCard) + " 내 점수 : " + playerTotal);
                    System.out.println("딜러 패 : " + myHand(dealerCard) + " 딜러 점수: " + dealerTotal);

                    if (dealerTotal > 21 || playerTotal > dealerTotal) {
                        System.out.println("당신의 승리!");
                        if (count == 2) {
                            bet.doubleDown(stake);
                        } else if (count == 1) {
                            bet.split(stake);
                        }
                        if (playerCard.size() == 2 && playerTotal == 21) {
                            System.out.println("블랙잭!");
                            bet.blackjack(stake);
                        }if (count == 0){
                            user.addUserMoney(stake*2);
                        }
                        System.out.println("남은 금액 : " + user.getUserMoney());
                    } else if (playerTotal < dealerTotal) {
                        System.out.println("딜러의 승리!");
                        System.out.println("남은 금액 : " + user.getUserMoney());
                    } else {
                        System.out.println("무승부!");
                        user.addUserMoney(stake);
                        System.out.println("남은 금액 : " + user.getUserMoney());

                    }
                } else {
                    System.out.println("버스트! 당신의 패배");
                    System.out.println("내 패 : " + myHand(playerCard) + " 내 점수 : " + playerTotal);
                    System.out.println("딜러 패 : " + myHand(dealerCard) + " 딜러 점수: " + dealerTotal);
                    System.out.println("남은 금액 : " + user.getUserMoney());
                }
                if (user.getUserMoney() < 100) {
                    System.out.println(" 어이 " + user.getName() + " 돈없으면 나가쇼");
                    break;
                }
                System.out.println("다시 하시려면 Y");
                System.out.println("그만하시면 아무키나 눌러주세요");
                String restart = input.nextLine();
                user.setUserMoney(user.getUserMoney());
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
        int total = 0;
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.get(i).getValue();
            total += value;

        }

        return total;
    }

    private void checkAce(List<Card> hand) { //Ace 1 or 11
        for (int i = 0; i < hand.size(); i++) {
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
        for (int i = 0; i < hand.size(); i++) {
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
        savedCards.add(card);
        System.out.println("넘김 받은 카드 : " + card);
    }
}
