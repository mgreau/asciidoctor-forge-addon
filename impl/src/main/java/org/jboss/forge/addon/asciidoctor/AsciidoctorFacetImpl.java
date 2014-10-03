package org.jboss.forge.addon.asciidoctor;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.plugins.Configuration;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginAdapter;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

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
      CoordinateBuilder asciidoctorCoordinate = createAsciidoctorMPCoordinate();
      if (facet.hasPlugin(asciidoctorCoordinate))
      {
         return;
      }
      Coordinate versioned = getAsciidoctorMPLatestVersion(asciidoctorCoordinate);

      ConfigurationBuilder configuration = ConfigurationBuilder.create();
      configuration.createConfigurationElement("sourceDirectory").setText("src/docs/asciidoc");

      final MavenPlugin asciidoctorPlugin = MavenPluginBuilder.create()
               .setCoordinate(versioned).setConfiguration(configuration);
      facet.addPlugin(asciidoctorPlugin);

   }

   private boolean asciidoctorConfigured()
   {
      CoordinateBuilder dependency = createAsciidoctorMPCoordinate().setVersion(null);
      MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);
      if (pluginFacet.hasPlugin(dependency))
      {
        return true;
      }
      return false;
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