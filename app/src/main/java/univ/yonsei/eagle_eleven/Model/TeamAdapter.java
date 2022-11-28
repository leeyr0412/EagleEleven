package univ.yonsei.eagle_eleven.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import univ.yonsei.eagle_eleven.LoginActivity;
import univ.yonsei.eagle_eleven.R;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private Context mContext;
    private List<Team> mTeams;
    private FirebaseUser firebaseUser;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.team_item,parent,false);
        return new TeamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Team team = mTeams.get(position);
        holder.btnTeamSelect.setVisibility(View.VISIBLE);
        holder.teamname.setText(team.getTeamName());
        holder.txtTeamCaptain.setText("주장 : "+team.getCaptainName());
        holder.txtGameNum.setText("게임수 : "+team.getGameNum());
        holder.txtTeamNumber.setText("팀원수 : "+team.getTeamNum());

        Glide.with(mContext).load(team.getEmblemUrl()).into(holder.image_emblem);

        holder.btnTeamSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(v.getContext(),/*경기 생성 페이지로 이*/);
//                Intent intent = new Intent(getApplicationCo동text(), LoginActivity.class);
//                startActivity(intent);
//                매칭신청
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

    public TeamAdapter(Context mContext, List<Team> mTeams){
        this.mContext = mContext;
        this.mTeams = mTeams;
    }
}
