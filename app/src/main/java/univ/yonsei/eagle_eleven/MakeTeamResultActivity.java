package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MakeTeamResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_make_team_result);

        Intent intent = getIntent();
        String TeamName = intent.getStringExtra("TeamName");
        String Captain = intent.getStringExtra("Captain");
        int teamNum = intent.getIntExtra("teamNum",11);
        Uri imgUri = intent.getParcelableExtra("imgUri");


        ImageView ivEmblem = findViewById(R.id.imgTeamEmblem);
        TextView tvTeamName = findViewById(R.id.tvTeamName);
        TextView tvCaptain = findViewById(R.id.tvCaptainName);
        TextView tvTeamMember = findViewById(R.id.tvTeamMember);
        Button btnOK = findViewById(R.id.btnOK);
        Button btnGoMatching  = findViewById(R.id.btnGoMatching);

        if(imgUri!=null)
            ivEmblem.setImageURI(imgUri);
        tvTeamName.setText(TeamName);
        tvCaptain.setText(Captain);
        tvTeamMember.setText(""+teamNum);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentok = new Intent();
                intentok.putExtra("close", "Close");
                setResult(RESULT_CANCELED, intentok);
                finish();
            }
        });

        btnGoMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentM = new Intent();
                intentM.putExtra("T", TeamName);
                setResult(RESULT_OK, intentM);
                finish();
            }
        });

        Toast.makeText(getApplicationContext(),"이미지 변경",Toast.LENGTH_SHORT).show();


        //Uri imageUri = intent.extra("imageUri");
    }
}