package bi.konstrictor.pizzaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name = findViewById(R.id.user_name);

    }

    public void listerLesPizzas(View view) {
        Toast.makeText(this, "Bienvenue "+user_name.getText(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
    }
}