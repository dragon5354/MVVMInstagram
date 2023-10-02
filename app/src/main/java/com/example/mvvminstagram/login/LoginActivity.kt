package com.example.mvvminstagram.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvminstagram.R
import com.example.mvvminstagram.databinding.ActivityLoginBinding

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
    fun loginEmail() {
        println("Email")
        loginViewModel.showInputNumberActivity.value = true
    }
    fun findId() {
        println("findId")
        loginViewModel.showFindIdActivity.value = true
    }
}