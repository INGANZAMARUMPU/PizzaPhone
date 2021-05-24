package bi.konstrictor.pizzaphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        reminder = getIntent().getStringExtra("reminder");

        recycler = findViewById(R.id.liste_pizza);
        pizzas = new ArrayList<>();
        adaptateur = new AdapterPizza(this, pizzas);
        recycler.setAdapter(adaptateur);
        chargerPizzas();
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
                    JSONObject json_item = new JSONObject(json);
                    Iterator<String> keys = json_items.keys();
                    Pizza pizza;
                    while (keys.hasNext()){
                        String key = keys.next();
                        json_item = json_items.getJSONObject(key);
                        pizza = new Pizza(
                            json_item.getString("prix"),
                            json_item.getString("ingredients"),
                            json_item.getString("image"),
                            key
                        );
                        Log.i(TAG, pizza.toString());
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
}