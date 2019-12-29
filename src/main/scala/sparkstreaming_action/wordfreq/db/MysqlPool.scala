package sparkstreaming_action.wordfreq.db
import java.sql.Connection
import java.util.Properties
import com.mchange.v2.c3p0.ComboPooledDataSource

class MysqlPool extends Serializable{
  private val cpds: ComboPooledDataSource = new ComboPooledDataSource(true)

  try{
    cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8")
    cpds.setDriverClass("com.mysql.jdbc.Driver")
    cpds.setUser("root")
    cpds.setPassword("1q2w3e4r")
    cpds.setMaxPoolSize(200)
    cpds.setMinPoolSize(20)
    cpds.setAcquireIncrement(15)
    cpds.setMaxStatements(180)
  }catch{
    case e: Exception => e.printStackTrace()
  }
  
  def getConnection: Connection = {
    try{
      return cpds.getConnection()
    }catch{
      case ex: Exception =>
        ex.printStackTrace()
        null
    }
  }
}

object MysqlManager{
  var mysqlManager: MysqlPool = _
  def getMysqlManager: MysqlPool = {
    synchronized{
      if (mysqlManager == null){
        mysqlManager = new MysqlPool
      }
    }
    mysqlManager
  }
}
