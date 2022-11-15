package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTeamMake = findViewById(R.id.btnTeamMake);
        Button btnTeamLoad = findViewById(R.id.btnTeamLoad);
    }
}