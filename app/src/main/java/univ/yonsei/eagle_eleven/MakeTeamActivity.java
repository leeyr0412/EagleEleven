package univ.yonsei.eagle_eleven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class MakeTeamActivity extends AppCompatActivity {
    //private FirebaseAuth auth = FirebaseAuth.getInstance();
    //private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int teamNum = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnCancel = findViewById(R.id.btnCancel);

        EditText edtTeamName = findViewById(R.id.editTeamName);
        EditText edtCaptainName = findViewById(R.id.editCaptainName);
        TextView tvTeamNumber = findViewById(R.id.tvTeamMember);

        tvTeamNumber.setText(teamNum+"");

        findViewById(R.id.btnTeamSub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamNum--;
                tvTeamNumber.setText(teamNum+"");
            }
        });
        findViewById(R.id.btnTeamAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamNum++;
                tvTeamNumber.setText(teamNum+"");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable teamName = edtTeamName.getText();
                Editable CaptainName = edtCaptainName.getText();
                //Editable TeamNumber = edtTeamNumber.getText();

                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("CaptainName", ""+CaptainName);
                hashMap.put("TeamNumber", teamNum);

                databaseReference.child("TEAM").child(teamName.toString()).setValue(hashMap);


//                데이터 불러오기 테스트
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM")
                        .child(teamName.toString()).child("CaptainName");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getChildren();
                        Toast.makeText(getApplicationContext(),"주장이름 : "+snapshot.getValue(String.class),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}