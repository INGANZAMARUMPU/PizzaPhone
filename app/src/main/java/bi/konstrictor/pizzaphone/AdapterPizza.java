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

public class AdapterPizza extends RecyclerView.Adapter<AdapterPizza.PizzaItem> {
    private Activity activity;
    private ArrayList<Pizza> pizzas;

    public AdapterPizza(Activity activity, ArrayList<Pizza> pizzas) {
        this.activity = activity;
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public PizzaItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pizza, parent, false);
        return new PizzaItem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaItem holder, int position) {
        final Pizza pizza = pizzas.get(position);
        holder.card_ingredient.setText(pizza.ingredients);
        holder.card_name.setText(pizza.nom);
        holder.card_prix.setText(pizza.prix);
        Glide.with(activity).load(HOST.URL+"/"+(pizza.image)+".jpg").into(holder.card_image);
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    public class PizzaItem extends RecyclerView.ViewHolder {
        TextView card_name, card_prix, card_ingredient, card_qtt;
        ImageView card_image;
        Button card_btn_plus, card_btn_moins;
        public PizzaItem(@NonNull View itemView) {
            super(itemView);
            card_name = itemView.findViewById(R.id.card_name);
            card_prix = itemView.findViewById(R.id.card_prix);
            card_ingredient = itemView.findViewById(R.id.card_ingredient);
            card_qtt = itemView.findViewById(R.id.card_qtt);
            card_image = itemView.findViewById(R.id.card_image);
            card_btn_plus = itemView.findViewById(R.id.card_btn_plus);
            card_btn_moins = itemView.findViewById(R.id.card_btn_moins);
        }
    }
}
