package com.example.user.realmdatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.realmdatabase.model.Student;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText name, age;
    TextView display;
    Button saveBtn;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name_txt);
        age = findViewById(R.id.age_txt);
        display = findViewById(R.id.display_imformation);

        saveBtn = findViewById(R.id.save_btn);
        Log.d(TAG, "onCreate: view initialize done");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                readData();

            }
        });
    }

    private void saveData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Student student = bgRealm.createObject(Student.class);
                student.setName(name.getText().toString().trim());
                student.setAge(Integer.parseInt(age.getText().toString().trim()));
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.d(TAG, "onSuccess: Data written successfilly");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled
                Log.d(TAG, "onError: An error occured");
            }
        });
    }

     private void readData(){
         RealmResults<Student> students = realm.where(Student.class).findAll();
         display.setText("");
         String data = "";

         for (Student student:students) {
             try {
                 Log.d(TAG, "readData: Reading Data");
                 data = data + "\n" + student.toString();

             } catch (NullPointerException e) {
                 e.printStackTrace();
             }
         }
         display.setText(data);
     }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
