package univ.yonsei.eagle_eleven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import univ.yonsei.eagle_eleven.Adapter.TeamSelectAdapter;
import univ.yonsei.eagle_eleven.Model.Team;
import univ.yonsei.eagle_eleven.Model.TeamAdapter;

public class MatchingTeamSelectActivity extends AppCompatActivity {

    public static Context mContext;         //adapter에 변수 전달용
    public String gamedata;

    private FirebaseAuth mAuth;     //회원정보 관련
    FirebaseUser currentUser;
    String uid;

    List<String> TeamnameList;

    RecyclerView recyclerView;
    TeamSelectAdapter teamAdapter;
    List<Team> teamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_team_select);
        Intent ListName = getIntent();

//        변수 받아옴
        gamedata = ListName.getStringExtra("ListName");
        Log.i(gamedata, "gamedata: "+gamedata);
        mContext = this;

        mAuth = FirebaseAuth.getInstance();     //회원정보 관련 인스턴스 초기화
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("경기할 팀 선택");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamList = new ArrayList<>();
        teamAdapter = new TeamSelectAdapter(this, teamList);
        recyclerView.setAdapter(teamAdapter);

        TeamnameList = new ArrayList<>();

        getTeamlist();

//                공간대여시스템 연결해주기
                Intent InternetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://space.yonsei.ac.kr/index.php?lang=k"));
                startActivity(InternetIntent);
//                finish();
    }

    private void getTeamlist() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeamnameList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    TeamnameList.add(dataSnapshot.getKey());
                }
                showTeams();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showTeams() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Team team = dataSnapshot.getValue(Team.class);
                    for(String teamname : TeamnameList){
                        if(team.getTeamName().equals(teamname)){
                            Log.i("팀이름",teamname);
                            teamList.add(team);
                        }
                    }
                }
                teamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        if(currentUser == null){
//            로그인 안되어있음. 우선 로그인 페이지로 이동
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}