package bi.konstrictor.pizzaphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import org.jetbrains.annotations.NotNull;
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

public class CommandesActivity extends AppCompatActivity implements PrintingCallback {
    private static final String TAG = "=== COMMANDES ===";
    private RecyclerView recycler;
    private ArrayList<Commande> commandes;
    private AdapterCommande adaptateur;
    private String reminder;
    private Printing printing;
    ActivityResultLauncher<Intent> activityLauncher;
    private Commande commande;

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
        activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(commande != null && Printooth.INSTANCE.hasPairedPrinter()){
                            printCommande(commande);
                        } else {
                            commande = null;
                        }
                    } else {
                        commande = null;
                    }
                }
            }
        );
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

    public void printCommande(Commande commande) {
        this.commande = commande;
        if (!Printooth.INSTANCE.hasPairedPrinter()){
            Intent intent = new Intent(this, ScanningActivity.class);
            activityLauncher.launch(intent);
        }
        if(Printooth.INSTANCE.hasPairedPrinter()){
            printing = Printooth.INSTANCE.printer();
            printing.setPrintingCallback(this);
            ArrayList<Printable> printables = new ArrayList<Printable>();
            Printable printable = new TextPrintable.Builder()
                    .setText(commande.toPrintableString()) //The text you want to print
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD()) //Bold or normal
                    .setFontSize(DefaultPrinter.Companion.getFONT_SIZE_NORMAL())
                    .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_OFF()) // Underline on/off
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252()) // Character code to support languages
                    .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_30())
                    .setNewLinesAfter(1) // To provide n lines after sentence
                    .build();
            printables.add(printable);
            printing.print(printables);
        }
    }

    public void connectingWithPrinter() {
        Toast.makeText(getApplicationContext(), "Connecting with printer", Toast.LENGTH_SHORT).show();
        Log.d("xxx", "Connecting");
    }
    public void printingOrderSentSuccessfully() {
        Toast.makeText(getApplicationContext(), "printingOrderSentSuccessfully", Toast.LENGTH_SHORT).show();
        Log.d("xxx", "printingOrderSentSuccessfully");
    }
    public void connectionFailed(@NonNull String error) {
        Toast.makeText(getApplicationContext(), "connectionFailed :"+error, Toast.LENGTH_SHORT).show();
        Log.d("xxx", "connectionFailed : "+error);
    }
    public void onError(@NonNull String error) {
        Toast.makeText(getApplicationContext(), "onError :"+error, Toast.LENGTH_SHORT).show();
        Log.d("xxx", "onError : "+error);
    }
    public void onMessage(@NonNull String message) {
        Toast.makeText(getApplicationContext(), "onMessage :" +message, Toast.LENGTH_SHORT).show();
        Log.d("xxx", "onMessage : "+message);
    }
}