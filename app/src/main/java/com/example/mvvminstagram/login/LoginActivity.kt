package com.example.mvvminstagram.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvminstagram.R
import com.example.mvvminstagram.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
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
        setObserve()
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
}