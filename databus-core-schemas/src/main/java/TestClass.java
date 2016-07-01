import java.util.Map;

import com.linkedin.databus2.schemas.FileSystemSchemaRegistryService;


public class TestClass {
	
	public static void main(String args[]){
		try{
		    FileSystemSchemaRegistryService.Config configBuilder = new FileSystemSchemaRegistryService.Config();
		    configBuilder.setFallbackToResources(true);
		    configBuilder.setSchemaDir("/kishore/cloudberrie/CDC/resources");

		    FileSystemSchemaRegistryService.StaticConfig config = configBuilder.build();
		    FileSystemSchemaRegistryService service = FileSystemSchemaRegistryService.build(config);

		    Map<Short,String > fakeSchemas =
		        service.fetchAllSchemaVersionsBySourceName("com.linkedin.events.example.fake.FakeSchema");
//		    Assert.assertNotNull(fakeSchemas);
//		    Assert.assertTrue(2 <= fakeSchemas.size());
		    
		    
		    String str="kishore,kumar";
		    
		    String[] strs= str.split("["+","+"]");
		    System.out.println(strs[0]);
		    
		    System.out.println(fakeSchemas.size());

		    String personSchema = service.fetchLatestSchemaBySourceName("example.or_test.student");
		    System.out.println(personSchema);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
