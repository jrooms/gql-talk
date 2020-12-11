package jrooms.example.gql_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.example.gql_talk.R
import jrooms.example.CreateDraftMutation
import jrooms.example.DraftsQuery
import jrooms.example.PostsQuery
import jrooms.example.PublishMutation

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
            .serverUrl("http://10.0.2.2:4000/graphql/endpoint")
            .build()

        GlobalScope.launch {
            val response = try {
                apolloClient.mutate(CreateDraftMutation("android", "client")).toFlow().collect {
                    Log.e("response", "${it.data?.createDraft}")
                }
                apolloClient.mutate(PublishMutation(3)).toFlow().collect {
                    Log.e("response","${it.data?.publish}")
                }
                apolloClient.query(PostsQuery()).toFlow().collect {
                    Log.e("response", "${it.data?.posts}")
                }
                apolloClient.query(DraftsQuery()).toFlow().collect {
                    Log.e("response", "${it.data?.drafts}")
                }
            } catch (e: ApolloException) {
                // handle protocol errors
                Log.e("exception", "${e.message}")
                return@launch
            }
        }
    }
}