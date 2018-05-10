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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		List<Feature> allFeatures = new ArrayList<>();
		Feature resultFeature=new Feature();
		File f = new File(".\\testdata\\ID3_Beispieldaten.csv");
		if (f.exists()) {
			System.out.println("File exists");
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (br != null) {
				System.out.println("File loaded");
			}
			String csvDelimiter = ",";
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {

			}
			String[] features = line.split(csvDelimiter);
			List<String[]> featureVectors = new ArrayList<>();
			while (line != null) {

				try {
					line = br.readLine();

				} catch (IOException e) {
					line = null;
				}
				if (line != null) {
					featureVectors.add(line.split(csvDelimiter));
				}
			}
			try {
				br.close();
			} catch (IOException e) {

			}
			System.out.println(features.length + " features");
			System.out.println(featureVectors.size() + " feature vectors");
			for (int i = 0; i < features.length-1; i++) {
				Feature feature = new Feature(features[i]);
				allFeatures.add(feature);
				for (String[] strings : featureVectors) {
					feature.addValue(strings[i]);
				}
			}
			resultFeature.setName(features[features.length-1]);
			DecisionTreeNode id3 = buildID3(allFeatures, featureVectors);
		}

	}

	private static DecisionTreeNode buildID3(List<Feature> features, List<String[]> featureVectors) {
		double entropy=getFullEntropy(featureVectors);
		Feature f=getMostDecidingFeature(features,featureVectors);
		DecisionTreeNode dtn=new DecisionTreeNode(f);
		for (String value : f.getValues()) {
			dtn.addDecisionBranch(value, buildID3(buildFeatureSubset(features,f), buildFeatureVectorSubset(featureVectors,features,f)));
		}
		return dtn;
	}
	private static double getFullEntropy(List<String[]> featureVectors) {
		double entropy=0;
		int cntTotal=featureVectors.size();
		Map<String,Integer> possibleClassificationResultCounts=new HashMap<>();
		for (int i = 0; i < featureVectors.size(); i++) {
			String[] vector = featureVectors.get(i);
			//Last element in vector is assumed to be classification result
			String possibleClassificationResult = vector[vector.length-1];
			Integer cnt=0;
			if(possibleClassificationResultCounts.containsKey(possibleClassificationResult)) {
				cnt=possibleClassificationResultCounts.get(possibleClassificationResult);
			}
			cnt++;
			possibleClassificationResultCounts.put(possibleClassificationResult, cnt);
		}
		Set<String> keys = possibleClassificationResultCounts.keySet();
		for (String classResult : keys) {
			int cnt=possibleClassificationResultCounts.get(classResult);
			double frac=cnt/(double)cntTotal;
			entropy+=(-frac*log2(frac));
		}
		return entropy;
	}
	private static Map<String, Integer> getFeatureValueCounts(Feature f,List<String[]> featureVectors,List<Feature> allFeatures) {
		int featureIndex=allFeatures.indexOf(f);
		if(featureIndex>-1) {
			Map<String, Integer> featureValueCounts=new HashMap<>();
			for (String value : f.getValues()) {
				featureValueCounts.put(value, 0);
				for (int i = 0; i < featureVectors.size(); i++) {
					String[] vector = featureVectors.get(i);
					String featureValue=vector[featureIndex];
					if(value.equals(featureValue)) {
						Integer cnt = featureValueCounts.get(value);
						cnt++;
					}
					
				}
			}
			return featureValueCounts;
		}
		return null;
	}
	private static double getFeatureInformationGain(Feature f,List<String[]> featureVectors,List<Feature> allFeatures) {
		double fullDataSetEntropy=getFullEntropy(featureVectors);
		double gain=fullDataSetEntropy;
		Map<String, Integer> featureValueCnts = getFeatureValueCounts(f, featureVectors, allFeatures);
		for (String value : featureValueCnts.keySet()) {
			List<String[]> featureVectorsFiltered=filterFeatureVectorsForFeatureValue(featureVectors, allFeatures, f, value);
			gain-=(featureValueCnts.get(value)/(double)featureVectors.size())*getFullEntropy(featureVectorsFiltered);
		}
		return gain;
	}
	private static double log2(double x) {
		return Math.log(x)/Math.log(2);
	}

	private static List<String[]> buildFeatureVectorSubset(List<String[]> featureVectors, List<Feature> allFeatures,
			Feature fRemove) {

		int indexOfFeatureToRemove = allFeatures.indexOf(fRemove);
		if (indexOfFeatureToRemove > -1) {
			List<String[]> featureVectorsSubset = new ArrayList<>();
			for (String[] oldFeatureVector : featureVectors) {
				String[] newFeatureVector=new String[oldFeatureVector.length-1];
				featureVectorsSubset.add(
					newFeatureVector);
				for (int i = 0; i < newFeatureVector.length; i++) {
					if(i<indexOfFeatureToRemove) {
						newFeatureVector[i]=oldFeatureVector[i];
					}else {
						newFeatureVector[i]=oldFeatureVector[i+1];
					}
				}
			}
			return featureVectorsSubset;
		}
		return featureVectors;

	}
	private static List<String[]> filterFeatureVectorsForFeatureValue(List<String[]> featureVectors, List<Feature> allFeatures,
			Feature f,String value) {
		List<String[]> newFeatureVectors=new ArrayList<>();
		int indexOfFeature = allFeatures.indexOf(f);
		for (int i = 0; i < featureVectors.size(); i++) {
			if(featureVectors.get(i)[indexOfFeature].equals(value)) {
				newFeatureVectors.add(featureVectors.get(i));
			}
		}
		return newFeatureVectors;

	}

	private static List<Feature> buildFeatureSubset(List<Feature> allFeatures, Feature f) {
		List<Feature> subSet = new ArrayList<>();
		subSet.addAll(allFeatures);
		subSet.remove(f);
		return subSet;
	}

	private static Feature getMostDecidingFeature(List<Feature> features, List<String[]> featureVectors) {
		Feature objectivlyBestFeature=null;
		double featureInformationGain=0;
		for (int i = 0; i < features.size(); i++) {
			double gain = getFeatureInformationGain(features.get(i), featureVectors, features);
			if(gain>featureInformationGain) {
				objectivlyBestFeature=features.get(i);
				gain=featureInformationGain;
			}
		}
		return objectivlyBestFeature;
	}

}
