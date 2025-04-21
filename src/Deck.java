import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suit = {"♠", "◆", "♥", "♣"};
        String[] rank = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        int[] value = {1,2,3,4,5,6,7,8,9,10,10,10,10};
        for (int i = 0 ; i<suit.length; i++){
            for (int j = 0 ; j< rank.length;j++){
                cards.add(new Card(suit[i],rank[j],value[j]));
            }
        }
        Collections.shuffle(cards);
    }
    public Card drawCard(){
        return cards.remove(0);
    }
}
