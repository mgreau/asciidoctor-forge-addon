package org.jboss.forge.addon.asciidoctor;

import java.util.List;

import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.dependencies.util.NonSnapshotDependencyFilter;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
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
   protected Coordinate getAsciidoctorMPCoordinateWithLatestVersion()
   {
      return getAsciidoctorMPLatestVersion(createAsciidoctorMPCoordinate());
   }

   protected CoordinateBuilder createAsciidoctorMPCoordinate()
   {
      return CoordinateBuilder.create()
               .setGroupId("org.asciidoctor")
               .setArtifactId("asciidoctor-maven-plugin");
   }

   protected Coordinate getAsciidoctorMPLatestVersion(Coordinate dependency)
   {
      DependencyFacet dependencyFacet = getFaceted().getFacet(DependencyFacet.class);
      Coordinate result = dependency;
      List<Coordinate> versions = dependencyFacet.resolveAvailableVersions(DependencyQueryBuilder.create(dependency)
               .setFilter(new NonSnapshotDependencyFilter()));
      if (versions.size() > 0)
      {
         result = versions.get(versions.size() - 1);
      }
      else
      {
    	  //Add default value for the plugin version
    	  result = CoordinateBuilder.create(result).setVersion("1.5.0");
      }
      
      return result;
   }

}