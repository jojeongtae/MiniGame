import java.util.Locale;
import java.util.Scanner;

public class YachtDice {
    private User user;
    Dice[] dice = new Dice[5];
    int[] playerScore = new int[15];
    int[] computerScore = new int[15];
    Scanner input = new Scanner(System.in);
    int turn;

    public YachtDice(User user) {
        this.user = user;
    }

    public void start() {
        this.dice = new Dice[5];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new Dice(); // ✅ 각 주사위 객체 생성
        }
        introText();
        System.out.print("베팅금액을 입력해주세요 : ");
        int stake = input.nextInt();
        input.nextLine();
        if (user.getUserMoney() < stake) {
            System.out.println("돈이 없노");
            return;
        }
        turnSelect();
        while (true) {
            if (turn == 1) {
                playerTurn();
            } else {
                computerTurn();
            }

        }
    }


    public void introText() {
        String intro = """
                
                야추 다이스 게임에 오신걸 환영합니다.
                게임은 컴퓨터랑 1대1로 진행이 가능하며
                이기면 베팅금액에 2.5배로 드리고 있습니다.
                자신의 운빨을 시험해보세요!
                
                 \s""";
        System.out.println(intro);
    }

    public void scoreBoard(int[] player, int[] computer) {
        String score = String.format("""
                                       PLAYER  / COMPUTER
                        ---------------------------------
                        ACE        :    %2d    /   %2d
                        DEUCES     :    %2d    /   %2d
                        THREES     :    %2d    /   %2d
                        FOURS      :    %2d    /   %2d
                        FIVE       :    %2d    /   %2d
                        SIXEX      :    %2d    /   %2d
                        SUBTOTAL   :    %2d    /   %2d
                        ---------------------------------
                        CHOICE     :    %2d    /   %2d
                        4 OF KIND  :    %2d    /   %2d
                        FULL HOUSE :    %2d    /   %2d
                        S.STRAIGHT :    %2d    /   %2d
                        L.STRAIGHT :    %2d    /   %2d
                        YACHT      :    %2d    /   %2d
                        ---------------------------------
                        TOTAL      :    %2d    /   %2d
                        """,
                player[0], computer[0],
                player[1], computer[1],
                player[2], computer[2],
                player[3], computer[3],
                player[4], computer[4],
                player[5], computer[5],
                player[6], computer[6],
                player[7], computer[7],
                player[8], computer[8],
                player[9], computer[9],
                player[10], computer[10],
                player[11], computer[11],
                player[12], computer[12],
                player[13], computer[13],
                player[14], computer[14]
        );
    }

    public int turnSelect() {
        System.out.println("먼저 하실 유저를 뽑겠습니다.");
        System.out.println("앞/뒤 둘중 하나를 입력하시거나 또는 1/2로 입력하시면됩니다");
        String select = input.nextLine();
        int randomTurn = (int) (Math.random() * 2 + 1);
        int value = 0;
        switch (select) {
            case "앞":
            case "1":
                value = 1;
                break;
            case "뒤":
            case "2":
                value = 2;
                break;
            default:
                System.out.println("잘못 입력하셨습니다.");
                return turnSelect();

        }
        if (value == randomTurn) {
            System.out.println("플레이어 먼저 시작합니다!");
            return this.turn = 1;
        } else {
            System.out.println("컴퓨터 먼저 시작합니다.");
            return this.turn = 2;
        }

    }

    public void playerTurn() {
        int num = 0;
        int total = 0;
        while (num < 3) {
            for (Dice d : dice) {
                d.diceRoll();
            }

            System.out.println("던진 주사위 결과");
            for (int i = 0; i < dice.length; i++) {
                System.out.print((1 + i) + " :" + dice[i] + " ");
            }
            System.out.println();
            if (num == 2) {
                break;
            }
            System.out.println("주사위 고정 하실려면 1번 해제하실거면 2번을 입력해주세요");
            String holdNum =input.nextLine();
            if (holdNum.equals("1")){
                holdDice();
            } else if (holdNum.equals("2")) {
                notHoldDice();
            }
            num++;
        }
        System.out.println("주사위  최종 결과 : ");
        for (int i = 0 ;i<dice.length ;i++){
//            total += Integer.parseInt(dice[i]);
            System.out.print((1 + i) + " :" + dice[i] + " ");
            break;
        }
        System.out.println();
        System.out.println("점수판에서 점수를 넣을곳을 정해주세요");
//        scoreBoard();

    }

    public void computerTurn() {

    }
    public void holdDice(){
        System.out.println("고정 하실 주사위 위치를 입력하세요(예: 1 3) 그냥 넘어가면 Enter 눌러주세요");
        String holdDice = input.nextLine().trim();
        if (!holdDice.isEmpty()) {
            String[] select = holdDice.split(" ");
            for (String s : select) {
                int index = Integer.parseInt(s) - 1;
                if (index < 5 && index >= 0) {
                    dice[index].hold();
                }
            }
        }
    }
    public void notHoldDice(){
        System.out.println("고정을 해제할 주사위 위치를 입력하세요(예: 1 3) 그냥 넘어가면 Enter 눌러주세요");
        String holdDice = input.nextLine().trim();
        if (!holdDice.isEmpty()) {
            String[] select = holdDice.split(" ");
            for (String s : select) {
                int index = Integer.parseInt(s) - 1;
                if (index < 5 && index >= 0) {
                    dice[index].notHold();
                }
            }
        }
    }
}
