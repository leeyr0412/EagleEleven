package univ.yonsei.eagle_eleven;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class MakeTeamActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int teamNum = 11;   //팀 인원수 변수
    private static String TeamName; //팀 이름

//    이미지용+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    ImageView ivEmblem;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference emblemRef;

    private ProgressBar progressBar;
//    private final DatabaseReference imgBDRef = FirebaseDatabase.getInstance().getReference("TEAM");
//    private final StorageReference imgSRef = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
//
    private UploadTask uploadTask;





//    private String pathUri;
//    public static final int IMAGE_REQUEST = 1;
//    StorageReference storageReference;
//    StorageTask uploadTask;
    EditText edtTeamName;


//    private FirebaseStorage storage = FirebaseStorage.getInstance();        // 스토리지 접근 위한 인스턴스
//    ;
//    private StorageReference pathReference = storageReference.child("emblem");   //사진저장주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnCancel = findViewById(R.id.btnCancel);
        edtTeamName = findViewById(R.id.editTeamName);
        EditText edtCaptainName = findViewById(R.id.editCaptainName);
        TextView tvTeamNumber = findViewById(R.id.tvTeamMember);
        ivEmblem = findViewById(R.id.imgTeamEmblem);

//        StorageReference imgReference = storageReference.child("emblem/")
//        pathReference =



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






//        tvTeamNumber.setText(teamNum+"");

//        엠블럼 이미지 변경
//        storage = FirebaseStorage.getInstance();
//        이미지 클릭 시
//        storageReference = FirebaseStorage.getInstance().getReference();
//        ivEmblem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/");
//                activityResult.launch(galleryIntent);     //이미지 선택
//            }
//        });
//      팀 엠블럼 조작 끝----------------------------------------------------

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



//                hashMap.put("EmblemUri", imageUri.);

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

                if(imageUri != null){   //이미지 선택됨
                    uploadToFirebase(imageUri);
                }else{
//                   기본이미지
                }

//                결과보여주기(미완)
                View makeTeamResult = View.inflate(MakeTeamActivity.this,R.layout.dlg_make_team_result,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MakeTeamActivity.this);
                dlg.setTitle("팀 생성 결과");
                dlg.setView(makeTeamResult);
                dlg.setNegativeButton("닫기",null);
                dlg.show();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM").child(TeamName);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("EmblemUrl",""+uri.toString());

                reference.updateChildren(hashMap);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressBar.setVisibility(View.VISIBLE);
            }
        });


//        StorageReference fileRef = imgSRef.child("emblem/"+TeamName+"."+getFileExtension(uri));

//        *@*@*@*@*@*@**@*@*@*@*@
//        uploadTask = fileRef.putFile(uri);
//        uploadTask.continueWithTask(new Continuation() {
//            @Override
//            public Object then(@NonNull Task task) throws Exception {
//                if(!task.isSuccessful()){
//                    throw task.getException();
//                }
//                return fileRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if(task.isSuccessful()){
//                    Uri  downloadUri = task.getResult();
//                    String myUri = downloadUri.toString();
//
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TEAM").child(TeamName);
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("emblemurl",""+myUri);
//
//                    reference.updateChildren(hashMap);
//                }
//            }
//        });

//        ************!!!!!!!!!!!!!!!!!!!!!!!!!!
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Emblem emblem = new Emblem(uri.toString());
//                        String emblemId = imgBDRef.push().getKey(); //키로 아이디 생성
//                        imgBDRef.child(emblemId).setValue(emblem);
//                        progressBar.setVisibility(View.INVISIBLE);
//                        ivEmblem.setImageResource(R.drawable.loading);
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//        });
//        ************!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    //    파일형식 가져오기
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}














