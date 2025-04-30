package liarsbar;

import common.Card;

import java.util.ArrayList;

public interface Liars {
    String getName();
    ArrayList<Card> selectAct(ArrayList<Card> gameDeck );
    boolean StrikeLiar( ArrayList<Card> LastPlayerCard);
    ArrayList<Card> getHand();
    void showDeck();
    LiarsStrategy getStrategy();
}
