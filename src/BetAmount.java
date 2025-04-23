public class BetAmount {
     User user;
     Deck deck;
    public BetAmount(User user,Deck deck){
     this.user = user;
     this.deck = deck;
    }
    public int doubleDown(int stake){
    return this.user.addUserMoney((stake*3));
    }
    public int split(int stake){
        return this.user.addUserMoney((stake*2));

    }
    public int blackjack(int stake){
        return this.user.addUserMoney((stake*3));
    }
//    public int blackjack(int userMoney){
//
//    }

}

