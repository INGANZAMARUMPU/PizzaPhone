package bi.konstrictor.pizzaphone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import bi.konstrictor.pizzaphone.Commande;
import bi.konstrictor.pizzaphone.R;

public class AdapterCommande extends RecyclerView.Adapter<AdapterCommande.CommandeItem>{
    private Activity activity;
    private ArrayList<Commande> commandes;

    public AdapterCommande(Activity activity, ArrayList<Commande> commandes) {
        this.activity = activity;
        this.commandes = commandes;
    }

    @NonNull
    @Override
    public CommandeItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_commande, parent, false);
        return new CommandeItem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommandeItem holder, int position) {
        final Commande commande = commandes.get(position);
        holder.txt_id_commande.setText(commande.id_commande);
        holder.txt_commande_prix.setText(commande.total);
        holder.txt_references.setText(commande.toString());
        holder.txt_commande_date.setText(commande.time);
    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }

    public void setData(ArrayList<Commande> commandes) {
        this.commandes = commandes;
    }

    public class CommandeItem extends RecyclerView.ViewHolder {
        TextView txt_id_commande, txt_commande_prix, txt_references, txt_commande_date;
        public CommandeItem(@NonNull View itemView) {
            super(itemView);
            txt_id_commande = itemView.findViewById(R.id.txt_id_commande);
            txt_references = itemView.findViewById(R.id.txt_references);
            txt_commande_prix = itemView.findViewById(R.id.txt_commande_prix);
            txt_commande_date = itemView.findViewById(R.id.txt_commande_date);
        }
    }
}
