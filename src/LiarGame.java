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
    int winningStack = 0;
    int gamePlayed = 0;
    boolean isLie = false;
    Card FieldCard;
    String mainRank;
    Scanner input = new Scanner(System.in);
    Random random = new Random();

    public LiarGame(User user) {
        this.user = user;
    }

    void start() {
            if(this.user.getUserMoney()==0){
                System.out.println("돈이없어 참가가 불가능합니다");
                return;
            }



            showInfo();
        do {
            BettingStart();

            setGameDeck();

            setPlayers();

            DealHand(player1, player2, this.user);

            RollWhoWillBeFirst();


            startPlaying();


        } while (!gameEndResetAll());


    }




    void showInfo(){
        SP.s("""
                라이어 게임을 시작하겠습니다
                
                😍규칙😍
                
                A, K, Q 중 랜덤으로 랭크 하나가 제시됩니다
                
                승리조건은 자신의 모든 패를 제출. 혹은 상대방의 거짓말을 간파
                
                같거나 다른 랭크의 카드를 3장까지 동시에 낼수 있습니다.
                제시랭크와 다른 랭크의 카드도 제출 가능하나 상대방이 라이어를 외친다면 패배!
                
                조커의 경우, 제출할 때 제시 랭크와 같은 랭크로 취급됩니다
                
                순서는 매 판 랜덤으로 결정됩니다
                
                라이어 지목은 자신의 다음 사람을 대상으로만 지목할 수 있습니다.
                
                
                """,500);
        SP.s("주의. 올인은 패망으로 가는 지름길 입니다 👿",1000);
    }
    void BettingStart() {
        while (true) {
            SP.s("현재 보유 금액: ("+this.user.getUserMoney()+") 판돈을 입력해주세요:",500);
            int userBet=0;
            try {
                userBet = input.nextInt();
            }catch (Exception e){
                System.out.println("숫자를 입력하세요");
                input.nextLine();
                continue;
            }
            if (userBet > user.getUserMoney() || userBet ==0) {
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

        if(this.totalStack>50000 || user.getUserMoney() == 0 || this.winningStack>2){
            this.player1.setStrategy(LiarsStrategy.CHEATER);
            this.player2.setStrategy(LiarsStrategy.CHEATER);
        }
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
        String [] rank = {"A","Q","K"};
        this.mainRank = rank[random.nextInt(3)];
        player1.mainRank = mainRank;
        player2.mainRank = mainRank;
        user.mainRank = mainRank;
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
        SP.s("이번 라운드 제시 랭크: ["+this.mainRank+"] 입니다!",800);

    }

    void startPlaying() {
        boolean stop = false;
        do {

            ArrayList<Card> firstTurn = this.first.selectAct(this.gameDeck);
            this.LastPlayerCard = firstTurn;
            chooseForStrike(this.third, this.first);
            stop = check();
            if(stop)
                break;
            if (this.first.getHand().isEmpty()) {
                SP.s(this.first.getName() + "의 손패가 0장입니다! 승리!",300);
                this.winner = this.first;
                this.loser = this.third;
                check();
                break;
            }


            ArrayList<Card> secondTurn = this.second.selectAct(this.gameDeck);
            this.LastPlayerCard = secondTurn;
            chooseForStrike(this.first, this.second);
            stop = check();
            if(stop)
                break;
            if (this.second.getHand().isEmpty()) {
                SP.s(this.second.getName() + "의 손패가 0장입니다! 승리!",300);
                this.winner = this.second;
                this.loser = this.first;
                check();
                break;
            }

            ArrayList<Card> thirdTurn = this.third.selectAct(this.gameDeck);
            this.LastPlayerCard = thirdTurn;
            chooseForStrike(this.second, this.third);
            stop = check();
            if(stop)
                break;
            if (this.third.getHand().isEmpty()) {
                SP.s(this.third.getName() + "의 손패가 0장입니다! 승리!",300);
                this.winner = this.third;
                this.loser = this.second;
                check();
                break;
            }
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
            for (int i = 0 ; i < LastPlayerCard.size();i++) {
                LastPlayerCard.get(i).toCardString();
            }
            System.out.println();
        }
    }

    void chooseForStrike(Liars LastPlayer, Liars nowPlayer) {
        if (LastPlayer == this.user) {
            SP.s("결정하세요 1.넘김 2.라이어!",300);
            int choose =0;
            while (true) {
                try {
                    choose = input.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("숫자를 입력해주세요");
                    input.nextLine();
                }
            }
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
            switch (LastPlayer.getStrategy()){
                case RANDOM :
                    int choose = random.nextInt(100) + 1;
                    if(nowPlayer.getHand().isEmpty())
                        choose -=90;

                    if (choose < 51) {
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
                    break;
                case DEFENSIVE:
                   choose = random.nextInt(100) + 1;
                    if(nowPlayer.getHand().isEmpty())
                        choose -=90;

                    if (choose < 15) {
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
                    break;
                case AGGRESSIVE:
                    choose = random.nextInt(100) + 1;
                    if(this.LastPlayerCard.size()==3 && LastPlayer.getHand().stream().anyMatch(e->e.getRank().equals(this.mainRank)))
                        choose -=90;
                    if(nowPlayer.getHand().isEmpty())
                        choose -=90;

                    if (choose < 20) {
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
                    break;
                case CHEATER:
                    if(this.LastPlayerCard.stream().
                            noneMatch(e->e.getRank().equals(this.mainRank) || e.getRank().equals("Joker"))){
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
                    }else SP.s(LastPlayer.getName()+"(은)는 라이어선언을 참았습니다",800);
                    break;
            }

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
    boolean gameEndResetAll(){
        if(this.winner==this.user){
            this.winningStack++;
            int userGetBet = this.stake*3;
            System.out.println("게임 승리로 "+userGetBet+"원을 획득했습니다!");
            this.user.getBet(this.user.getUserMoney()+userGetBet);
            this.totalStack+=userGetBet;
            System.out.println("현재 보유 금액: "+this.user.getUserMoney()+"원 ");
        }else {
            this.winningStack=0;
            System.out.println("현재 보유 금액: " + this.user.getUserMoney() + "원 ");
        }
        this.stake=0;
        this.user.getHand().clear();
        this.winner=null;
        this.Striker=null;
        this.loser = null;
        this.LastPlayerCard=null;
        this.gamePlayed++;
        if(user.getUserMoney()==0) {
            System.out.println("돈이없네요 나가쇼💩");
            System.exit(0);
        }
        System.out.println();
        System.out.println("한판 더? y/n");
        input.nextLine();
        String YorN = input.nextLine();
        if (YorN.equalsIgnoreCase("n")) {
            sayGoodBye();
            return true;
        } else {
            SP.s("새로운 게임을 시작합니다!", 300);
            return false;
        }

    }
    void sayGoodBye(){
        System.out.println("다음에봐용🤣");
        System.out.println("승: "+this.winStack+" "+" 총 수입: "+this.totalStack);
        System.out.println("승률: "+(double)this.winStack/this.gamePlayed*100+"%");
        this.winStack=0;
        this.totalStack=0;
    }
}