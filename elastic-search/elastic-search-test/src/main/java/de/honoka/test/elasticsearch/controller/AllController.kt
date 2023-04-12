package de.honoka.test.elasticsearch.controller

import de.honoka.sdk.json.api.JsonArray
import de.honoka.sdk.json.api.JsonObject
import de.honoka.sdk.util.file.FileUtils
import de.honoka.test.elasticsearch.dao.TestEntityDao
import de.honoka.test.elasticsearch.dao.TestEntityEsDao
import de.honoka.test.elasticsearch.entity.TestEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.transaction.Transactional

@RestController
class AllController(
    private val testEntityDao: TestEntityDao,
    private val testEntityEsDao: TestEntityEsDao
) {

    val fruitList: JsonArray<String> = JsonArray.of(FileUtils.urlToString(
        javaClass.getResource("/file/fruits.json")
    ), String::class.java)

    @RequestMapping("/hello")
    fun hello(): JsonObject = JsonObject.of().add("msg", "hello")

    @Transactional
    @RequestMapping("/entity/add")
    fun add(count: Int) {
        val random = Random()
        for(i in 1..count) {
            val testEntity = TestEntity().apply {
                intCol = random.nextInt(20)
                keyword = fruitList[random.nextInt(fruitList.size)]
                val textBuilder = StringBuilder()
                for(j in 1..5) {
                    textBuilder.append(fruitList[random.nextInt(fruitList.size)])
                }
                text = textBuilder.toString()
            }
            testEntityDao.insert(testEntity)
            testEntityEsDao.save(testEntity)
        }
    }

    @RequestMapping("/entity/all")
    fun findAll(): List<TestEntity> {
        return testEntityDao.selectList(null)
    }
}