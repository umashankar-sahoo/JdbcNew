package com.learn.JSON;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {

	static void readJsonFile(FileReader jsonFile) throws FileNotFoundException, IOException {
		try {

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject)parser.parse(new FileReader("../JDBCTutorial/src/com/learn/resource/JsonEx.json"));

			// get a String from the JSON object
			String firstName = (String)jsonObject.get("firstname");
			System.out.println("The first name is: " + firstName);

			// get a number from the JSON object
			long id = (long)jsonObject.get("id");
			System.out.println("The id is: " + id);

			// get an array from the JSON object
			JSONArray languages = (JSONArray)jsonObject.get("languages");

			// take the elements of the json array
			for(int i = 0; i < languages.size(); i++) {
				System.out.println("The " + i + " element of the array: " + languages.get(i));
			}

			Iterator<?> i = languages.iterator();

			// take each value from the json array separately
			while(i.hasNext()) {
				JSONObject innerObj = (JSONObject)i.next();
				System.out.println("language " + innerObj.get("lang") + " with level " + innerObj.get("knowledge"));
			}
			// handle a structure into the json object
			JSONObject structure = (JSONObject)jsonObject.get("job");
			System.out.println("Into job structure, name: " + structure.get("name"));

		}
		catch(ParseException ex) {
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	static void writeJsonToFile() {
		JSONObject obj = new JSONObject();
		obj.put("name", "umashankar");
		obj.put("age", new Integer(24));

		JSONArray list = new JSONArray();
		list.add("msg 1");
		list.add("msg 2");
		list.add("msg 3");

		obj.put("messages", list);

		try (FileWriter file = new FileWriter("../JDBCTutorial/src/com/learn/resource/JsonEx2.json")) {
			file.write(obj.toJSONString());
			file.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.print(obj);
	}

	public static void main(String[] args) {
		try (FileReader jsonFile = new FileReader("../JDBCTutorial/src/com/learn/resource/JsonEx.json")) {
			readJsonFile(jsonFile);
			// writeJsonToFile();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}
	}
}