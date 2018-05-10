/**
 * 
 */
package de.dralle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nils
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Feature> allFeatures=new ArrayList<>();
		File f=new File(".\\testdata\\ID3_Beispieldaten.csv");
		if(f.exists()) {
			System.out.println("File exists");
			BufferedReader br=null;
			try {
				br=new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(br!=null) {
				System.out.println("File loaded");
			}
			String csvDelimiter=",";
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				
			}
			String[] features=line.split(csvDelimiter);
			List<String[]> featureVectors=new ArrayList<>();
			while(line!=null) {
				
				try {
					line=br.readLine();
				
				} catch (IOException e) {
					line=null;
				}
				if(line!=null) {
						featureVectors.add(line.split(csvDelimiter));
				}
			}
			try {
				br.close();
			} catch (IOException e) {
			
			}
			System.out.println(features.length+" features");
			System.out.println(featureVectors.size()+" feature vectors");
			for (int i = 0; i < features.length; i++) {
				Feature feature=new Feature(features[i]);
				allFeatures.add(feature);
				for (String[] strings : featureVectors) {					
					feature.addValue(strings[i]);
				}
			}
		}

	}

}
