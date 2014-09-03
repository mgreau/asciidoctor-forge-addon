package org.jboss.forge.addon.asciidoctor;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.dependencies.util.NonSnapshotDependencyFilter;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.plugins.Configuration;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElement;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginAdapter;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.resource.ResourceFactory;

/**
 * Implementation of {@link AsciidoctorFacet}
 * 
 * @author <a href="maxime.greau@gmail.com">Maxime Gréau</a>
 */
@FacetConstraints({
         @FacetConstraint(MavenPluginFacet.class),
         @FacetConstraint(DependencyFacet.class)
})
public class AsciidoctorFacetImpl extends AbstractAsciidoctorFacet implements AsciidoctorFacet
{
   @Inject
   private ResourceFactory factory;

   public AsciidoctorFacetImpl()
   {
   }

   @Override
   public boolean install()
   {
      addAsciidoctorPlugin();

      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return asciidoctorConfigured();
   }

   private void addAsciidoctorPlugin()
   {
      MavenPluginFacet facet = getFaceted().getFacet(MavenPluginFacet.class);
      CoordinateBuilder asciidoctorCoordinate = createAsciidoctorCoordinate();
      if (facet.hasPlugin(asciidoctorCoordinate))
      {
         return;
      }
      Coordinate versioned = getLatestVersion(asciidoctorCoordinate);

      ConfigurationBuilder configuration = ConfigurationBuilder.create();
      configuration.createConfigurationElement("sourceDirectory").setText("src/main/asciidoc");

      final MavenPlugin asciidoctorPlugin = MavenPluginBuilder.create()
               .setCoordinate(versioned).setConfiguration(configuration);
      facet.addPlugin(asciidoctorPlugin);

   }

   private CoordinateBuilder createAsciidoctorCoordinate()
   {
      return CoordinateBuilder.create()
               .setGroupId("org.asciidoctor")
               .setArtifactId("asciidoctor-maven-plugin");
   }

   private boolean asciidoctorConfigured()
   {
      CoordinateBuilder dependency = createAsciidoctorCoordinate().setVersion(null);
      MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);
      if (pluginFacet.hasPlugin(dependency))
      {
        return true;
      }
      return false;
   }

   private Coordinate getLatestVersion(Coordinate dependency)
   {
      DependencyFacet dependencyFacet = getFaceted().getFacet(DependencyFacet.class);
      Coordinate result = dependency;
      List<Coordinate> versions = dependencyFacet.resolveAvailableVersions(DependencyQueryBuilder.create(dependency)
               .setFilter(new NonSnapshotDependencyFilter()));
      if (versions.size() > 0)
      {
         result = versions.get(versions.size() - 1);
      }
      return result;
   }

   // TODO : handle copy-resources for resources outside src/main/asciidoc
   private void modifyResourcesPlugin()
   {
      Coordinate resourcesDependency = CoordinateBuilder.create()
               .setGroupId("org.apache.maven.plugins")
               .setArtifactId("maven-resources-plugin");
      MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);
      final MavenPluginAdapter resources;
      if (pluginFacet.hasPlugin(resourcesDependency))
      {
         resources = new MavenPluginAdapter(pluginFacet.getPlugin(resourcesDependency));
      }
      else
      {
         resources = new MavenPluginAdapter(MavenPluginBuilder.create().setCoordinate(resourcesDependency));
      }
      Configuration config = resources.getConfig();
   }

}