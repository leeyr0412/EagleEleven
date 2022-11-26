package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Toast;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////// list View /////////////////////
        final String[] matches = {"2022년 11월 12일 17시","2022년 11월 13일 13시","2022년 11월 9일 19시","2022년 11월 3일 17시",
                                "2022년 11월 13일 11시","2022년 11월 29일 9시","2022년 11월 28일 11시","2022년 11월 30일 19시"};
        final ArrayList<String> items = new ArrayList<>();
        for(String match: matches) {
            items.add(match);
        }
        ListView list = findViewById(R.id.listView1);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,match);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View matchingPage = View.inflate(MainActivity.this,R.layout.matchingpage,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                TextView txtDate = matchingPage.findViewById(R.id.txtDate);
                TextView txtTeamNumber = matchingPage.findViewById(R.id.txtTeamNumber);
                Button btnReturn = matchingPage.findViewById(R.id.btnReturn);
                btnReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        items.remove(position);// 매칭 성사 시 리스트뷰에서 삭제
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"매칭이 성사 됐습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setTitle("매치 상세 내용");
                String content = (String) parent.getAdapter().getItem(position); //리스트 클릭시 해당 내용 불러옴
                txtDate.setText(content); // 리스트 내용 dlg 텍스트 뷰에 넣음
                dlg.setView(matchingPage);
                dlg.setNegativeButton("닫기",null);
                dlg.show();
            }
        });
        Button btnAdd = findViewById(R.id.btnAdd);

        /////////////////// list View /////////////////////
        Button btnTeamMake = findViewById(R.id.btnTeamMake);
        btnTeamMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MakeTeamActivity.class);
                startActivity(intent);
            }
        });

        Button btnTeamLoad = findViewById(R.id.btnTeamLoad);
        btnTeamLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoadTeamActivity.class);
                startActivity(intent);
            }
        });

    }
}