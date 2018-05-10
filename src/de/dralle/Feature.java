package de.dralle;

import java.util.HashSet;
import java.util.Set;

public class Feature {
	private Set<String> values;

	public Set<String> getValues() {
		return values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean addValue(String v) {
		return values.add(v);
	}

	private String name;

	public Feature() {
		values = new HashSet<>();
	}

	public Feature(String name) {
		this.name = name;
		values = new HashSet<>();
	}
}
