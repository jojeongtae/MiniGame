import java.util.Locale;
import java.util.Scanner;

public class YachtDice {
    private User user;
    Dice[] dice = new Dice[5];
    DicePlayer player;
    DicePlayer computer;
    Scanner input = new Scanner(System.in);
    int turn;

    public YachtDice(User user) {
        this.user = user;
    }

    public void start() {
        int count = 0;
        player = new DicePlayer(user.getName(), false);
        computer = new DicePlayer("컴퓨터", true);
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
        while (count < 13) {
            if (turn == 1) {
                playerTurn();
                continue;

            } else if (turn == 2) {
                computerTurn();

            }
            count++;
        }
    }


    public void introText() {
        String intro = """
                
                야추 다이스 게임에 오신걸 환영합니다.
                게임은 컴퓨터랑 1대1로 진행이 가능하며
                이기면 베팅금액에 2.5배로 드리고 있습니다.
                자신의 운빨을 시험해보세요!
                주사위를 굴려 족보에 일치하는곳에 입력하면됩니다!
                
                 \s""";
        System.out.println(intro);
    }

    public String scoreBoard(DicePlayer player, DicePlayer computer) {
        int[] p = player.getScores();
        int[] c = computer.getScores();
        String score = String.format("""
                                      PLAYER  / COMPUTER
                        ---------------------------------
                        ACE        :    %2d    /   %2d
                        DEUCES     :    %2d    /   %2d
                        THREES     :    %2d    /   %2d
                        FOURS      :    %2d    /   %2d
                        FIVE       :    %2d    /   %2d
                        SIXES      :    %2d    /   %2d
                        SUBTOTAL   :  %2d/64   / %2d/64
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
                p[0], c[0], // ACE
                p[1], c[1], // DEUCES
                p[2], c[2], // THREES
                p[3], c[3], // FOURS
                p[4], c[4], // FIVE
                p[5], c[5], // SIXES
                p[12], c[12], // SUBTOTAL         index  sum(0 ~5)  sum >63  =+ 35
                p[6], c[6], // CHOICE
                p[7], c[7], // 4 OF KIND
                p[8], c[8], // FULL HOUSE
                p[9], c[9], // S.STRAIGHT         score 15
                p[10], c[10], // L.STRAIGHT       score 30
                p[11], c[11], // YACHT            score 50
                p[13], c[13] // TOTAL             sum (index 0~ 11 )
        );
        return score;
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
        for (int i = 0; i < 5; i++) {
            dice[i].notHold();
        }
        while (num < 3) {
            for (Dice d : dice) {
                d.diceRoll();
            }
            System.out.println("던진 주사위 결과");
            for (int i = 0; i < dice.length; i++) {
                System.out.print((1 + i) + "번째 주사위 : " + dice[i] + " || ");
            }
            System.out.println();
            if (num == 2) {
                break;
            }
            System.out.println("주사위 고정 하실려면 1번 해제하실거면 2번을 입력해주세요");
            String holdNum = input.nextLine();
            if (holdNum.equals("1")) {
                holdDice();
            } else if (holdNum.equals("2")) {
                notHoldDice();
            }
            num++;
        }
        if (num == 2) {
            System.out.println("주사위 최종 결과를 알려 드립니다 ");
            for (int i = 0; i < 5; i++) {
                total += dice[i].getValue();
                System.out.print((1 + i) + "번째 주사위 : " + dice[i] + " || ");
            }
            System.out.println();
            scoreBoardSelect(player);
        }

    }


    public void computerTurn() {
        DicePlayer com = this.computer;
        System.out.println(com.getName() + "님의 턴이 시작 하였습니다.");
        for (int i = 0; i < 5; i++) {
            dice[i].notHold();
        }
        for (int j = 0; j < 3; j++) {
            for (Dice d : dice) {
                d.diceRoll();
            }
            if (j == 0) {
                holdHighNumber();
            }
        }
        computerHighNumber();
        updateSubtotalAndTotal(computer);
        for (Dice d : dice) {
            System.out.print(computer.getName() + "님의 주사위 : " + d.getValue() +" || ");
        }
        System.out.println();
        System.out.println(scoreBoard(player, computer));
        this.turn = 1;
    }

    public void computerHighNumber() {
        if (!computer.isUsed(11) && isYacht()) {
            computer.setScore(11, 50);
            System.out.println(computer.getName() + "님 야추! 50점이 기록되었습니다.");
        } else if (!computer.isUsed(10) && isLargeStraight()) {
            computer.setScore(10, 30);
            System.out.println(computer.getName() + "님 LS 30점이 기록되었습니다.");
        } else if (!computer.isUsed(9) && isSmallStraight()) {
            computer.setScore(9, 15);
            System.out.println(computer.getName() + "님 SS 15점이 기록되었습니다.");
        } else if (!computer.isUsed(8) && fullHouseScore() > 0) {
            computer.setScore(8, fullHouseScore());
            System.out.println(computer.getName() + "님 FullHouse 점이 기록되었습니다.");
        } else if (!computer.isUsed(7) && sumIfOfAKind(4) > 0) {
            computer.setScore(7, sumIfOfAKind(4));
            System.out.println(computer.getName() + "님 4 Of Kind 점이 기록되었습니다.");
        } else {
            int max = 0, maxIndex = -1;
            for (int i = 0; i <= 5; i++) {
                if (!computer.isUsed(i)) {
                    int temp = getSumOf(i + 1);
                    if (temp > max) {
                        max = temp;
                        maxIndex = i;
                    }
                }
            }
            if (maxIndex != -1) {
                computer.setScore(maxIndex, max);
                System.out.println(computer.getName() + "님 " + (maxIndex + 1) + " 숫자에 " + max + "점 기록.");
            } else {
                System.out.println("모든 숫자 칸이 이미 사용되었습니다.");
            }

        }
    }

    public int getSumOf(int num) {
        int sum = 0;
        for (Dice d : dice) {
            if (d.getValue() == num) {
                sum += num;
            }
        }
        return sum;
    }


    public void holdDice() {
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

    public void notHoldDice() {
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

    public void scoreBoardSelect(DicePlayer player) {
        System.out.println("점수판에서 점수를 넣을곳을 앞에 두글자나 풀네임 써주세요 예시(ACE = AC / L.STRAIGHT = L.)");
        String select = input.nextLine().toUpperCase();

        //점수판 입력
        switch (select) {
            case "AC":
            case "ACE": {
                int score = 0;
                for (Dice d : dice) if (d.getValue() == 1) score += 1;
                player.setScore(0, score);
                break;
            }
            case "DE":
            case "DEUCES": {
                int score = 0;
                for (Dice d : dice) if (d.getValue() == 2) score += 2;
                player.setScore(1, score);
                break;
            }
            case "TH":
            case "THREES": {
                int score = 0;
                for (Dice d : dice) if (d.getValue() == 3) score += 3;
                player.setScore(2, score);
                break;
            }
            case "FO":
            case "FOURS": {
                int score = 0;
                for (Dice d : dice) if (d.getValue() == 4) score += 4;
                player.setScore(3, score);
                break;
            }

            case "FI":
            case "FIVES": {
                int score = 0;
                for (Dice d : dice) if (d.getValue() == 5) score += 5;
                player.setScore(4, score);
                break;
            }

            case "SI":
            case "SIXES": {
                int score = 0;
                for (Dice d : dice) if (d.getValue() == 6) score += 6;
                player.setScore(5, score);
                break;
            }

            case "CH":
            case "CHOICE": {
                int score = 0;
                for (Dice d : dice) score += d.getValue();
                player.setScore(6, score);
                break;
            }

            case "FOUR":
            case "4":
            case "4OFKIND": {
                int score = 0;
                score = sumIfOfAKind(4);
                player.setScore(7, score);
                break;
            }

            case "FU":
            case "FULL":
            case "FULLHOUSE": {
                int score = 0;
                score = fullHouseScore();
                player.setScore(8, score); // index 8: FULL HOUSE
                break;
            }

            case "SS":
            case "SMALL":
            case "S.STRAIGHT": {
                if (isSmallStraight()) {
                    player.setScore(9, 15);
                }
                break;
            }

            case "LS":
            case "LARGE":
            case "L.STRAIGHT":
                if (isLargeStraight()) {
                    player.setScore(10, 30);
                }
                ;
                break;

            case "YA":
            case "YACHT":
                if (isYacht()) {
                    player.setScore(11, 50);  // index 11 = YACHT
                    System.out.println("🎉 야추! 50점이 기록되었습니다.");
                }
                break;

            default:
                System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
                scoreBoardSelect(player);
                return;
        }

        // subtotal, total 계산 자동 적용
        updateSubtotalAndTotal(player);

        // 점수판 출력
        System.out.println(scoreBoard(player, computer));
        this.turn = 2;
    }

    public int sumIfOfAKind(int count) {
        int[] diceCount = new int[7];
        for (Dice d : dice) {
            diceCount[d.getValue()]++;
        }
        for (int i = 1; i <= 6; i++) {
            if (diceCount[i] >= count) {
                int sum = 0;
                for (Dice d : dice) {
                    sum += d.getValue();
                }
                return sum;
            }
        }
        return 0;
    }

    public int fullHouseScore() {
        int[] diceCount = new int[7];
        for (Dice d : dice) {
            diceCount[d.getValue()]++;
        }
        boolean three = false;
        boolean two = false;
        for (int i = 1; i <= 6; i++) {
            if (diceCount[i] == 3) {
                three = true;
            }
            if (diceCount[i] == 2) {
                two = true;
            }
        }
        if (three && two) {
            int sum = 0;
            for (Dice d : dice) {
                sum += d.getValue();
            }
            return sum;
        }
        return 0;
    }

    public boolean isSmallStraight() {
        boolean[] value = new boolean[7];
        for (Dice d : dice) {
            value[d.getValue()] = true;
        }
        return (value[1] && value[2] && value[3] && value[4] || value[2] && value[3] && value[4] && value[5] || value[3] && value[4] && value[5] && value[6]);
    }

    public boolean isLargeStraight() {
        boolean[] value = new boolean[7];
        for (Dice d : dice) {
            value[d.getValue()] = true;
        }
        return (value[1] && value[2] && value[3] && value[4] && value[5] || value[2] && value[3] && value[4] && value[5] && value[6]);
    }

    public boolean isYacht() {
        int value = dice[0].getValue();
        for (Dice d : dice) {
            if (d.getValue() != value) {
                return false;
            }
        }
        return true;
    }

    public void updateSubtotalAndTotal(DicePlayer player) {
        int subTotal = player.getSubtotal();
        player.setScore(12, subTotal);
        int total = player.getTotalScore();
        player.setScore(13, total);
    }

    public void holdHighNumber() {
        int[] counts = new int[7]; // 주사위 값 1~6 카운트

        for (Dice d : dice) {
            counts[d.getValue()]++;
        }

        int mostFreq = 1;
        for (int i = 2; i <= 6; i++) {
            if (counts[i] > counts[mostFreq]) {
                mostFreq = i;
            }
        }

        for (Dice d : dice) {
            if (d.getValue() == mostFreq) {
                d.hold(); // 가장 많이 나온 숫자들만 고정
            }
        }

        System.out.println("컴퓨터가 " + mostFreq + " 고정함 (x" + counts[mostFreq] + ")");
    }

}
