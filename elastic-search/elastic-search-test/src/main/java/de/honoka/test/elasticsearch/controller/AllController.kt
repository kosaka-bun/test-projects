package de.honoka.test.elasticsearch.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import de.honoka.sdk.json.api.JsonArray
import de.honoka.sdk.json.api.JsonObject
import de.honoka.sdk.util.file.FileUtils
import de.honoka.test.elasticsearch.dao.TestEntityDao
import de.honoka.test.elasticsearch.dao.TestEntityEsDao
import de.honoka.test.elasticsearch.entity.TestEntity
import de.honoka.test.elasticsearch.entity.TestEntityVo
import de.honoka.test.elasticsearch.service.TestEntityService
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.transaction.Transactional

@RestController
class AllController(
    private val testEntityService: TestEntityService,
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

    @GetMapping("/entity/all")
    fun findAll(): List<TestEntity> {
        return testEntityDao.selectList(null)
    }

    @GetMapping("/es/entity/all")
    fun esFindAll(): List<TestEntity> {
        return testEntityEsDao.findAll().toList()
    }

    @DeleteMapping("/entity/all")
    fun deleteAll() {
        testEntityDao.deleteAll()
    }

    @GetMapping("/es/entity/int")
    fun queryIntCol(intCol: Int): List<TestEntity> {
        return testEntityService.esQuery({
            must(QueryBuilders.termQuery("intCol", intCol))
        }).map { it.content }
    }

    @GetMapping("/es/entity/keyword")
    fun queryKeyword(keyword: String): List<TestEntity> {
        return testEntityService.esQuery({
            must(QueryBuilders.termQuery("keyword", keyword))
        }).map { it.content }
    }

    @GetMapping("/es/entity/text")
    fun queryText(keyword: String): List<TestEntityVo> {
        return testEntityService.esQuery({
            must(QueryBuilders.matchQuery("text", keyword))
        }, {
            withHighlightFields(HighlightBuilder.Field("text").apply {
                preTags("<font>").postTags("</font>")
            })
        }).map { TestEntityVo(it.content, it.highlightFields) }
    }
}