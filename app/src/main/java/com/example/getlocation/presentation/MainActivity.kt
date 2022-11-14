/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.getlocation.presentation

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import com.example.getlocation.R
import com.example.getlocation.presentation.theme.GetlocationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    GetlocationTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */


           CarsScreen(CarsViewModel())

    }
}



@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}


data class Car(
    val name: String = "",
    val price: Int = 0
)

class CarsViewModel : ViewModel() {
    private val database =
        Firebase.database("https://fir-iot-488f2-default-rtdb.firebaseio.com")


    private var _cars = mutableStateOf<List<Car>>(emptyList())
    val cars: State<List<Car>> = _cars

    /* ... */
    /* fun getCars() {
        database.getReference("cars")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _cars.value = task.result.getValue<List<Car>>()!!
                } else {
                    Log.w(TAG, task.exception?.localizedMessage.toString())
                }
            }
    }*/

    fun getCars() {
        database.getReference("cars")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        _cars.value = dataSnapshot.getValue<List<Car>>()!!
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "Failed to read value.", error.toException())
                    }
                }
            )
    }
}

@Composable
fun CarsScreen(viewModel: CarsViewModel ) {
   viewModel.getCars()
    ScalingLazyColumn {

        items(viewModel.cars.value) { car ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = car.name, modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center, color = White)
                Text(text = car.price.toString(), modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center, color = White)

            }
        }
    }
}






