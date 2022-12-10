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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;


import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;

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
    EditText dlgTime,dlgDate;
    TextView txtResult;
    Popup popup;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Eagle Eleven");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        /////////////////// list View 시작/////////////////////
        ListView list = findViewById(R.id.listView1);
        final String[] matches = {};
        final ArrayList<String> items = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
        DatabaseReference GameListDatabase = FirebaseDatabase.getInstance().getReference("GameList");
        GameListDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String Data = dataSnapshot.getValue().toString();
                    //android.util.Log.i("Data: ", Data);
                    //android.util.Log.i("Data: ", String.valueOf(Data.lastIndexOf("Date=")+5));
                    items.add(Data.substring(Data.indexOf("Date=")+5,Data.indexOf("}")));
                    //map.put(arrayValue.substring(arrayValue.lastIndexOf("TeamName=")+9,arrayValue.indexOf("}")), Integer.valueOf(arrayValue.substring(arrayValue.lastIndexOf("GameNum=")+8,arrayValue.indexOf(","))));
                    //android.util.Log.i("map2: ", String.valueOf(map));
                }
                android.util.Log.i("Items: ", String.valueOf(items));
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //final String[] matches = {"2022년 11월 12일 17시","2022년 11월 13일 13시","2022년 11월 9일 19시","2022년 11월 3일 17시",
        //        "2022년 11월 13일 11시","2022년 11월 29일 9시","2022년 11월 28일 11시","2022년 11월 30일 19시"};
        //final ArrayList<String> items = new ArrayList<>();
        //for(String match: matches) {
        //    items.add(match);
        //}
        ////////////////////list view 끝 ///////////////////

        /////////////////// 매치 생성 ///////////////////////

        View makeMatchingPage = View.inflate(MainActivity.this,R.layout.make_matching_page,null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlgTime = makeMatchingPage.findViewById(R.id.dlgTime);
        dlgDate = makeMatchingPage.findViewById(R.id.dlgDate);

        /*
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("매칭 생성하기");
                dlg.setView(makeMatchingPage);

                dlg.setPositiveButton("등록하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = dlgDate.getText().toString();
                        String Time = dlgTime.getText().toString();

                        android.util.Log.i("date: ",date + " "+Time);
                        String[] TotalDate = date.split("/");
                        items.add(TotalDate[0] + "년" +TotalDate[1]+ "월" + TotalDate[2] +"일 " + Time + "시");
                    }
                });
                dlg.setNegativeButton("취소",null);

                dlg.show();
            }
        });
        */

        /////////////////매치 생성 끝 ////////////////////////
        //ListView list = findViewById(R.id.listView1);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        //list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View matchingPage = View.inflate(MainActivity.this,R.layout.matchingpage,null);
//                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                TextView txtDate = matchingPage.findViewById(R.id.txtDate);
//                TextView txtTeamNumber = matchingPage.findViewById(R.id.txtTeamNumber);
                Button btnReturn = matchingPage.findViewById(R.id.btnReturn);  //매칭하기 버튼

                btnReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.util.Log.i("position: ", String.valueOf(position));
                        android.util.Log.i("position: ", String.valueOf(items.get(position)));
                        DatabaseReference removeReference = FirebaseDatabase.getInstance().getReference("GameList").child(items.get(position));
                        removeReference.removeValue();
                        items.remove(position);// 매칭 성사 시 리스트뷰에서 삭제
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"매칭이 성사 됐습니다.",Toast.LENGTH_SHORT).show();
                    }
                });

                String content = (String) parent.getAdapter().getItem(position); //리스트 클릭시 해당 내용 불러옴
                Intent intent = new Intent(getApplicationContext(),MatchingTeamSelectActivity.class);
                intent.putExtra("ListName",content);
                startActivity(intent);

//
//                dlg.setTitle("매치 상세 내용");
//
//
//                txtDate.setText(content); // 리스트 내용 dlg 텍스트 뷰에 넣음
//                dlg.setView(matchingPage);
//                dlg.setNegativeButton("닫기",null);
//
//                dlg.show();
            }
        });
        /////////////////// list View 끝 /////////////////////

        ////////////////// 순위 시작 ////////////////////////
        TextView txtFirst = findViewById(R.id.txtFirst);
        TextView txtSecond = findViewById(R.id.txtSecond);
        TextView txtThird = findViewById(R.id.txtThird);
        TextView txtFourth = findViewById(R.id.txtFourth);
        TextView txtFifth = findViewById(R.id.txtFifth);
        ArrayList<String> tName = new ArrayList<>();

        //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("TEAM");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ranking");
        //Query myTopTeam = mDatabase.orderByChild("GameNum").limitToFirst(5);

        HashMap<String,Integer> map = new HashMap<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String arrayValue = dataSnapshot.getValue().toString();
                    map.put(arrayValue.substring(arrayValue.lastIndexOf("TeamName=")+9,arrayValue.indexOf("}")), Integer.valueOf(arrayValue.substring(arrayValue.lastIndexOf("GameNum=")+8,arrayValue.indexOf(","))));
                    //android.util.Log.i("map2: ", String.valueOf(map));
                }
                //android.util.Log.i("map1", String.valueOf(map));
                List<Map.Entry<String, Integer>> list_entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
                Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
                    // compare로 값을 비교
                    public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2)
                    {
                        // 내림 차순으로 정렬
                        return obj2.getValue().compareTo(obj1.getValue());
                    }
                });
                txtFirst.setText(list_entries.get(0).getKey());
                txtSecond.setText(list_entries.get(1).getKey());
                txtThird.setText(list_entries.get(2).getKey());
                txtFourth.setText(list_entries.get(3).getKey());
                txtFifth.setText(list_entries.get(4).getKey());
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
        ImageButton btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad  = new AlertDialog.Builder(MainActivity.this);
                ad.setMessage("로그아웃 하시겠습니까?");

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        finish();
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

        //////////////////////////팝업창 시작///////////////////////////
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
/*
        ArrayList<GameListData> gameList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("GameList");
        Query myTopPostsQuery = mDatabase.orderByChild("GameHost").equalTo("대한민국");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GameListData gameListData = dataSnapshot.getValue(GameListData.class);
                    gameList.add(gameListData);
                }
                Log.w("MainActivity", "Gamelist = "+gameList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "onCancelled");
            }
        });

 */
        ImageButton btnNotification = findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        /////////////////////////팝업창 끝////////////////////////////////

    }

    /*
  //상단 메뉴바 만들어서 로그아웃 하위메뉴로 넣으려고 했는데 안돼서 일단 보류

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