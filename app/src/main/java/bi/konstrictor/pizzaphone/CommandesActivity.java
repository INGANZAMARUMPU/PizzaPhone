package bi.konstrictor.pizzaphone;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommandesActivity extends AppCompatActivity {
    private static final String TAG = "=== COMMANDES ===";
    private RecyclerView recycler;
    private ArrayList<Commande> commandes;
    private AdapterCommande adaptateur;
    private String reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandes);

        reminder = getIntent().getStringExtra("reminder");

        recycler = findViewById(R.id.liste_pizza);
        commandes = new ArrayList<>();
        adaptateur = new AdapterCommande(this, commandes);
        recycler.setAdapter(adaptateur);
        chargerCommandes();
    }

    private void chargerCommandes() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(HOST.URL+"/mescommandes").newBuilder();

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", reminder)
                .build();
        Log.i(TAG, request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) { }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Log.i(TAG, json);
                    JSONArray json_items = new JSONArray(json);
                    JSONObject json_item;
                    Commande commande;
                    for (int i=0; i<json_items.length(); i++){
                        json_item = json_items.getJSONObject(i);
                        commande = new Commande(
                                json_item.getString("idCommande"),
                                json_item.getString("Total"),
                                json_item.getLong("Time"),
                                json_item.getString("References")
                        );
                        commandes.add(commande);
                    }
                    CommandesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        adaptateur.setData(commandes);
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