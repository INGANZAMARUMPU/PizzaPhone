package bi.konstrictor.pizzaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText user_name;
    public static final String TAG = "=== LOGIN ===";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_name = findViewById(R.id.user_name);

    }

    public void listerLesPizzas(View view) {
        Toast.makeText(MainActivity.this, "connexion...", Toast.LENGTH_SHORT).show();

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(HOST.URL + "/login/"+user_name).newBuilder();
        String url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create("", null);
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                String reminder = "";
                for(String cookie: response.headers().get("Set-Cookie").split(";")) {
                    if (cookie.contains("PHPSESSID")) {
                        reminder = cookie;
                        try {
                            JSONObject j = new JSONObject(json);
                            if (j.getString("return").equals("connected")) {
                                Intent intent = new Intent(MainActivity.this, ListeActivity.class);
                                intent.putExtra("reminder", reminder);
                                startActivity(intent);
                                MainActivity.this.finish();
                            } else {
                                HOST.toast(MainActivity.this, "les infos semblent incorrectes", Toast.LENGTH_LONG);
                            }
                        } catch (final Exception e) {
                            HOST.toast(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                        } finally {
                            break;
                        }
                    }
                }
            }
        });
    }
}