package de.dralle;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DecisionTreeNode {
	private final static Logger LOGGER = Logger.getLogger(DecisionTreeNode.class.getName());
	public DecisionTreeNode() {
		branches=new HashMap<>();
	}
	public DecisionTreeNode(Feature decisionFeature) {
		branches=new HashMap<>();
		this.decisionFeature=decisionFeature;
	}
	private String terminatingValue=null;
	public String getTerminatingValue() {
		return terminatingValue;
	}
	public void setTerminatingValue(String terminatingValue) {
		this.terminatingValue = terminatingValue;
	}
	private Feature decisionFeature;
	public Feature getDecisionFeature() {
		return decisionFeature;
	}
	public boolean isTerminating() {
		return decisionFeature==null;
	}
	public void setDecisionFeature(Feature decisionFeature) {
		this.decisionFeature = decisionFeature;
	}
	private Map<String,DecisionTreeNode> branches;
	public Map<String, DecisionTreeNode> getBranches() {
		return branches;
	}
	public void addDecisionBranch(String value,DecisionTreeNode subTree) {
		LOGGER.log(Level.FINE,"Branch added for "+value);
		branches.put(value, subTree);
	}
}
