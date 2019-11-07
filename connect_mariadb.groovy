
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

import java.sql.*

	private Connection getMariaDBConnection(String url, String userName,
			String password) throws SQLException {
		Connection connection = DriverManager.getConnection(url+"?user="+userName+"&password="+password);
		return connection;
	}

    public void executeQuery(String sql) throws Exception {
        
        try (Connection connection = getMariaDBConnection("jdbc:mariadb://$params.server:$params.port/$params.database", params.get("user"),params["pass"])) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    println rs.getString(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

executeQuery("SELECT DISTINCT batch_id FROM srp_report_cache ")
