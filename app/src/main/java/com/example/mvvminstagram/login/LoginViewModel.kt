package com.example.mvvminstagram.login

//import android.app.Application
import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvminstagram.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    // 변수 4가지
    // Id, Password, 다음 화면으로 넘어갈 boolean값, id찾기로 넘어갈 boolean값
    var id : MutableLiveData<String> = MutableLiveData("test")
    var password : MutableLiveData<String> = MutableLiveData("")

    var showInputNumberActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showFindIdActivity : MutableLiveData<Boolean> = MutableLiveData(false)

    // 회원가입을 관리하는 변수
    var auth = FirebaseAuth.getInstance()

    val context = getApplication< Application>().applicationContext

    var googleSignInClient : GoogleSignInClient
    // 구글 변수
    init {
        // 파이어베이스에서 authentication에서 구글을 로그인하는데 쓰겠다고 설정하고,
        // google-services.json를 넣어줘야(설정 안한 상태면 다시 넣어줘야) default_web_client_id 인식을 한다...
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context,gso)
    }

    // 화면을 통제하는 컨트롤러 코드라 뷰모델이 가지고 있는게 좋음
    fun loginWithSignupEmail() {
        println("Email")
        auth.createUserWithEmailAndPassword(
            id.value.toString(),
            password.value.toString()
        ).addOnCompleteListener {
            // 정상적으로 회원가입할 때
            if(it.isSuccessful) {
                showInputNumberActivity.value = true
            }
            // 회원가입이 실패했을 때
            else {
                // 아이디가 있을 경우

            }
        }
    }
    fun loginGoogle(view : View) {
        var i = googleSignInClient.signInIntent
        (view.context as? LoginActivity)?.googleLoginResult?.launch(i)
    }
    fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            // 정상적으로 회원가입할 때
            if(it.isSuccessful) {
                showInputNumberActivity.value = true
            }
            // 회원가입이 실패했을 때
            else {
                // 아이디가 있을 경우

            }
        }
    }
}