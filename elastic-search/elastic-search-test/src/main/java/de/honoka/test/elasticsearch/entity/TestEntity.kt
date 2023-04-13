package de.honoka.test.elasticsearch.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Document(indexName = "elastic-search-test.test_entity")
@Entity
data class TestEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    var id: Int? = null,

    @Field(type = FieldType.Integer)
    var intCol: Int? = null,

    @Field(type = FieldType.Keyword)
    var keyword: String? = null,

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    var text: String? = null
)