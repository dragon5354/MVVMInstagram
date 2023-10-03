package com.example.mvvminstagram.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mvvminstagram.R
import com.example.mvvminstagram.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays


class LoginActivity : AppCompatActivity() {
    lateinit var  callbackManager: CallbackManager

    val TAG = "LoginActivity"
    // 바인딩
    lateinit var binding : ActivityLoginBinding
    // 뷰모델 클래스 선언
    val loginViewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        // 바인딩과 현재 LoginActivity의 생명주기를 같게 함
        binding.viewModel = loginViewModel
        binding.activity = this
        binding.lifecycleOwner = this
        callbackManager = CallbackManager.Factory.create()
        setObserve()
        printHashKey(this)
    }

    fun loginFacebook() {
        var loginManager = LoginManager.getInstance()
        loginManager.loginBehavior = LoginBehavior.WEB_ONLY
        loginManager.logInWithReadPermissions(this, Arrays.asList("email"))
        loginManager.registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }

            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken
                loginViewModel.firebaseAuthWithFacebook(token)

            }

        })

    }

    // 페이스북 로그인용 해시코드
    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }


    // 실제로 뷰모델의 값이 변환될 때 Activity가 뜰 수 있도록 하는 함수
    fun setObserve() {
        loginViewModel.showInputNumberActivity.observe(this) {
            // 로그인이 성공했을 때
            if(it) {
                finish()
                // inputNumberActivity를 실행
                startActivity(Intent(this,InputNumberActivity::class.java))

            }
        }
        loginViewModel.showFindIdActivity.observe(this) {
            // 로그인이 성공했을 때
            if(it) {
                // findIdActivity를 실행
                startActivity(Intent(this,FindIdActivity::class.java))

            }
        }

    }
    fun findId() {
        println("findId")
        loginViewModel.showFindIdActivity.value = true
    }

    // 구글 로그인이 성공한 결과값 받는 함수
    // 뷰모델에 넣기 실질적으로 힘들다고 함.
    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->

        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        account.idToken // 로그인한 사용자 정보를 암호화한 값
        loginViewModel.firebaseAuthWithGoogle(account.idToken)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}