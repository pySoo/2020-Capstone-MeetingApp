package com.example.mmmmeeting.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mmmmeeting.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SignActivity constructor() : ToolbarActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var signInButton: SignInButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setToolbarTitle("우리 지금 만나")
        signInButton = findViewById(R.id.signInButton)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.getCurrentUser() != null) {
            val intent: Intent = Intent(getApplication(), MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signInButton.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                signIn()
            }
        })
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                    public override fun onComplete(task: Task<AuthResult?>) {
                        if (task.isSuccessful()) {
                            val user: FirebaseUser? = mAuth!!.getCurrentUser()
                            readDocId(user)
                        } else {
                            startToast("인증에 실패하였습니다.")
                        }
                    }
                })
    }

    private fun updateUI(check: Boolean) { //update ui code here
        val intent: Intent
        if (check) {
            // 같은 uid 존재 -> 바로 메인 화면으로
            Log.d("Document Read", "exist uid")
            intent = Intent(this, MainActivity::class.java)
        } else {
            // 같은 uid 없으면 -> 회원 정보 입력 창으로
            Log.d("Document Read", "new uid")
            intent = Intent(this, MemberInitActivity::class.java)
        }
        startActivity(intent)
    }

    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // user table 읽어서 같은 uid가 존재하는지 확인
    private fun readDocId(user: FirebaseUser?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        if (user != null) {
            val docRef: DocumentReference = db.collection("users").document(user.getUid())
            docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                public override fun onComplete(task: Task<DocumentSnapshot>) {
                    if (task.isSuccessful()) {
                        val document: DocumentSnapshot = task.getResult()
                        if (document.exists()) {
                            // 로그인 시도한 uid가 이미 document에 존재
                            Log.d("Document Snapshot", "Data is : " + document.getId())
                            updateUI(true)
                        } else {
                            // 로그인 시도한 uid가 document에 없음
                            Log.d("Document Snapshot", "No Document")
                            updateUI(false)
                        }
                    } else {
                        Log.d("Document Snapshot", "Task Fail : " + task.getException())
                    }
                }
            })
        }
    }

    companion object {
        private val RC_SIGN_IN: Int = 9001
    }
}