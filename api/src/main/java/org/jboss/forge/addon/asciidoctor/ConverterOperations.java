package org.jboss.forge.addon.asciidoctor;

import org.jboss.forge.addon.projects.Project;

public interface ConverterOperations
{
   
   public boolean setup(String uniqueName, Project project, Converter converter, String asciidoctorVersion);
   
   

}
