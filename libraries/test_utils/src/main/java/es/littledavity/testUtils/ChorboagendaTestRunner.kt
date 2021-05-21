package es.littledavity.testUtils

import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class ChorboagendaTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        classLoader: ClassLoader,
        className: String,
        context: Context
    ) = super.newApplication(
        classLoader,
        HiltTestApplication::class.java.name,
        context
    )
}