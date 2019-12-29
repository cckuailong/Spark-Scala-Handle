package sparkstreaming_action.wordfreq.main

import sparkstreaming_action.wordfreq.db._
import java.sql.PreparedStatement
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming._

object WordFreq {
  def main(args: Array[String]) {
    // Create spark context
    val conf = new SparkConf()
      .setAppName("WordFreq_Spark")
      .setMaster("spark://127.0.0.1:7077")
    val ssc = new StreamingContext(conf, Seconds(10))
    val filename = "data"
    val lines = ssc.textFileStream(filename)
    val wcData = lines.flatMap { line => line.split(" ") }
      .map { word => (word, 1) }
      .reduceByKey(_ + _)
    wcData.cache()
    wcData.print()
    wcData.foreachRDD(rdd =>{
      if (!rdd.isEmpty){
        rdd.foreachPartition(partitionRecords => {
          val conn = MysqlManager.getMysqlManager.getConnection
          val sql = "insert into words (name, count) values (?,?)"
          val ps: PreparedStatement = conn.prepareStatement(sql)
          try{
            partitionRecords.foreach(record => {
              val name: String = record._1
              val count: Int = record._2
              ps.setString(1, name)
              ps.setInt(2,count)
              ps.addBatch()
            })
            ps.executeBatch()
            conn.commit()
          }catch{
            case e: Exception => e.printStackTrace()
          }finally{
            ps.close()
            conn.close()
          }
        })
      }
    })

    
    ssc.start()
    ssc.awaitTermination()
  }
}
