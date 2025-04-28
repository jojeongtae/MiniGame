public class Dice {
    private int dice;
    private boolean flag;
    public Dice(){
        diceRoll();
    }
    public void diceRoll(){
        if (!flag) {
            this.dice = (int) (Math.random() * 6 + 1);
        }
        }
    public void hold(){
        flag =true;
    }
    public void notHold(){
        flag = false;
    }
    @Override
    public String toString() {
        // 고정된 주사위는 [숫자], 안 고정은 그냥 숫자
        return flag ? "[" + dice + "]" : String.valueOf(dice);
    }
}
