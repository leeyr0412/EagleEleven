package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;
    private FirebaseStorage firebaseStorage;
    EditText joinName, joinId, joinPwd, joinPwdCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();
        firebaseStorage = firebaseStorage.getInstance();

        Button btnJoin = findViewById(R.id.btnJoin);
        Button btnExit = findViewById(R.id.btnExit);

        joinName = findViewById(R.id.joinName);
        joinId = findViewById(R.id.joinId);
        joinPwd = findViewById(R.id.joinPwd);
        joinPwdCheck = findViewById(R.id.joinPwdCheck);

        //회원가입 버튼 클릭
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinId.getText().toString().equals("") || joinPwd.getText().toString().equals(""))
                { //아이디나 패스워드가 공백일 경우
                    Toast.makeText(RegisterActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(!joinPwd.getText().toString().equals(joinPwdCheck.getText().toString()))
                    { //비밀번호와 비밀번호 확인이 서로 다를경우
                        Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                    }
                    else
                    { //문제 없으면 createUser 함수 실행
                        createUser(joinName.getText().toString(), joinId.getText().toString(),
                                joinPwd.getText().toString(), joinPwdCheck.getText().toString());
                    }
                }
            }
        });

        //회원가입 창에서 '취소' 버튼 클릭
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //회원가입 함수
    private void createUser(String name, String email, String password, String passwordchk) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //회원가입 성공시
                            final String uid = task.getResult().getUser().getUid();
                            //UserModel.java 클래스
                            UserModel userModel = new UserModel();
                            userModel.userName = name;
                            userModel.email = email;
                            userModel.uid = uid;

                            //DB에 'users' 테이블에 회원정보 저장 (uid 정보)
                            firebaseDB.getReference().child("users").child(uid)
                                    .setValue(userModel);

                            //토스트 출력 후, 로그인 페이지로 전환
                            Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else { // 계정이 중복된 경우
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

