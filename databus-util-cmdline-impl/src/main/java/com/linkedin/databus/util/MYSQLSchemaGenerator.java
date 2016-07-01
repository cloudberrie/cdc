package com.linkedin.databus.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.apache.avro.specific.SpecificCompiler;

public class MYSQLSchemaGenerator {

	static final String JDBCDB_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/or_test";
	static final String USER = "root";
	static final String PASS = "admin";
	static final String _avroOutDir="F:\\kishore\\cloudberrie\\CDC\\work";
	static final String _javaOutDir="F:\\kishore\\cloudberrie\\CDC\\work";
	static final String _namespace="com.linkedin.events.example.person";
	static final String _recordName="Person";
	static final String _primaryKey="ID";
	
	public MYSQLSchemaGenerator(){
		loadJdbcDrivers();
	}
	
	public static void main(String args[]) {

		MYSQLSchemaGenerator sg=new MYSQLSchemaGenerator();
		try{
			sg.generateSchema();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void loadJdbcDrivers() {
		// If a JDBC driver was specified then try to load it, otherwise load
		// Oracle and MySQL if available
		if (JDBCDB_DRIVER != null) {
			try {
				Class.forName(JDBCDB_DRIVER);
			} catch (ClassNotFoundException ex) {
				System.out.println("Could not load JDBC driver: "
						+ JDBCDB_DRIVER);
				return;
			}
		}
	}

	public void generateSchema() throws IOException {
		Connection con = null;

		try {
			con = getConnection();

			String owner="or_test";
			String table=_recordName;

			if (_avroOutDir == null) {
				System.out
						.println("Avro schema will not be saved (use -avroOutDir if you want to save it).");
			}
			if (_javaOutDir == null) {
				System.out
						.println("Java files will not be generated (use -javaOutDir if you want to generate them).");
			}

			TableTypeInfo ti = (TableTypeInfo) new TypeInfoFactory()
					.getTypeInfo(con, owner, table, 0, 0, _primaryKey);

			String topRecordAvroName = _recordName + "_V" ;
			String namespace = _namespace;
			String topRecordDatabaseName = _recordName;

			FieldToAvro fa = new FieldToAvro();
			String schema = fa.buildAvroSchema(namespace, topRecordAvroName,
					topRecordDatabaseName, null, ti);
			System.out.println("Generated Schema:\n" + schema);

			if (_avroOutDir != null || _javaOutDir != null) {
				// We will write the schema out to a file. If an output
				// directory was specified then we will
				// write it there. Otherwise we write to a temp file that is
				// deleted on exit.
				File avroOutFile;
				if (_avroOutDir != null) {
					avroOutFile = new File(_avroOutDir, _namespace + "."
							+ _recordName + ".avsc");
					System.out
							.println("Avro schema will be saved in the file: "
									+ avroOutFile.getAbsolutePath());
				} else {
					avroOutFile = File.createTempFile(getClass().getName(),
							null);
					avroOutFile.deleteOnExit();
				}

				// Write the schema to a file.
				PrintWriter pw = new PrintWriter(new FileWriter(avroOutFile));
				pw.println(schema);
				pw.flush();
				pw.close();

				if (_javaOutDir != null) {
					File javaOutFile=new File(_javaOutDir+"\\java_1.java");
					
					System.out
							.println("Generating Java files in the directory: "
									+ javaOutFile.getAbsolutePath());
					SpecificCompiler.compileSchema(avroOutFile, javaOutFile);
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex);
			ex.printStackTrace();
		} finally {
			SchemaUtils.close(con);
		}
	}

	public Connection getConnection() throws SQLException {
		try {

			Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
			return con;
		} catch (SQLException ex) {
			System.out.println("Could not connect to database: " + DB_URL);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
	}

}
