public class DicePlayer {
    private String name;
    private boolean isComputer;
    private int[] scores = new int[14];     // 0~13 점수판
    private boolean[] used = new boolean[14]; // 해당 칸 사용 여부

    public DicePlayer(String name, boolean isComputer) {
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
        if (used[index]) {
            System.out.println(index + "번 칸은 이미 사용되었습니다.");
        } else {
            scores[index] = score;
            used[index] = true;
        }
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
        if (sum >= 63){
            sum += 35;
        }
        return sum;
    }

    public int getTotalScore() {
        int sum = 0;
        for (int s : scores) sum += s;
        return sum;
    }
}
