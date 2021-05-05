package com.example.github_issues

import java.util.*
import android.os.Bundle
import android.view.View
import java.io.IOException
import com.squareup.okhttp.*
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import com.google.gson.GsonBuilder
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.example.github_issues.entity.Issue
import com.example.github_issues.adapter.IssueAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.issue_item.view.*
import kotlinx.android.synthetic.main.fragment_issue_list.*

/**
 * A simple [Fragment] subclass.
 * Use the [IssueList.newInstance] factory method to
 * create an instance of this fragment.
 */
class IssueList : Fragment() {
    private lateinit var issueAdapter: IssueAdapter

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_issue_list,
            container,
            false
        )

        requestIssues(context)
        return view
    }

    private fun requestIssues(context: Context?) {
        val promise = OkHttpClient()
        val promiseURL = "https://api.github.com/repos/JetBrains/kotlin/issues"
        val promiseBuilder = Request.Builder().url(promiseURL).build()

        promise.newCall(promiseBuilder).enqueue(object : Callback {
            override fun onFailure(request: Request?, expection: IOException?) {
                throw IllegalArgumentException()
            }

            override fun onResponse(response: Response) {
                val convert = GsonBuilder().create()
                val responseStr = response.body().string()

                val resJSON = convert
                    .fromJson(responseStr, Array<Issue>::class.java)
                    .toMutableList()

                activity?.runOnUiThread {
                    issueAdapter = IssueAdapter(resJSON)
                    rvIssueItems.adapter = issueAdapter
                    rvIssueItems.layoutManager = LinearLayoutManager(context)
                }
            }
        })
    }
}



