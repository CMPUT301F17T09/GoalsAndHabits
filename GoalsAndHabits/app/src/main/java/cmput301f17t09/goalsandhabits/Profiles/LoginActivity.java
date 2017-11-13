package cmput301f17t09.goalsandhabits.Profiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cmput301f17t09.goalsandhabits.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
        final int duration = Toast.LENGTH_SHORT;

        Button login = (Button) findViewById(R.id.login_button);
        Button signup = (Button) findViewById(R.id.signup_button);
        final EditText loginID = (EditText) findViewById(R.id.loginid);
        final EditText loginName = (EditText) findViewById(R.id.loginname);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginID.getText().toString().isEmpty() || loginName.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this,"Please fill all fields",duration).show();
                }
                else {
                    finish();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,NewProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
