import java.util.Arrays;

public class DicePlayer {
    private String name;
    private boolean isComputer;
    private int[] scores = new int[14];     // 0~13 점수판
    private boolean[] used = new boolean[14]; // 해당 칸 사용 여부
    public static final int TOTAL_INDEX = 13;
    public static final int SUBTOTAL_INDEX = 12;


    public DicePlayer(String name, boolean isComputer) {
        Arrays.fill(used, false);
        this.name = name;
        this.isComputer = isComputer;

    }

    public String getName() {
        return name;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public void setScore(int index, int score) {

        if (index == SUBTOTAL_INDEX || index == TOTAL_INDEX) {
            scores[index] = score;
            return;
        }
        if (used[index]) {
            System.out.println("이미 사용 됨");
            return;
        }
            scores[index] = score;
            used[index] = true;


    }

    public int getScore(int index) {
        return scores[index];
    }

    public int[] getScores() {
        return scores;
    }

    public boolean isUsed(int index) {
        return used[index];
    }

    public int getSubtotal() {
        int sum = 0;
        for (int i = 0; i <= 5; i++) { // ACE ~ SIXES
            sum += scores[i];
        }
        if (sum >= 63) {
            sum += 35;
        }
        return sum;
    }

    public int getTotalScore() {
        int sum = 0;
        for (int i = 6; i < SUBTOTAL_INDEX; i++) {
            sum += scores[i];
        }
        sum += getSubtotal();
        return sum;
    }
}
