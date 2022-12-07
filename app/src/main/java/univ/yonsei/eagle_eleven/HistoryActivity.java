package univ.yonsei.eagle_eleven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;     //회원정보 관련
    FirebaseUser currentUser;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth = FirebaseAuth.getInstance();     //회원정보 관련 인스턴스 초기화
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        /////////////////메인으로 되돌아가기 시작///////////////////
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        ////////////////메인으로 되돌아가기 끝/////////////////////


        //////////////히스토리 리스트 시작////////////////////
        ListView listView = findViewById(R.id.HistoryView);
        ArrayList<String> History = new ArrayList<>();
        ArrayList<String> OtherTeam = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_1,History);
        DatabaseReference HistoryDatabase = FirebaseDatabase.getInstance().getReference("History");
        HistoryDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                History.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String data = dataSnapshot.getValue().toString();
                    //History.add(data.substring(data.indexOf("Date=")+5, data.indexOf("}")));
                    History.add(data.substring(data.indexOf("otherTeam="), data.lastIndexOf("}")));
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(HistoryActivity.this);
                dlg.setTitle("상대한 팀");
                dlg.setMessage(OtherTeam.toString());
                dlg.setPositiveButton("확인", null);
                dlg.show();
            }
        });

    }
}