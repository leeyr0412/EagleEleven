package univ.yonsei.eagle_eleven;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;


public class MakeTeamActivity extends AppCompatActivity {
    private static String TAG = "makeTeam";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private FirebaseAuth mAuth;     //회원정보 관련
    FirebaseUser currentUser;
    String uid;

    private int teamNum = 11;   //팀 인원수 변수
    private static String TeamName; //팀 이름
    private static String captainName;


//    이미지용+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    ImageView ivEmblem;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference emblemRef;
    private ProgressBar progressBar;
    private Uri imageUri;
    private UploadTask uploadTask;

    EditText edtTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        mAuth = FirebaseAuth.getInstance();     //회원정보 관련 인스턴스 초기화

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnCancel = findViewById(R.id.btnCancel);
        edtTeamName = findViewById(R.id.editTeamName);
        EditText edtCaptainName = findViewById(R.id.editCaptainName);
        TextView tvTeamNumber = findViewById(R.id.tvTeamMember);
        ivEmblem = findViewById(R.id.imgTeamEmblem);

//        팀 엠블럼 부붑*******************************************************************************************************
//        프로그래스바 숨겨
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

//        엠블럼 클릭 시 앨범열기
        ivEmblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                activityResult.launch(galleryIntent);
            }
        });

//        인원수 조작
        findViewById(R.id.btnTeamSub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teamNum != 0)
                    teamNum--;
                tvTeamNumber.setText(teamNum+"");
            }
        });
        findViewById(R.id.btnTeamAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamNum++;
                tvTeamNumber.setText(teamNum+"");
            }
        });

//        팀 생성버튼 클릭시
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable teamName = edtTeamName.getText();
                TeamName = teamName.toString();
                Editable CaptainName = edtCaptainName.getText();
                captainName = CaptainName.toString();


//                데이터 넣기 전 정리
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("TeamName", ""+TeamName);
                hashMap.put("CaptainName", ""+CaptainName);
                hashMap.put("TeamNumber", teamNum);
                hashMap.put("GameNum", 0);



//                hashMap.put("EmblemUri", imageUri.);

                //데이터 넣기
                databaseReference.child("TEAM").child(uid).child(TeamName).setValue(hashMap);

//                데이터 불러오기 테스트
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM")
//                        .child(uid).child(TeamName).child("CaptainName");
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Toast.makeText(getApplicationContext(),"주장이름 : "+snapshot.getValue(String.class),Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                if(imageUri != null){   //이미지 선택됨
                    uploadToFirebase(imageUri);
                }else{
//                   기본이미지
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TEAM").child(uid).child(TeamName);
                    HashMap<String, Object> hashMapi = new HashMap<>();
                    hashMapi.put("EmblemUrl","https://firebasestorage.googleapis.com/v0/b/eagle-eleven.appspot.com/o/emblem%2Fdefault.png?alt=media&token=f0ce1b86-1d4b-4c35-82f1-cb3413458759");
                    reference1.updateChildren(hashMapi);

                    showResultDlg();
                }



            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    ////    사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    imageUri = result.getData().getData();
                    ivEmblem.setImageURI(imageUri);
                }
            }
        });

//    파이어베이스 업로드
    private void uploadToFirebase(Uri uri){
        emblemRef = storageReference.child("emblem/"+TeamName+"."+getFileExtension(uri));
        uploadTask = emblemRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                emblemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM").child(uid).child(TeamName);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("EmblemUrl",""+uri.toString());
                        reference.updateChildren(hashMap);
                        progressBar.setVisibility(View.INVISIBLE);

                        showResultDlg();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    //    파일형식 가져오기
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void showResultDlg() {
        Toast.makeText(getApplicationContext(),"토스트 실행됨",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MakeTeamResultActivity.class);
        intent.putExtra("Captain",captainName);
        intent.putExtra("TeamName",TeamName);
        intent.putExtra("teamNum",teamNum);
        intent.putExtra("imgUri",imageUri);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_CANCELED){
                finish();
            }
            else if(resultCode==RESULT_OK){
                Intent go = getIntent();
                go.putExtra("TeamName",TeamName);
                startActivity(go);
                finish();
            }
        }
    }
}














