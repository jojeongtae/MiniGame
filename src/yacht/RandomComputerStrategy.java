package yacht;

import common.SP;

import java.util.Random;

public class RandomComputerStrategy {
    String computerName;
    String easy;
    String normal;
    String hard;
    String hell;

    public RandomComputerStrategy() {

        String[] FirstName = {"김", "이", "박", "최", "장", "조", "황", "손", "지"};
        String[] LastName = {"정태", "주언", "재호", "현범", "둘리", "흥민", "시현", "준홍", "선아", "민규", "창현"};
        this.computerName = FirstName[(int) (Math.random() * 8)] + LastName[(int) (Math.random() * 10)];
        if (computerName.equals("김주언")) {
            SP.s("이 몸.등장", 800);
        }
    }

    public void difficultyLevel() {
        int level = (int) (Math.random() * 4) + 1;
        if (level == 1) {
            SP.s("Easy 모드 돌입 승리시 배당금 1.5 배",600);
        } else if (level == 2) {
            SP.s("Normal 모드 돌입 승리시 배당금 2 배",600);

        } else if (level == 3) {
            SP.s("Hard 모드 돌입 승리시 배당금 3 배",600);
        } else if (level == 4) {
            SP.s("Hell 모드 돌입 승리시 배당금 4 배",600);

        }
    }
}
