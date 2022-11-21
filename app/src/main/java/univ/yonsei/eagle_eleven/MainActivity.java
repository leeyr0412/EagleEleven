package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        팀생성, 팀 불러오기
//        팀 생성
        Button btnTeamMake = findViewById(R.id.btnTeamMake);
        btnTeamMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        Button btnTeamLoad = findViewById(R.id.btnTeamLoad);

        btnTeamMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MakeTeamActivity.class);
                startActivity(intent);
            }
        });
    }
}