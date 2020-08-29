package com.feiling.dasong.model.process

/**
 * 描述： 检验项目列表
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/7
 * @author ql
 */
class ProcessCheckModel {
    var checkcode: String? = null //(string): 检验编码 ,
    var checkname: String? = null //  (string): 检验项目名称 ,
    var description: String? = null //(string): 描述 ,
    var did: Int? = null //(integer): 检验项目主键 ,
    var maxvalue: String? = null //(string): 最大值 ,
    var minvalue: String? = null // (string): 最小值 ,
    var pmcode: String? = null //  (string): 工序编码 ,
    var priority: Int? = null //(integer): 顺序号 ,
    var seq: Int? = null // (integer): 行号 ,
    var stanvalue: Int? = null // (string): （待定）
}