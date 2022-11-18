package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class MakeTeamActivity extends AppCompatActivity {
    //private FirebaseAuth auth = FirebaseAuth.getInstance();
    //private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnCancel = findViewById(R.id.btnCancel);

        EditText edtTeamName = findViewById(R.id.editTeamName);
        EditText edtCaptainName = findViewById(R.id.editCaptainName);
        EditText edtTeamNumber = findViewById(R.id.editTeamMember);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable teamName = edtTeamName.getText();
                Editable CaptainName = edtCaptainName.getText();
                Editable TeamNumber = edtTeamNumber.getText();
                databaseReference.child("teamName").push().setValue(teamName.toString());
                databaseReference.child("CaptainName").push().setValue(CaptainName.toString());
                databaseReference.child("TeamNumber").push().setValue(TeamNumber.toString());
                Toast.makeText(getApplicationContext(),""+teamName,Toast.LENGTH_SHORT).show();
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