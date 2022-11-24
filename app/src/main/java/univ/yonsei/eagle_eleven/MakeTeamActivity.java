package univ.yonsei.eagle_eleven;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;


public class MakeTeamActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int teamNum = 11;   //팀 인원수 변수

//    이미지용
    private static String TeamName;
//    private FirebaseStorage storage;        //이미지 저장용
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    ImageView imgEmblem;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnCancel = findViewById(R.id.btnCancel);
        EditText edtTeamName = findViewById(R.id.editTeamName);
        EditText edtCaptainName = findViewById(R.id.editCaptainName);
        TextView tvTeamNumber = findViewById(R.id.tvTeamMember);
        imgEmblem = findViewById(R.id.imgTeamEmblem);

//        프로그래스바 숨겨
        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

//        tvTeamNumber.setText(teamNum+"");

//        엠블럼 이미지 변경
//        storage = FirebaseStorage.getInstance();
        imgEmblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                activityResult.launch(galleryIntent);     //이미지 선택
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
                //Editable TeamNumber = edtTeamNumber.getText();
//                이미지 업로드
//                uploadToFirebase(imageUri);

//                데이터 넣기 전 정리
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("CaptainName", ""+CaptainName);
                hashMap.put("TeamNumber", teamNum);
                hashMap.put("GameNum", 0);

                //데이터 넣기
                databaseReference.child("TEAM").child(teamName.toString()).setValue(hashMap);

//                데이터 불러오기 테스트
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM")
                        .child(teamName.toString()).child("CaptainName");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(getApplicationContext(),"주장이름 : "+snapshot.getValue(String.class),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    imageUri = result.getData().getData();
                    imgEmblem.setImageURI(imageUri);
                }
            }
        });

//    파이어베이스 업로드
//    private void uploadToFirebase(Uri uri){
//        StorageReference fileRef = reference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Model model = new Model(uri.toString());
//                    }
//                })
//            }
//        })
//    }

//    파일형식 가져오기
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}