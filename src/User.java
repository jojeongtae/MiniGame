public class User {
    private String id;
    private String pw;
    private String userName;
    private int userMoney;

    public User(String id, String pw, String userName, int userMoney) {
        this.id = id;
        this.pw = pw;
        this.userName = userName;
        this.userMoney = userMoney;
    }

    public String getId() {
        return this.id;
    }

    public String getPw() {
        return this.pw;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getUserMoney() {
        return this.userMoney;
    }

    public void setLoseUserMoney(int userMoney) {
        this.userMoney = userMoney/2;
    }
    public void setWinUserMoney(int userMoney) {
        this.userMoney = userMoney *2;
    }
    public int setUserMoney(int userMoney) {
        this.userMoney = userMoney;
        return userMoney;
    }
}
