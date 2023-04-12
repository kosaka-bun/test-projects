package de.honoka.test.elasticsearch.dao

import de.honoka.test.elasticsearch.entity.TestEntity
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface TestEntityEsDao : ElasticsearchRepository<TestEntity, Int>