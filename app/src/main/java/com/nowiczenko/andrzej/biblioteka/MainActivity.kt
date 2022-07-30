package com.nowiczenko.andrzej.biblioteka


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nowiczenko.andrzej.activities.MenuActivity
import com.nowiczenko.andrzej.activities.RegisterActivity
import com.nowiczenko.andrzej.api.MyApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*



const val BASE_URL = "https://biblioteka-gsoft.herokuapp.com/"

lateinit var userId: String
lateinit var userName: String

class MainActivity : AppCompatActivity() {

    lateinit var users: List<UserItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUsersRequest()
        setListeners()

    }

    override fun onStart() {
        super.onStart()
        getUsersRequest()
    }


    private fun getUsersRequest(){
        CoroutineScope(Dispatchers.IO).launch {
            users = MyApi().getUser().await()
        }
    }

    private fun loginValidation(user: UserItem): Boolean{
        return user.username.equals(edit_text_login.text.toString()) &&
                user.password.equals(edit_text_password.text.toString())
    }

    private fun setListeners(){
        setLoginClickListener()
        setRegisterClickListener()
    }

    private fun setLoginClickListener(){
        button_login.setOnClickListener {
            for(user in users){
                if (loginValidation(user)){
                    val intent = Intent(this, MenuActivity::class.java)
                    userId = user.id.toString()
                    userName = user.username
                    startActivity(intent)
                    return@setOnClickListener
                }
            }
            Toast.makeText(this, "Nieprawidłowe dane logowanie", Toast.LENGTH_SHORT).show()

        }
    }

    private fun setRegisterClickListener(){

        button_register.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)

        }
    }
}
