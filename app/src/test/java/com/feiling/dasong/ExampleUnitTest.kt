package com.feiling.dasong

import com.feiling.dasong.model.InventoryModel
import com.feiling.dasong.model.base.ResponseModel
import com.feiling.dasong.uitils.DateUtils
import com.feiling.dasong.uitils.StringUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val replaceYYYYMMdd = DateUtils.replaceYYYYMMdd(1589186914457L)
        println(replaceYYYYMMdd)
    }

    fun demoJsonArrayFromObject(){
//        var type = object : TypeToken<MutableList<InventoryModel>>() {}.type
//        Gson().fromJson<MutableList<InventoryModel>>(pageModel.list, type)
    }

    @Test
    fun text() {
        var testModel = TestModel("1")
        var text = testModel.txt

        text  =  "3333";
        println(testModel)
    }

    data class TestModel(var txt: String);

    @Test
    fun testList(){
        val testJson = "{\n" +
                "  \"code\": 0,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"accdate\": \"2020-08-24T09:12:59.280Z\",\n" +
                "      \"accuser\": \"string\",\n" +
                "      \"cdid\": 0,\n" +
                "      \"cgcode\": \"string\",\n" +
                "      \"cid\": 0,\n" +
                "      \"descriptions\": \"string\",\n" +
                "      \"did\": 0,\n" +
                "      \"edid\": 0,\n" +
                "      \"id\": 0,\n" +
                "      \"iscomprint\": 0,\n" +
                "      \"iscorrection\": 0,\n" +
                "      \"isdelete\": 0,\n" +
                "      \"isindustry\": 0,\n" +
                "      \"isoutsourcing\": 0,\n" +
                "      \"isqualitytesting\": 0,\n" +
                "      \"levels\": 0,\n" +
                "      \"operating\": \"string\",\n" +
                "      \"pdid\": 0,\n" +
                "      \"pinvcode\": \"string\",\n" +
                "      \"pmName\": \"string\",\n" +
                "      \"pmcode\": \"string\",\n" +
                "      \"porder\": \"string\",\n" +
                "      \"preparationtime\": 0,\n" +
                "      \"priority\": 0,\n" +
                "      \"processingtime\": 0,\n" +
                "      \"scrapflag\": 0,\n" +
                "      \"sdid\": 0,\n" +
                "      \"selcol\": 0,\n" +
                "      \"seq\": 0,\n" +
                "      \"state\": 0,\n" +
                "      \"tempseq\": 0,\n" +
                "      \"toothprofile\": \"string\",\n" +
                "      \"vtype\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"message\": \"string\"\n" +
                "}"
        var responseModel = Gson().fromJson<ResponseModel>(testJson, ResponseModel::class.java)

        var list =
            responseModel.getList<MutableMap<String, Any?>>(MutableMap::class.java)

        print(list)
    }

}
