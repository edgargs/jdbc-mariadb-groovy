
println "Init connection"

@GrabConfig(systemClassLoader=true)
@Grab(group='org.mariadb.jdbc', module='mariadb-java-client', version='2.5.1')

import groovy.transform.Field

@Field def params
/*params = [server:"10.10.10.1",	
          port:"4006", 
          database:"prueba", 
          user:"temporal1", 
          pass:"Lima5349"]*/
GroovyShell shell = new GroovyShell()
def script = shell.parse(new File('params.groovy'))
params = script.connection()
println params

import groovy.sql.Sql

def queryDB(pQuery) {
    def printColNames = { meta -> 
        (1..meta.columnCount).each { print meta.getColumnLabel(it).padRight(20)+"|" }
        println()
        (1..meta.columnCount).each { print "--------------------" }
        println()
    }
    def printRow = { row ->
        row.toRowResult().values().each{ print it.toString().padRight(20)+"|" }
        println()
    }
    sql = Sql.newInstance("jdbc:mariadb://$params.server:$params.port/$params.database",params.get("user"),params["pass"],"org.mariadb.jdbc.Driver")
    sql.eachRow(pQuery,[],printColNames,1,10,printRow)
    sql.close()
}


queryDB('SELECT DISTINCT batch_id FROM srp_report_cache')