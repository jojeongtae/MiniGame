import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LiarGame {
    ArrayList<Card> gameDeck;
    ArrayList<Card> LastPlayerCard;
    User user;
    LiarsPlayer player1;
    LiarsPlayer player2;
    Liars first;
    Liars second;
    Liars third;
    Liars Striker = null;
    Liars winner = null;
    Liars loser = null;
    int stake;
    int totalStack=0;
    int winStack = 0;
    boolean isLie = false;
    Card FieldCard;
    Scanner input = new Scanner(System.in);
    Random random = new Random();

    public LiarGame(User user) {
        this.user = user;
    }

    void start() {



            showInfo();
        do {
            BettingStart();

            setGameDeck();

            setPlayers();

            DealHand(player1, player2, this.user);

            RollWhoWillBeFirst();

            startPlaying();

            gameEndResetAll();

            String YorN = input.nextLine();

            if(YorN.equalsIgnoreCase("n")){
                sayGoodBye();
                break;
            }

        }while(true);


    }









    void showInfo(){
        SP.s("""
                라이어 게임을 시작하겠습니다
                
                😍규칙😍
                
                승리조건은 자신의 모든 패를 제출. 혹은 상대방의 거짓말을 간파
                
                자신의 턴에 카드를 낼지, 1장 드로우를 할지 선택할 수 있습니다.
                
                같은 랭크의 카드를 3장까지 동시에 낼수 있습니다.
                혹은 서로 다른 랭크의 카드도 제출 가능하나 상대방이 라이어를 외친다면 패배!
                
                조커의 경우, 제출할 때 어떤 랭크든 같은 랭크로 취급됩니다
                
                순서는 매 판 랜덤으로 결정됩니다
                
                라이어 지목은 자신의 다음 사람을 대상으로만 지목할 수 있습니다.
                
                
                """,500);
    }
    void BettingStart() {
        while (true) {
            SP.s("판돈을 입력해주세요:",500);
            int userBet = input.nextInt();
            if (userBet > user.getUserMoney()) {
                System.out.println("판돈이 모자랍니다.");
            } else {
                SP.s(userBet + "원을 배팅합니다. 승리 시 " + userBet * 3 + "원 획득!",500);
                this.user.LoseBet(userBet);
                this.stake = userBet;
                this.totalStack-=userBet;
                break;
            }
        }
    }

    void setGameDeck() {
        Deck LiarsDeck = new Deck();
        this.gameDeck = LiarsDeck.LiarsDecK();
    }

    void setPlayers() {
        LiarsPlayer player1 = new LiarsPlayer();
        LiarsPlayer player2 = new LiarsPlayer();
        while (true) {
            if (player2.name.equals(player1.name))
                player2 = new LiarsPlayer();
            else break;
        }
        this.player1 = player1;
        this.player2 = player2;
        SP.s(player1.name + "(이)가 게임에 참가했습니다.",300);
       SP.s(player2.name + "(이)가 게임에 참가했습니다.",300);
    }

    void DealHand(LiarsPlayer player1, LiarsPlayer player2, User user) {
        for (int i = 0; i < 5; i++) {
            player1.PlayerDeck.add(this.gameDeck.remove(0));
            player2.PlayerDeck.add(this.gameDeck.remove(0));
            user.PlayerDeck.add(this.gameDeck.remove(0));
        }
        this.FieldCard = gameDeck.remove(0);
        player1.FieldCard = this.FieldCard;
        player2.FieldCard = this.FieldCard;
    }

    void RollWhoWillBeFirst() {
        int roll = random.nextInt(3);
        switch (roll) {
            case 0:
                this.first = this.player1;
                this.second = this.player2;
                this.third = this.user;
                break;
            case 1:
                this.first = this.player2;
                this.second = this.user;
                this.third = this.player1;
                break;
            case 2:
                this.first = this.user;
                this.second = this.player1;
                this.third = this.player2;
        }
        SP.s("순서: " + this.first.getName() + " 👉 " + this.second.getName() + " 👉 " + this.third.getName(),300);
        SP.s(this.first.getName() + " 부터 게임을 시작합니다!",600);
        System.out.println("필드 카드");
        FieldCard.toCardString();
        System.out.println();
    }

    void startPlaying() {
        boolean stop = false;
        do {

            ArrayList<Card> firstTurn = this.first.selectAct(this.gameDeck);
            this.LastPlayerCard = firstTurn;
            chooseForStrike(this.third, this.first);
            if (this.first.getHand().isEmpty()) {
                SP.s(this.first.getName() + "의 손패가 0장입니다! 승리!",300);
                this.winner = this.first;
                this.loser = this.third;
                break;
            }
            stop = check();
            if(stop)
                break;

            ArrayList<Card> secondTurn = this.second.selectAct(this.gameDeck);
            this.LastPlayerCard = secondTurn;
            chooseForStrike(this.first, this.second);
            if (this.second.getHand().isEmpty()) {
                SP.s(this.second.getName() + "의 손패가 0장입니다! 승리!",300);
                this.winner = this.second;
                this.loser = this.first;
                break;
            }
            stop = check();
            if(stop)
                break;
            ArrayList<Card> thirdTurn = this.third.selectAct(this.gameDeck);
            this.LastPlayerCard = thirdTurn;
            chooseForStrike(this.second, this.third);
            if (this.third.getHand().isEmpty()) {
                SP.s(this.third.getName() + "의 손패가 0장입니다! 승리!",300);
                this.winner = this.third;
                this.loser = this.second;
                break;
            }
            stop = check();
            if(stop)
                break;
        } while (true);
    }

    void ShowAllCardInHand() {
        player1.showDeck();
        player2.showDeck();
        user.showDeck();
        System.out.println("필드 카드");
        FieldCard.toCardString();
        System.out.println();
        System.out.println("남은 패");
        for (Card c : gameDeck)
            c.toCardString();
        System.out.println();
    }

    void showLastPlayerCard() {
        if (LastPlayerCard == null)
            System.out.println("패 낸거 없음");
        else {
            for (int i = 0 ; i < LastPlayerCard.size()-1;i++) {
                LastPlayerCard.get(i).toCardString();
            }
            System.out.println();
        }
    }

    void chooseForStrike(Liars LastPlayer, Liars nowPlayer) {
        if (LastPlayerCard == null || LastPlayerCard.isEmpty()) {
            SP.s("이전 플레이어가 카드를 내지 않았습니다. 라이어 호출 불가.",300);
            return;
        }
        if (LastPlayer == this.user) {
            SP.s("결정하세요 1.넘김 2.라이어!",300);
            int choose = input.nextInt();
            if (choose == 2) {
                this.Striker = LastPlayer;
                this.isLie = LastPlayer.StrikeLiar(this.LastPlayerCard);
                showLastPlayerCard();
                if (isLie) {
                    SP.s(nowPlayer.getName() + "(은)는 라이어 였습니다!",300);
                    this.winner = LastPlayer;
                    this.loser = nowPlayer;
                } else {
                    SP.s(nowPlayer.getName() + "(은)는 라이어 가 아니었습니다!",300);
                    this.loser = LastPlayer;
                    this.winner = nowPlayer;
                }
            }
        } else {
            SP.s(LastPlayer.getName() + "(이)가 고민중입니다...",1000);
            int choose = random.nextInt(100) + 1;
            if(this.user.PlayerDeck.isEmpty() && LastPlayerCard.size()>1)
                choose-=50;

            if (choose < 50) {
                SP.s(LastPlayer.getName()+"(이)가 "+nowPlayer.getName() + "에게 라이어 선언! 너 그짓말이지",1200);
                this.Striker = LastPlayer;
                this.isLie = LastPlayer.StrikeLiar(this.LastPlayerCard);
                showLastPlayerCard();
                if (isLie) {
                    SP.s(nowPlayer.getName()+"(은)는 라이어 였습니다!",300);
                    this.winner = LastPlayer;
                    this.loser = nowPlayer;
                } else {
                    SP.s(nowPlayer.getName()+"(은)는 라이어가 아니였습니다!",300);
                    this.loser = LastPlayer;
                    this.winner = nowPlayer;
                }
            } else SP.s(LastPlayer.getName()+"(은)는 라이어선언을 참았습니다",800);
        }
    }

    boolean check() {
        if (this.winner != null && this.loser != null) {
            SP.s("게임 종료! 승자: " + winner.getName() + ", 패자: " + loser.getName(),300);
            if(this.winner == this.user)
                this.winStack++;
            return true;
        }
        return false;
    }
    void gameEndResetAll(){
        if(this.winner==this.user){
            int userGetBet = this.stake*3;
            System.out.println("게임 승리로 "+userGetBet+"원을 획득했습니다!");
            this.user.getBet(this.user.getUserMoney()+userGetBet);
            this.totalStack+=userGetBet;
            System.out.println("현재 보유 금액: "+this.user.getUserMoney()+"원 ");
        }else
            System.out.println("현재 보유 금액: "+this.user.getUserMoney()+"원 ");
        this.stake=0;
        this.user.getHand().clear();
        this.winner=null;
        this.Striker=null;
        this.loser = null;
        this.LastPlayerCard=null;

        System.out.println("한판 더? y/n");
        input.nextLine();
    }
    void sayGoodBye(){
        System.out.println("다음에봐용🤣");
        System.out.println("승: "+this.winStack+" "+" 총 수입: "+this.totalStack);
        this.winStack=0;
        this.totalStack=0;
    }
}