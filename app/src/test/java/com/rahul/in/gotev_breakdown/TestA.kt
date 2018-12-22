package com.rahul.`in`.gotev_breakdown
import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File

@RunWith(PowerMockRunner::class)
class TestA{
    @Mock
    private lateinit var context: Context

    val testFilePath = "src/test/raw/large.jpg"
    @Test
    fun upload(){
        val file = File(testFilePath)

        val presenter = MainPresenter()
        val code = presenter.uploadFile(context, file)
        assert(code == 200)
    }
}