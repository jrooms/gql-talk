package jrooms.example.gql_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.example.gql_talk.R
import jrooms.example.LaunchDetailsQuery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // First, create an `ApolloClient`
    // Replace the serverUrl with your GraphQL endpoint
    val apolloClient = ApolloClient.builder()
      .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql/endpoint")
      .build()

    GlobalScope.launch {
      val response = try {
        apolloClient.query(LaunchDetailsQuery(id = "83")).toDeferred().await()
      } catch (e: ApolloException) {
        // handle protocol errors
        return@launch
      }

      val launch = response.data?.launch
      if (launch == null || response.hasErrors()) {
        // handle application errors
        return@launch
      }

      // launch now contains a typesafe model of your data
      println("Launch site: ${launch.site}")
    }

  }

}