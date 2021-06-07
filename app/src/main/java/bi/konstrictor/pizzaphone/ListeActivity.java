package bi.konstrictor.pizzaphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListeActivity extends AppCompatActivity {

    private static final String TAG = "=== PIZZA ===";
    private RecyclerView recycler;
    private String reminder;
    private ArrayList<Pizza> pizzas;
    private AdapterPizza adaptateur;
    private TextView txt_main_total, txt_main_qtt;
    public MutableLiveData<Double> total = new MutableLiveData<>();
    public MutableLiveData<Integer> quantite = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        txt_main_total = findViewById(R.id.txt_main_total);
        txt_main_qtt = findViewById(R.id.txt_main_qtt);

        reminder = getIntent().getStringExtra("reminder");

        recycler = findViewById(R.id.liste_pizza);
        pizzas = new ArrayList<>();
        adaptateur = new AdapterPizza(this, pizzas);
        recycler.setAdapter(adaptateur);
        chargerPizzas();
        total.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double value) {
                txt_main_total.setText(value.toString());
            }
        });
        quantite.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                txt_main_qtt.setText(integer.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_commandes) {
            Intent intent = new Intent(this, CommandesActivity.class);
            intent.putExtra("reminder", reminder);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void chargerPizzas() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(HOST.URL).newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", reminder)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) { }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject json_items = new JSONObject(json);
                    JSONObject json_item;
                    Iterator<String> keys = json_items.keys();
                    Pizza pizza;
                    while (keys.hasNext()){
                        String key = keys.next();
                        json_item = json_items.getJSONObject(key);
                        pizza = new Pizza(
                            json_item.getDouble("prix"),
                            json_item.getString("ingredients"),
                            json_item.getString("image"),
                            key
                        );
                        pizzas.add(pizza);
                    }
                    ListeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adaptateur.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void evaluerLesQuantites(){
        int qtt = 0;
        double total = 0;
        for(Pizza pizza:pizzas){
            if(pizza.quantite == 0) continue;
            qtt += pizza.quantite;
            total += pizza.quantite*pizza.prix;
        }
        this.quantite.setValue(qtt);
        this.total.setValue(total);
    }
}