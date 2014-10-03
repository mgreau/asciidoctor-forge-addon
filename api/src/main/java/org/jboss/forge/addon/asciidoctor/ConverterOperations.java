package org.jboss.forge.addon.asciidoctor;

import org.jboss.forge.addon.projects.Project;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public interface ConverterOperations
{
   public boolean setup(String uniqueName, Project project, Converter converter, String asciidoctorVersion);
}
