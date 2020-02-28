package com.sbsj.ordermatstaboss.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sbsj.ordermatstaboss.Activity.BaseActivity;
import com.sbsj.ordermatstaboss.Activity.LoginActivity;
import com.sbsj.ordermatstaboss.Activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLogin implements GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "Google_Login";
    private final String DIRECTORY = "Google";
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient googleSignInClient;
    private Context context;
    private int requestcode;

    public GoogleLogin(final Context context, SignInButton signInButton) {
        this.context = context;
        this.requestcode = requestcode;

        final GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("845191476196-msjpuuq3pucefiu1njfdcqd5o67gct1e.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void FirebaseAuthWithGoogle(final GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ((BaseActivity) context).saveTempLogin(DIRECTORY, googleSignInAccount.getId());
                            ((LoginActivity)context).checkLogin(DIRECTORY, googleSignInAccount.getId());
                        } else {
                            Toast.makeText(context, "구글 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener((Activity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure : " + e.toString());
                    }
                })
                .addOnCanceledListener((Activity) context, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i(TAG, "onCanceled : ");
                    }
                })
                .addOnSuccessListener((Activity) context, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG, "onSuccess : " + authResult.toString());
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed : " + connectionResult.toString());
    }

    public void loginGoogle(final int requestcode) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        ((FragmentActivity) context).startActivityForResult(signInIntent, requestcode);
    }
}
