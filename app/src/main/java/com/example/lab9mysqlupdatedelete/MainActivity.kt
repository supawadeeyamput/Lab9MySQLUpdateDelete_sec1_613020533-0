package com.example.lab9mysqlupdatedelete


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import layout.EditStudentAdapter
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    var studentList = arrayListOf<Student>()
    val createClient = StudentAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(applicationContext) as RecyclerView.LayoutManager?
        recycler_view.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onResume() {
        super.onResume()
        callStudentData()
    }
    fun clickSearch(v:View){
        studentList.clear();
        if(edt_search.text.isEmpty()){
            callStudentData()
        }else{
            createClient.retrieveStudentID(edt_search.text.toString())
                .enqueue(object : retrofit2.Callback<Student> {

                    override fun onResponse(call: retrofit2.Call<Student>, response: retrofit2.Response<Student>){
                        studentList.add(Student(response.body()?.std_id.toString(),response.body()?.std_name.toString(),
                            response.body()?.std_age.toString().toInt()))

                        recycler_view.adapter = EditStudentAdapter(studentList,applicationContext)
                    }
                    override fun onFailure(call: retrofit2.Call<Student>, t: Throwable) = t.printStackTrace()
                })
        }

    }

    fun callStudentData(){
        studentList.clear();
        createClient.
        retrieveStudent()
            .enqueue(object : retrofit2.Callback<List<Student>> {
                override fun onResponse(call: retrofit2.Call<List<Student>>, response: retrofit2.Response<List<Student>>) {
                    response.body()?.forEach {
                        studentList.add(Student(it.std_id, it.std_name, it.std_age))
                    }
                    recycler_view.adapter = EditStudentAdapter(studentList, applicationContext)
                }

                override fun onFailure(call: retrofit2.Call<List<Student>>, t: Throwable) =
                    t.printStackTrace()
            })

    }
}
