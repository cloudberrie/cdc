package com.ui.schemaGen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.avro.specific.SpecificCompiler;
import org.codehaus.jackson.map.ObjectMapper;

import com.linkedin.databus.util.FieldToAvro;
import com.linkedin.databus.util.SchemaUtils;
import com.linkedin.databus.util.TableTypeInfo;
import com.linkedin.databus.util.TypeInfoFactory;
import com.ui.schemaGen.bean.DBSourceSchemaDef;
import com.ui.schemaGen.bean.TableSourceSchemaDef;
import com.ui.util.Environment;

public class MYSQLSchemaGenerator {

	static final String JDBCDB_DRIVER = "jdbc.driver";
	static final String DB_URL = "jdbc.url";
	static final String USER = "database.userName";
	static final String PASS = "database.password";
	String _avroOutDir;
	String _javaOutDir;
	String _namespace;
	// String _primaryKey="ID";
	String databaseName;
	String databaseType;
//	static Properties props = new Properties();
	String indexSchemaRegFileName = "index.schemas_registry";
	String clientRegisterEvents = "client.properties";

	File file;
	OutputStream output;
	OutputStream clientEventsoutSteam;
	OutputStream dbSchemaSourceStream;


	public MYSQLSchemaGenerator(String databaseType, String databaseName,
			String avroOutDirpath, String javaOutDirPath) {

		this.databaseType = databaseType;
		this.databaseName = databaseName;
		_avroOutDir = avroOutDirpath;
		_javaOutDir = javaOutDirPath;

		loadJdbcDrivers();
	}

	public void loadJdbcDrivers() {
		// If a JDBC driver was specified then try to load it, otherwise load
		// Oracle and MySQL if available
		String jdbcDriver = Environment.getProperty(databaseType + "."
				+ JDBCDB_DRIVER);
		if (jdbcDriver != null) {
			try {
				Class.forName(jdbcDriver);
			} catch (ClassNotFoundException ex) {
				System.out.println("Could not load JDBC driver: " + jdbcDriver);
				return;
			}
		}
	}

	public void generateSchema() throws IOException {
		Connection con = null;

		try {
			con = getConnection();

			// String table=_tableName;

			List<String> tables = getAllDbTables(con);
			file = new File(_avroOutDir, indexSchemaRegFileName);
			if (file.exists()) {
				file.delete();
			}
			output = new FileOutputStream(file, true);

			File eventsFile = new File(_avroOutDir, clientRegisterEvents);

			if (eventsFile.exists()) {
				eventsFile.delete();
			}
			clientEventsoutSteam = new FileOutputStream(eventsFile, true);

			File dbSchemaSrcFile = new File(_avroOutDir, "sources_database.json"
					);
			if (dbSchemaSrcFile.exists()) {
				dbSchemaSrcFile.delete();
			}
			dbSchemaSourceStream = new FileOutputStream(dbSchemaSrcFile, true);
			StringBuffer eventsRegister = new StringBuffer();

			String DBSourceSchemaUri = MessageFormat.format(
					(String) Environment.getProperty(databaseType + ".uri"), databaseName,
					Environment.getProperty(databaseType + ".ip"),
					Environment.getProperty(databaseType + ".port"),
					Environment.getProperty(databaseType + ".port"));

			DBSourceSchemaDef dbSoruceSchemaDec = new DBSourceSchemaDef(					
					Environment.getProperty(databaseType + ".serverId"),
					databaseName,
					DBSourceSchemaUri);
			List<TableSourceSchemaDef> tableSources = new ArrayList<TableSourceSchemaDef>();

			dbSoruceSchemaDec.setSources(tableSources);

			eventsRegister.append("source.events=");
			int sourceId = 1;
			for (String table : tables) {
				generateAvro(con, table, eventsRegister, tableSources, ++sourceId);
			}
			clientEventsoutSteam.write(eventsRegister.substring(0,
					eventsRegister.length() - 1).getBytes());
			
			prepareSourceEventFile(dbSoruceSchemaDec,dbSchemaSourceStream);
		} catch (SQLException ex) {
			System.out.println(ex);
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			SchemaUtils.close(con);
		}
	}

	public Connection getConnection() throws SQLException {

		String dbUrl = Environment.getProperty(databaseType + "." + DB_URL)
				+ databaseName;
		String userName = Environment.getProperty(databaseType + "." + USER);
		String password = Environment.getProperty(databaseType + "." + PASS);
		try {

			Connection con = DriverManager.getConnection(dbUrl, userName,
					password);
			return con;
		} catch (SQLException ex) {
			System.out.println("Could not connect to database: " + dbUrl);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
	}

	public void generateAvro(Connection con, String table,
			StringBuffer eventsRegister, List<TableSourceSchemaDef> tableSrcs,
			Integer sourceId) throws Exception {
		if (_avroOutDir == null) {
			System.out
					.println("Avro schema will not be saved (use -avroOutDir if you want to save it).");
		}
		if (_javaOutDir == null) {
			System.out
					.println("Java files will not be generated (use -javaOutDir if you want to generate them).");
		}

		String primaryKey = SchemaUtils.toCamelCase(getPrimaryKey(con, table));
		TableTypeInfo ti = (TableTypeInfo) new TypeInfoFactory().getTypeInfo(
				con, databaseName, table, 0, 0, primaryKey);

		

		String topRecordAvroName = table + "_V";
		String namespace = databaseType + "." + databaseName + "." + table;
		String topRecordDatabaseName = table;
		tableSrcs.add(prepareTabSrcDef(sourceId, namespace,table));

		FieldToAvro fa = new FieldToAvro();
		String schema = fa.buildAvroSchema(namespace, topRecordAvroName,
				topRecordDatabaseName, null, ti);
		System.out.println("Generated Schema:\n" + schema);

		if (_avroOutDir != null || _javaOutDir != null) {
			// We will write the schema out to a file. If an output
			// directory was specified then we will
			// write it there. Otherwise we write to a temp file that is
			// deleted on exit.
			String version = ".1";
			File avroOutFile;
			if (_avroOutDir != null) {

				avroOutFile = new File(_avroOutDir, namespace +  version
						+ ".avsc");

				output.write(avroOutFile.getName().getBytes());
				output.write("\n".getBytes("UTF-8"));
				System.out.println("Avro schema will be saved in the file: "
						+ avroOutFile.getAbsolutePath());
				eventsRegister.append(namespace);
				eventsRegister.append(",");
			} else {
				avroOutFile = File.createTempFile(getClass().getName(), null);
				avroOutFile.deleteOnExit();
			}

			// Write the schema to a file.
			PrintWriter pw = new PrintWriter(new FileWriter(avroOutFile));
			pw.println(schema);
			pw.flush();
			pw.close();

			if (_javaOutDir != null) {
				File javaOutFile = new File(_javaOutDir + "\\java_1.java");

				System.out.println("Generating Java files in the directory: "
						+ javaOutFile.getAbsolutePath());
				SpecificCompiler.compileSchema(avroOutFile, javaOutFile);
			}
		}
	}

	public List<String> getAllDbTables(Connection con) throws SQLException {
		ResultSet rs = null;
		DatabaseMetaData meta = con.getMetaData();
		ArrayList<String> tables = new ArrayList<String>();
		rs = meta.getTables(null, null, null, new String[] { "TABLE" });

		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			System.out.println("tableName=" + tableName);
			tables.add(tableName);
		}
		return tables;
	}

	public String getPrimaryKey(Connection con, String tableName)
			throws SQLException {
		DatabaseMetaData dm = con.getMetaData();
		ResultSet rs = dm.getPrimaryKeys("", "", tableName);
		String pkey = null;
		while (rs.next()) {
			pkey = rs.getString(4);
			System.out.println("primary key = " + pkey);
		}
		return pkey;
	}

	public TableSourceSchemaDef prepareTabSrcDef(int id, String namespace,String tableName) {

		TableSourceSchemaDef tabSrcSchemaDef = new TableSourceSchemaDef(id,
				namespace, databaseName + "." + tableName);
		return tabSrcSchemaDef;

	}


	public void prepareSourceEventFile(DBSourceSchemaDef dbSourceSchemaDef,
			OutputStream out) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValueAsString(dbSourceSchemaDef);
		objectMapper.writeValue(out, dbSourceSchemaDef);
	}

}
