package univ.yonsei.eagle_eleven;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.core.Tag;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "MyTag";
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    EditText edtLogin, edtPwd;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        edtLogin = findViewById(R.id.edtLogin);
        edtPwd = findViewById(R.id.edtPwd);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        CheckBox autoLogin = findViewById(R.id.autoLogin);
        if(autoLogin.isChecked() == true){
            if(firebaseAuth.getCurrentUser() != null){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
        /*

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheckedj) {
                if(autoLogin.isChecked() == true){
                    if(firebaseAuth.getCurrentUser() != null){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

         */

        ////////자동로그인 시작///////


        ////////자동로그인 끝///////

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtLogin.getText().toString().equals("") && !edtPwd.getText().toString().equals("")) {
                    loginUser(edtLogin.getText().toString(), edtPwd.getText().toString());
                }
                else {
                    Toast.makeText(LoginActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


}