package de.dralle;

import java.util.HashMap;
import java.util.Map;

public class DecisionTreeNode {
	public DecisionTreeNode() {
		branches=new HashMap<>();
	}
	public DecisionTreeNode(Feature decisionFeature) {
		branches=new HashMap<>();
		this.decisionFeature=decisionFeature;
	}
	private Feature decisionFeature;
	public Feature getDecisionFeature() {
		return decisionFeature;
	}
	public void setDecisionFeature(Feature decisionFeature) {
		this.decisionFeature = decisionFeature;
	}
	private Map<String,DecisionTreeNode> branches;
	public void addDecisionBranch(String value,DecisionTreeNode subTree) {
		branches.put(value, subTree);
	}
}
