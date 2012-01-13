package com.lassekoskela.ant.selectors;

import static java.lang.String.valueOf;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
import org.junit.Before;
import org.junit.Test;

public class RoundRobinSelectorTest {

	private List<Parameter> params;

	@Before
	public void setUp() {
		params = new ArrayList<Parameter>();
	}

	@Test(expected = BuildException.class)
	public void requiresPartitions() {
		withParameter("partitions", 4);
		selector().verifySettings();
	}

	@Test(expected = BuildException.class)
	public void requiresSelectedPartition() {
		withParameter("selected", 2);
		selector().verifySettings();
	}

	@Test(expected = BuildException.class)
	public void selectedPartitionMustMakeSense() {
		withParameter("partitions", 4);
		withParameter("selected", 5);
		selector().verifySettings();
	}

	@Test
	public void acceptsValidConfiguration() {
		withParameter("partitions", 3);
		withParameter("selected", 1);
		selector().verifySettings();
	}

	@Test
	public void firstPartition() {
		withParameter("partitions", 3);
		withParameter("selected", 1);
		selectorRespondsWith(true, false, false, true, false, false);
	}

	@Test
	public void middlePartition() {
		withParameter("partitions", 3);
		withParameter("selected", 2);
		selectorRespondsWith(false, true, false, false, true, false);
	}

	@Test
	public void lastPartition() {
		withParameter("partitions", 3);
		withParameter("selected", 3);
		selectorRespondsWith(false, false, true, false, false, true);
	}

	private void selectorRespondsWith(boolean... values) {
		RoundRobinSelector s = selector();
		for (boolean value : values) {
			assertEquals(value, s.isSelected(null, "whatever", null));
		}
	}

	private void withParameter(String name, Object value) {
		Parameter p = new Parameter();
		p.setName(name);
		p.setValue(valueOf(value));
		params.add(p);
	}

	private RoundRobinSelector selector() {
		RoundRobinSelector selector = new RoundRobinSelector();
		Parameter[] p = params.toArray(new Parameter[params.size()]);
		selector.setParameters(p);
		return selector;
	}
}
