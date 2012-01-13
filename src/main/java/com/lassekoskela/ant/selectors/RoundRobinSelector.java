package com.lassekoskela.ant.selectors;

import static java.lang.Integer.parseInt;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;

public class RoundRobinSelector extends BaseExtendSelector {
  private int counter;
  private int numberOfPartitions;
  private int selectedPartition;

  @Override
  public void setParameters(Parameter[] parameters) {
    super.setParameters(parameters);
    for (Parameter p : parameters) {
      if ("partitions".equalsIgnoreCase(p.getName())) {
        numberOfPartitions = parseInt(p.getValue());
      } else if ("selected".equalsIgnoreCase(p.getName())) {
        selectedPartition = parseInt(p.getValue());
      } else {
        throw new BuildException("Unknown: " + p.getName());
      }
    }
  }

  public void verifySettings() {
    super.verifySettings();
    if (numberOfPartitions <= 0) {
      throw new BuildException("Must specify 'partitions'");
    }
    if (selectedPartition <= 0) {
      throw new BuildException("Must specify 'selected'");
    }
    if (selectedPartition > numberOfPartitions) {
      throw new BuildException("Partition must be between 1 and " 
          + numberOfPartitions);
    }
  }

  public boolean isSelected(File dir, String name, File path) {
    counter = (counter % numberOfPartitions) + 1;
    return counter == selectedPartition;
  }
}
