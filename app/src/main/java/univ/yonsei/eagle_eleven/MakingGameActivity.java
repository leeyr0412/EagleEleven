package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MakingGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_game);

        Intent getIntent = getIntent();
        String TeamName = getIntent.getStringExtra("TeamName");
        String TeamNum = getIntent.getStringExtra("TeamNum");

        Log.i("TAG", "TeamName" + TeamName);
        Log.i("TAG", "TeamNum" + TeamNum);
    }
}