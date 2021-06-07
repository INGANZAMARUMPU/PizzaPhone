package bi.konstrictor.pizzaphone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Commande {
    public String id_commande, total, time;
    public String[] references;

    public Commande(String id_commande, String total, String time, String references) {
        this.id_commande = id_commande;
        this.total = total;
        this.time = getTime(Integer.getInteger(time));
        this.references = getReferences(references);
    }

    private String getTime(Integer integer) {
        Date date = new Date(integer);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(date);
    }

    private String[] getReferences(String references) {
        String pure_str = references.replace("\"", "").replace("[", "").replace("]", "").replace("\\", "");
        return pure_str.split(",");
    }

    public int getQuantite(String recette){
        int qtt = 0;
        for(String s: references){
            if (s.equals(recette)) qtt++;
        }
        return qtt;
    }

    @Override
    public String toString() {
        ArrayList<String> displayed = new ArrayList<>();
        String txt = "";
        for(String s: references){
            if(displayed.contains(s)) continue;
            txt += ""+getQuantite(s)+" "+s+", ";
        }
        return txt.substring(0, txt.length()-3);
    }
}
