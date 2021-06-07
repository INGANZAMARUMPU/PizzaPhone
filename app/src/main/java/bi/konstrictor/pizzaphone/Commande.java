package bi.konstrictor.pizzaphone;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Commande {
    public String id_commande, total, time;
    public String[] references;

    public Commande(String id_commande, String total, Long time, String references) {
        this.id_commande = id_commande;
        this.total = total;
        this.time = getTime(time);
        this.references = getReferences(references);
    }

    private String getTime(Long integer) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return sdf.format(integer*1000);
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
            displayed.add(s);
        }
        return txt.substring(0, txt.length()-2);
    }
}
