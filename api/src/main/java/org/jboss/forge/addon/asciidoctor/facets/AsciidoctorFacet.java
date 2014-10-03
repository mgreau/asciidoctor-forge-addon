package org.jboss.forge.addon.asciidoctor.facets;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.projects.ProjectFacet;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public interface AsciidoctorFacet extends ProjectFacet
{
   public static final Dependency ASCIIDOCTORJ =
            DependencyBuilder.create().setGroupId("org.asciidoctor").setArtifactId("asciidoctorj").setVersion("1.5.1");
}
