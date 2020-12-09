package jrooms.example.gql_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.example.gql_talk.R
import jrooms.example.PostsQuery

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // First, create an `ApolloClient`
        // Replace the serverUrl with your GraphQL endpoint
        val apolloClient = ApolloClient.builder()
            .serverUrl("http://localhost:4000/graphql/endpoint")
            .build()

        GlobalScope.launch {
            val response = try {
                apolloClient.query(PostsQuery()).toFlow().collect {
                    Log.e("response","${it.data?.posts}")
                }
            } catch (e: ApolloException) {
                // handle protocol errors
                Log.e("exception", "${e.message}")
                return@launch
            }
        }
    }
}