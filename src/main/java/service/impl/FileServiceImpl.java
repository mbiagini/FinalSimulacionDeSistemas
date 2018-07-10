package service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import persistence.granular.Passenger;
import persistence.cellIndex.Particle;

public class FileServiceImpl {
	
	public <T> T getFromValidatedJson(String json, Type typeOfT) {
		Gson gson = new Gson();
		T object = null;
		try {
			object = gson.fromJson(json, typeOfT);
		} catch (JsonSyntaxException exception) {
			throw new RuntimeException(String.format("Error pasing json: %s", json));
		}
		return object;
	}

	public String readFileToString(File file) throws IOException {
		
		InputStream is = new FileInputStream(file);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
	
		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		
		buf.close();
		return sb.toString();
	}
	
	public void writeStringToFile(File file, String data) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error while reading from file " + file.getName());
		}
		pw.print(data);
		pw.close();
	}
	
	public void saveAllOvitoData(List<List<Passenger>> data) throws IOException {
	
		int instants = data.size();
		
		for(int i = 0; i < instants; i++) {
			saveOvitoData(data.get(i), i);
		}

	}
	
	public void saveOvitoData(List<Passenger> data, int i) throws IOException {
				
			String filename = String.format("src/main/resources/Files/Ovito/particles%07d.xyz", i);
			File file = new File(filename);
			PrintWriter pw = new PrintWriter(file);
			
			pw.println(data.size());
			pw.println("Animation file");
		
			for(int j = 0; j < data.size(); j++) {
				pw.println(j + " " + data.get(j).getX() + " " + data.get(j).getY());
			}
			
			pw.close();

	}

	public void changeJsonToOvitoForBox() throws JsonIOException, FileNotFoundException {
		for(int i = 0; i < 5900; i = i + 100) {
			Gson gson = new Gson();
			List<Particle> pList = null;
			Type listType = new TypeToken<ArrayList<Passenger>>(){}.getType();
			try {
				pList = gson.fromJson(new FileReader(
						String.format("src/main/resources/data/simulation/particles%07d.json", i)), 
						listType);
			} catch (JsonSyntaxException exception) {
				throw new RuntimeException("Error pasing json");
			}
			
			String filename = String.format("src/main/resources/data/ovito/particles%07d.xyz", i);
			File file = new File(filename);
			PrintWriter pw = new PrintWriter(file);
			
			pw.println(pList.size() + 4);
			pw.println("Animation file");
		
			for(int j = 0; j < pList.size(); j++) {
				pw.println(pList.get(j).getR() + " " + pList.get(j).getX() + " " + pList.get(j).getY() + " 1");
			}
			
//			//Surface particles
//			for(int k = 0; k <= 2; k += 0.1) {
//				pw.println("0.01 0 " + k + " 0");
//				pw.println("0.01 1 " + k + " 0");
//			}
//			for(int l = 0; l <= 1; l += 0.1) {
//				pw.println("0.01 " + l + " 0 0");
//			}
			
			pw.println("0.01 0 0 0");
			pw.println("0.01 1 0 0");
			pw.println("0.01 0 2 0");
			pw.println("0.01 1 2 0");
			
			pw.close();
			
			
		}
	}
	
	public void changeJsonToOvitoForSile() throws JsonIOException, FileNotFoundException {
		for(int i = 0; i <= 3400; i = i + 100) {
			Gson gson = new Gson();
			List<Particle> pList = null;
			Type listType = new TypeToken<ArrayList<Passenger>>(){}.getType();
			try {
				pList = gson.fromJson(new FileReader(
						String.format("src/main/resources/data/simulationSile/particles%07d.json", i)), 
						listType);
			} catch (JsonSyntaxException exception) {
				throw new RuntimeException("Error pasing json");
			}
			
			String filename = String.format("src/main/resources/data/ovitoSile/particles%07d.xyz", i);
			File file = new File(filename);
			PrintWriter pw = new PrintWriter(file);
			
			pw.println(pList.size() + 4);
			pw.println("Animation file");
		
			for(int j = 0; j < pList.size(); j++) {
				pw.println(pList.get(j).getR() + " " + pList.get(j).getX() + " " + pList.get(j).getY() + " 1");
			}
			
//			//Surface particles
//			for(int k = 0; k <= 2; k += 0.1) {
//				pw.println("0.01 0 " + k + " 0");
//				pw.println("0.01 1 " + k + " 0");
//			}
//			for(int l = 0; l <= 1; l += 0.1) {
//				pw.println("0.01 " + l + " 0 0");
//			}
			
			pw.println("0.01 0 0 0");
			pw.println("0.01 1 0 0");
			pw.println("0.01 0 2 0");
			pw.println("0.01 1 2 0");
			
			pw.close();
			
			
		}
	}

}
