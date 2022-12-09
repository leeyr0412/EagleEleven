package univ.yonsei.eagle_eleven.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import univ.yonsei.eagle_eleven.MainActivity;
import univ.yonsei.eagle_eleven.MakingGameActivity;
import univ.yonsei.eagle_eleven.MatchingTeamSelectActivity;
import univ.yonsei.eagle_eleven.Model.Team;
import univ.yonsei.eagle_eleven.Model.TeamAdapter;
import univ.yonsei.eagle_eleven.R;

public class TeamSelectAdapter extends RecyclerView.Adapter<TeamSelectAdapter.ViewHolder> {
    private Context mContext;
    private List<Team> mTeams;
//    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private FirebaseAuth mAuth;     //회원정보 관련
    FirebaseUser currentUser;
    String uid;

    @NonNull
    @Override
    public TeamSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.team_item,parent,false);
        return new TeamSelectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamSelectAdapter.ViewHolder holder, int position) {

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();     //로그인한 유저 회원정보 관련 인스턴스 초기화
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();


//        final String yourID;


        final Team team = mTeams.get(position);
        holder.btnTeamSelect.setVisibility(View.VISIBLE);
        String TeamName = team.getTeamName();   //내 팀 이름
        holder.teamname.setText(TeamName);
        holder.txtTeamCaptain.setText("주장 : "+team.getCaptainName());
        holder.txtGameNum.setText("게임수 : "+team.getGameNum());
        String TeamNum = ""+team.getTeamNum();
        holder.txtTeamNumber.setText("팀원수 : "+TeamNum);
        holder.btnTeamSelect.setText("선택");

        Glide.with(mContext).load(team.getEmblemUrl()).into(holder.image_emblem);

        holder.btnTeamSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                데이터 받아온거 인덱싱 해야함
                String GameData = ((MatchingTeamSelectActivity)MatchingTeamSelectActivity.mContext).gamedata;
                Log.i("adapterLog", "GameData: "+GameData);
//                파싱
                String date = GameData.substring(0,GameData.indexOf("-"));
                String yourTeam = GameData.substring(GameData.indexOf("-")+1);
                Log.i("인덱싱 확인", "date: "+date);
                Log.i("인덱싱 확인", "yourTeam: "+yourTeam);

//                매칭 됨

//                디비 작업 해야함, 팀 불러와야함
                View matchingPage = View.inflate(mContext,R.layout.matchingpage,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
                TextView txtDate = matchingPage.findViewById(R.id.txtDate);
                TextView txtYourTeam = matchingPage.findViewById(R.id.txtYourTeam);
                TextView txtMyTeam = matchingPage.findViewById(R.id.txtMyTeam);

//                매칭확인창
                dlg.setTitle("매치 상세 내용");
                txtDate.setText(date); // 리스트 내용 dlg 텍스트 뷰에 넣음
                txtYourTeam.setText(yourTeam);
                txtMyTeam.setText(TeamName/*내 팀 이름*/);
                dlg.setView(matchingPage);
                dlg.setNegativeButton("닫기",null);
                dlg.show();



                Button btnReturn = matchingPage.findViewById(R.id.btnReturn);  //매칭하기 버튼
                btnReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        히스토리용 테이블에 값 넣기@@@@@@@@@@@@@@@@@@@@!#$###############################
                        HashMap<String, Object> MyHistory = new HashMap<>();
                        MyHistory.put("myTeam", ""+TeamName);
                        MyHistory.put("otherTeam", ""+yourTeam);
                        MyHistory.put("Date", date);
                        databaseReference.child("History").child(uid).child(GameData).setValue(MyHistory);

                        HashMap<String, Object> YourHistory = new HashMap<>();
                        YourHistory.put("myTeam", ""+yourTeam);
                        YourHistory.put("otherTeam", ""+TeamName);
                        YourHistory.put("Date", date);
                        databaseReference.child("GameList").child(GameData).child("UID").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String yourID = "" +snapshot.getValue(String.class);
//                                히스토리테이블 등록
                                databaseReference.child("History").child(yourID).child(GameData).setValue(YourHistory);

//                                랭킹 테이블 게임 수 업 시켜야함(상대 유저)
                                databaseReference.child("Ranking").child(yourTeam).child("GameNum").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int value = (int)snapshot.getValue(Integer.class);//저장된 값을 숫자로 받아오고
                                        value +=1;//숫자를 1 증가시켜서
                                        databaseReference.child("Ranking").child(yourTeam).child("GameNum").setValue(value);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

//                                팀 테이블 게임 수 업 시키기(상대 유저)
                                databaseReference.child("TEAM").child(yourID).child(yourTeam).child("GameNum").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int value = (int)snapshot.getValue(Integer.class);//저장된 값을 숫자로 받아오고
                                        value +=1;//숫자를 1 증가시켜서
                                        databaseReference.child("TEAM").child(yourID).child(yourTeam).child("GameNum").setValue(value);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
//                        랭킹 테이블 게임 수 업 시켜야함(로그인 한 유저)
                        databaseReference.child("Ranking").child(TeamName).child("GameNum").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int value = (int)snapshot.getValue(Integer.class);//저장된 값을 숫자로 받아오고
                                value +=1;//숫자를 1 증가시켜서
                                databaseReference.child("Ranking").child(TeamName).child("GameNum").setValue(value);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

//                        팀 테이블 게임 수 업 시키기(로그인 한 유저)
                        databaseReference.child("TEAM").child(uid).child(TeamName).child("GameNum").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int value = (int)snapshot.getValue(Integer.class);//저장된 값을 숫자로 받아오고
                                value +=1;//숫자를 1 증가시켜서
                                databaseReference.child("TEAM").child(uid).child(TeamName).child("GameNum").setValue(value);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


//                      게임 리스트 디비 삭제하기
                        DatabaseReference removeReference = FirebaseDatabase.getInstance().getReference("GameList").child(GameData);
                        removeReference.removeValue();









                        ((Activity)mContext).finish();
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, MakingGameActivity.class/*경기 생성 페이지로 이*/);
//                        intent.putExtra("TeamName",TeamName);
//                        intent.putExtra("TeamNum",TeamNum);
////                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
////                Activity
////                startA
//                        context.startActivity(intent);
                    }
                });


                //items.remove(position);// 매칭 성사 시 리스트뷰에서 삭제
//                adapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(),"매칭이 성사 됐습니다.",Toast.LENGTH_SHORT).show();


//                팀 불러와서 출력했음. 리스트에서 매칭 삭제

//                히스토리 위한 디비에 각각 대입




//                Context 입ontext = v.getContext();
//                Intent intent = new Intent(context, MakingGameActivity.class/*경기 생성 페이지로 이*/);
//                intent.putExtra("TeamName",TeamName);
//                intent.putExtra("TeamNum",TeamNum);
//                context.startActivity(intent);


            }
        });
    }




    @Override
    public int getItemCount() {
        return mTeams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView image_emblem;
        public TextView teamname;
        public TextView txtTeamCaptain;
        public TextView txtTeamNumber;
        public TextView txtGameNum;
        public Button btnTeamSelect;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            image_emblem = itemView.findViewById(R.id.image_emblem);
            teamname = itemView.findViewById(R.id.teamname);
            txtTeamCaptain = itemView.findViewById(R.id.txtTeamCaptain);
            txtTeamNumber = itemView.findViewById(R.id.txtTeamNumber);
            txtGameNum = itemView.findViewById(R.id.txtGameNum);
            btnTeamSelect = itemView.findViewById(R.id.btnTeamSelect);

        }

    }

    public TeamSelectAdapter(Context mContext, List<Team> mTeams){
        this.mContext = mContext;
        this.mTeams = mTeams;
    }
}
