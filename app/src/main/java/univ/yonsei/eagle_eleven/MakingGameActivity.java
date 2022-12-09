package univ.yonsei.eagle_eleven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MakingGameActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;     //회원정보 관련
    FirebaseUser currentUser;
    String uid;

    Button btnMatchRegister;
    Button btnMatchCancel;
    CalendarView calView;
    TimePicker tPicker;
    int selectYear,selectMonth,selectDay;
    String RegisterDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_game);

        mAuth = FirebaseAuth.getInstance();     //회원정보 관련 인스턴스 초기화
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        HashMap<String, Object> hashMap = new HashMap<>();

        Intent getIntent = getIntent();
        String TeamName = getIntent.getStringExtra("TeamName");
        String TeamNum = getIntent.getStringExtra("TeamNum");

        //Log.i("TAG", "TeamName" + TeamName);
        //Log.i("TAG", "TeamNum" + TeamNum);
        btnMatchRegister = findViewById(R.id.btnMatchRegister);
        btnMatchCancel = findViewById(R.id.btnMatchCancel);
        calView = findViewById(R.id.calView);
        tPicker = findViewById(R.id.tPicker);

        btnMatchRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterDate = selectYear + "년 " + selectMonth + "월 " + selectDay + "일 " + tPicker.getCurrentHour() + "시-" + TeamName;
                hashMap.put("TeamNumber",TeamNum);
                hashMap.put("GameHost", TeamName);
                hashMap.put("Date",RegisterDate);
                hashMap.put("UID",uid);
                databaseReference.child("GameList").child(RegisterDate).setValue(hashMap);
                Toast.makeText(getApplicationContext(),"매칭이 생성 됐습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        btnMatchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectYear = year;
                selectMonth = month+1;
                selectDay = dayOfMonth;
            }
        });

    }
}