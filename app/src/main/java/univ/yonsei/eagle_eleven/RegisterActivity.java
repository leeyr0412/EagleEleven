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

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText joinName, joinId, joinPwd, joinPwdCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        Button btnJoin = findViewById(R.id.btnJoin);
        Button btnExit = findViewById(R.id.btnExit);

        joinName = findViewById(R.id.joinName);
        joinId = findViewById(R.id.joinId);
        joinPwd = findViewById(R.id.joinPwd);
        joinPwdCheck = findViewById(R.id.joinPwdCheck);

        //회원가입 버튼 클릭시
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinId.getText().toString().equals("") || joinPwd.getText().toString().equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(!joinPwd.getText().toString().equals(joinPwdCheck.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //공백이 아니거나 비밀번호에 문제 없으면 createUser 함수 실행
                        createUser(joinName.getText().toString(), joinId.getText().toString(),
                                joinPwd.getText().toString(), joinPwdCheck.getText().toString());
                    }
                }
            }
        });

        //취소 버튼 클릭시
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
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

