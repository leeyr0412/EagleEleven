package univ.yonsei.eagle_eleven;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import android.widget.Toast;


import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    LinearLayout baseLayout; //메인 xml의 부모 레이아웃 id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /////////////////// list View 시작/////////////////////
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
        /////////////////// list View 끝 /////////////////////

        ////////////////// 순위 시작 ////////////////////////
        TextView txtFirst = findViewById(R.id.txtFirst);
        TextView txtSecond = findViewById(R.id.txtSecond);
        TextView txtThird = findViewById(R.id.txtThird);
        TextView txtFourth = findViewById(R.id.txtFourth);
        TextView txtFifth = findViewById(R.id.txtFifth);
        ArrayList<String> tName = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("TEAM");
        Query myTopTeam = mDatabase.orderByChild("GameNum").limitToFirst(5);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //android.util.Log.i("snapshot: ",dataSnapshot.getValue().toString());
                    String[] array = dataSnapshot.getValue().toString().split(Pattern.quote("}, "));
                    HashMap<String,Integer> map = new HashMap<>();
                    for(int i=0;i<array.length;i++){
                        //+8 , +9 인 이유는 string으로 받아와서 substring하려고 (게임횟수가 1자리수인경우)
                        map.put(array[i].substring(0,array[i].indexOf("=")), Integer.valueOf(array[i].substring(array[i].lastIndexOf("GameNum=")+8,array[i].lastIndexOf("GameNum=")+9)));
                    }
                    List<Map.Entry<String, Integer>> list_entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
                    Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
                        // compare로 값을 비교
                        public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2)
                        {
                            // 내림 차순으로 정렬
                            return obj2.getValue().compareTo(obj1.getValue());
                        }
                    });
                    for(Map.Entry<String, Integer> entry : list_entries) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                    txtFirst.setText(list_entries.get(0).getKey());
                    txtSecond.setText(list_entries.get(1).getKey());
                    txtThird.setText(list_entries.get(2).getKey());
                    txtFourth.setText(list_entries.get(3).getKey());
                    txtFifth.setText(list_entries.get(4).getKey());
                    //tName.add(dataSnapshot.getValue().toString().substring(1,dataSnapshot.getValue().toString().indexOf("=")));
                }

                //tName.add(dataSnapshot.getValue().toString());
                //txtFirst.setText(tName.get(0));
                //txtSecond.setText(tName.get(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ////////////////// 순위 끝 ////////////////////////
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

        //로그아웃 기능
        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad  = new AlertDialog.Builder(MainActivity.this);
                ad.setMessage("로그아웃 하시겠습니까?");

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ad.show();
            }
        });

        baseLayout = findViewById(R.id.baseLayout);
    }

/*  //상단 메뉴바 만들어서 로그아웃 하위메뉴로 넣으려고 했는데 안돼서 일단 보류

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogout:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

 */
}