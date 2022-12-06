package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MatchingTeamSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_team_select);
        Intent ListName = getIntent();

        String a = ListName.getStringExtra("ListName");
        android.util.Log.i("listName: ", a);
    }
}