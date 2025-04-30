package blackjack;

import common.User;
public class BetAmount {
     User user;
    public BetAmount(User user){
     this.user = user;
    }
    public int doubleDown(int stake){
    return this.user.addUserMoney((stake*4));
    }
    public int split(int stake){
        return this.user.addUserMoney((stake*2));

    }
    public int blackjack(int stake){
        return this.user.addUserMoney((stake*3));
    }

}