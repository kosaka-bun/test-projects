package de.honoka.android.variousandroidtest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.hutool.core.util.RandomUtil
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import de.honoka.android.variousandroidtest.R
import de.honoka.sdk.util.android.database.BaseDao
import de.honoka.sdk.util.android.database.Table
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*

class DatabaseTestActivity : AppCompatActivity() {

    private val dao = TestEntityDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)
        CoroutineScope(Dispatchers.IO).launch {
            doAction()
        }
    }

    private fun doAction() {
        //transactionTest()
        val queryForAll = dao.rawDao.queryForAll().apply {
            if(isEmpty()) insertItems(10)
        }
        println(queryForAll)
    }

    private fun transactionTest() {
        try {
            dao.rawDao.callBatchTasks {
                val items = insertItems(5)
                if(items.last().col1!! > 80) throw Exception()
            }
        } catch(t: Throwable) {
            println()
        }
    }

    @Suppress("SameParameterValue")
    private fun insertItems(count: Int): List<TestEntity> {
        val list = ArrayList<TestEntity>()
        repeat(count) {
            dao.rawDao.create(TestEntity().apply {
                id = UUID.randomUUID().toString()
                col1 = RandomUtil.randomInt(1, 100)
                col2 = col1!! / 2 == 0
                col3 = RandomUtil.randomDouble(1.0, 100.0, 2, RoundingMode.HALF_UP)
                //col4 = RandomUtil.randomDouble(1.0, 100.0, 2, RoundingMode.HALF_UP).toInt()
                list.add(this)
            })
        }
        return list
    }
}

class TestEntityDao : BaseDao<TestEntity>(TestEntity::class.java)

@Table(version = 6)
@DatabaseTable(tableName = "test_entity")
data class TestEntity(

    @DatabaseField(id = true)
    var id: String? = null,

    @DatabaseField
    var col1: Int? = null,

    @DatabaseField
    var col2: Boolean? = null,

    @DatabaseField
    var col3: Double? = null,

    @DatabaseField
    var multiWordCol: String? = null,

    //@DatabaseField
    //var col4: Int? = null
)