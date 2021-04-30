package com.example.botanicallibrary;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.template.AddNewAnswerDialog;
import com.example.botanicallibrary.template.SettingDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class QuestionDetailActivity extends AppCompatActivity {
    private QuestionDetailActivity self = this;
    private FirebaseDatabase database;
    private TextView tv_name, tv_description;
    private ImageView iv_imageQuestion;
    private String keyQuestion, keyselect;
    private StorageReference mStorageRef;
    private ArrayList<Answer> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        answers = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        tv_name = findViewById(R.id.tv_nameQuestion);
        tv_description = findViewById(R.id.tv_descriptionQuestion);
        iv_imageQuestion = findViewById(R.id.iv_imageQuestion);
        ImageView iv_newAnswer = findViewById(R.id.iv_newAnswer);
        loadData();
        RadioGroup rg_listAnswer = findViewById(R.id.rg_listAnswer);
        DatabaseReference myRef = database.getReference(Local.firebaseLocal.ANSWERS).child(keyQuestion);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (getIdUser() != null) rg_listAnswer.setEnabled(false);
                rg_listAnswer.removeAllViewsInLayout();
                answers.clear();
                if (getIdUser() != null && dataSnapshot.child(getIdUser()).exists())
                    keyselect = (String) dataSnapshot.child(getIdUser()).child(Local.firebaseLocal.KEY).getValue();
                dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                    boolean has = false;
                    for (int i = 0; i < answers.size(); i++) {
                        if (answers.get(i).key.equals(dataSnapshot1.child(Local.firebaseLocal.KEY).getValue())) {
                            answers.get(i).vote++;
                            has = true;
                            break;
                        }
                    }
                    if (!has)
                        answers.add(new Answer(String.valueOf(dataSnapshot1.child(Local.firebaseLocal.KEY).getValue()),
                                (String) dataSnapshot1.child(Local.firebaseLocal.NAME).getValue()));
                });
                for (int i = 0; i < answers.size(); i++) {
                    RadioButton radioButton = new RadioButton(getBaseContext());
                    if (answers.get(i).name != null)
                        radioButton.setText(answers.get(i).name.concat(" (")
                                .concat(String.valueOf(answers.get(i).vote))
                                .concat(")"));
                    int finalI = i;
                    radioButton.setOnClickListener(v -> {
                        if (getIdUser() == null) {
                            SettingDialog settingDialog = new SettingDialog(self, false);
                            settingDialog.startDialog();
                            radioButton.setChecked(false);
                            return;
                        }
                        if (answers.get(finalI).key.equals(keyselect)) return;
                        DatabaseReference myRef = database.getReference(Local.firebaseLocal.ANSWERS)
                                .child(keyQuestion).child(getIdUser());
                        myRef.child(Local.firebaseLocal.KEY).setValue(answers.get(finalI).key);
                        myRef.child(Local.firebaseLocal.NAME).setValue(answers.get(finalI).name);
                    });
                    rg_listAnswer.addView(radioButton);
                    if (keyselect != null && keyselect.equals(answers.get(i).key))
                        rg_listAnswer.check(rg_listAnswer.getChildAt(i).getId());
                }
            }
            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
            }
        });
        iv_newAnswer.setOnClickListener(v -> {
            if (getIdUser() != null) {
                AddNewAnswerDialog addNewAnswerDialog = new AddNewAnswerDialog(this, keyQuestion, "");
                addNewAnswerDialog.startDialog();
            } else {
                SettingDialog settingDialog = new SettingDialog(this, false);
                settingDialog.startDialog();
            }
        });
    }
    private String getIdUser() {
        return getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE).getString(Local.DeviceLocal.ID, null);
    }
    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        keyQuestion = bundle.getString(Local.firebaseLocal.KEY);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference colRefsub = firebaseFirestore.collection(Local.QUESTION)
                .document(keyQuestion);
        colRefsub.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tv_name.setText(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).get(Local.firebaseLocal.LABLE)).toString());
                tv_description.setText(Objects.requireNonNull(task.getResult().get(Local.firebaseLocal.DESCRIPTION)).toString());
                String path = Objects.requireNonNull(task.getResult().get(Local.firebaseLocal.IMAGE)).toString();

                StorageReference pathReference = mStorageRef.child(path);
                pathReference.getDownloadUrl()
                        .addOnSuccessListener(taskSnapshot -> Picasso.with(getBaseContext()).load(taskSnapshot.toString()).fit().centerInside().into(iv_imageQuestion))
                        .addOnFailureListener(exception -> System.out.println("load image fail"));
            } else {
                Toasty.error(getBaseContext(), getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private static class Answer {
        private final String key;
        private final String name;
        private int vote;
        public Answer(String key, String name) {
            this.key = key;
            this.name = name;
            this.vote = 1;
        }
    }
}