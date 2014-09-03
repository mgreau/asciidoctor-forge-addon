package org.jboss.forge.addon.asciidoctor;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.ResourceFacet;

/**
 * A base facet implementation for Facets which require Asciidoctor library APIs to be installed.
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 * 
 */
@FacetConstraints({
         @FacetConstraint(ResourceFacet.class),
         @FacetConstraint(MavenPluginFacet.class)
})
public abstract class AbstractAsciidoctorFacet extends AbstractFacet<Project>
{
   protected static final Dependency ASCIIDOCTOR_DIAGRAM =
            DependencyBuilder.create().setGroupId("rubygems").setArtifactId("asciidoctor-diagram").setVersion("1.2.0")
                     .setScopeType("provided").setClassifier("gem")
                     .addExclusion(CoordinateBuilder.create().setGroupId("rubygems").setArtifactId("asciidoctor"));

  
}