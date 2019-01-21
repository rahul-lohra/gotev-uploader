package com.rahul.`in`.gotev_breakdown

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btn)
        button.setOnClickListener {

            val obs = Observable.fromCallable {
                val testFilePath = "src/main/raw/large.jpg"
                val file = File(testFilePath)

                val presenter = MainPresenter()
                val code = presenter.uploadFile(this, file)
                assert(code == 200)
            }

            obs.subscribeOn(Schedulers.io())
                .subscribe { it ->
                    {
                        println("Hello")
                    }
                }

        }
    }
}
