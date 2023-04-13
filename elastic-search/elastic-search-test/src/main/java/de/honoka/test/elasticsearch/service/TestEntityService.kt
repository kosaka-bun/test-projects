package de.honoka.test.elasticsearch.service

import de.honoka.test.elasticsearch.dao.TestEntityDao
import de.honoka.test.elasticsearch.entity.TestEntity
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service

@Service
class TestEntityService(
    private val testEntityDao: TestEntityDao,
    private val elasticsearchRestTemplate: ElasticsearchRestTemplate
) {

    fun esQuery(
        queryBlock: BoolQueryBuilder.() -> Unit,
        queryBuilderBlock: (NativeSearchQueryBuilder.() -> Unit)? = null
    ): List<SearchHit<TestEntity>> {
        return elasticsearchRestTemplate.search(NativeSearchQueryBuilder().apply {
            withQuery(QueryBuilders.boolQuery().apply(queryBlock))
            queryBuilderBlock?.let { it() }
        }.build(), TestEntity::class.java).searchHits
    }
}