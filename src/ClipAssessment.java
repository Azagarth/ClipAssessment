
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClipAssessment implements TransactionCommands {
	
	public void add(String user_id, String transaction_json){
		
		String unique_id = UUID.randomUUID().toString();	//auto generated id for transaction_id	
		JSONParser parser = new JSONParser();
		JSONObject json_obj = null;
		
		try {
			json_obj = (JSONObject)parser.parse(transaction_json);
			json_obj.put("user_id", user_id);
			json_obj.put("transaction_id", unique_id);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
			
		// saves the cmd json input into a txt file who's name is the unique identifier
		try (FileWriter file = new FileWriter("C:/Users/gregl/workspace/Clip/"+unique_id+".txt")) 
		{
			file.write(json_obj.toJSONString());
			System.out.println(json_obj);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void show(String user_id, String transaction_id){
		FileReader reader = null;
		JSONParser parser = new JSONParser();
		JSONObject json_obj = null;
		
		// see if the txt file exists that contains the transaction_id
		try {
			reader = new FileReader("C:/Users/gregl/workspace/Clip/"+transaction_id+".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			json_obj = (JSONObject)parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String value = (String) json_obj.get("user_id"); //finds the user_id from the txt file 
		
		// compares our cmd line user_id to that found in the txt file containing the transaction_id
		// if equal will print out the json
		// if not equal will print not found
		if(value.equals(user_id)){
			System.out.println(json_obj);		
		}
		else{
			System.out.println("Transaction not found");		
		}
	}
	
	public void list(String user_id){
		
		File folder = new File("C:/Users/gregl/workspace/Clip/"); // just looks in this directory for files
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> user_entries = new ArrayList<>();

		// checks if its a file and is a .txt
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
		    	FileReader reader = null;
				JSONParser parser = new JSONParser();
				JSONObject json_obj = null;
				
				try {
					reader = new FileReader("C:/Users/gregl/workspace/Clip/"+file.getName());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				try {
					json_obj = (JSONObject)parser.parse(reader);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				String value = (String) json_obj.get("user_id"); //finds the user_id from the txt file 
				
				if(value.equals(user_id)){
					user_entries.add(json_obj.toString());
					//System.out.println(file.getName());
				}
		    }
		}
		
		// prints off the array of transactions that pertain to user_id
		Collections.sort(user_entries);
		for (String s : user_entries){
		    System.out.println(s);
		}
	}
	
	public void sum(String user_id){
		File folder = new File("C:/Users/gregl/workspace/Clip/"); // just looks in this directory for files
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> user_entries = new ArrayList<String>();

		// checks if its a file and is a .txt
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
		    	FileReader reader = null;
				JSONParser parser = new JSONParser();
				JSONObject json_obj = null;
				
				try {
					reader = new FileReader("C:/Users/gregl/workspace/Clip/"+file.getName());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				try {
					json_obj = (JSONObject)parser.parse(reader);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				String value = (String) json_obj.get("user_id"); //finds the user_id from the txt file 
				
				if(value.equals(user_id)){
					user_entries.add(json_obj.get("amount").toString());
				}
		    }
		}
		
		// sums up the values of each transaction
		Double sum = 0.0;
		for (String s : user_entries){
		    sum += Double.parseDouble(s);
		}
		
		//printout of user_id and sum
		System.out.println("{ \"user_id\": "+ user_id +", \"sum\": " +sum + " }");
	}
	
	public static void main(String[] args) {

		TransactionCommands tc = new ClipAssessment();
		
		// checks to see if add/list/sum was called or defaults to show transaction
		if(args[1].equals("add")){
			tc.add(args[0],args[2]);
		}	
		else if(args[1].equals("list")){
			tc.list(args[0]);
		}
		else if(args[1].equals("sum")){
			tc.sum(args[0]);
		}
		else{
			tc.show(args[0], args[1]);
		}
	}
	
}
