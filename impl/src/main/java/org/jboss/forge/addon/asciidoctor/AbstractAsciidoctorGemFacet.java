package org.jboss.forge.addon.asciidoctor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

/**
 * A base facet implementation for Facets which require Asciidoctor Gem libraries APIs to be installed.
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 * 
 */
@FacetConstraints({
         @FacetConstraint(MavenPluginFacet.class),
         @FacetConstraint(DependencyFacet.class)
})
public abstract class AbstractAsciidoctorGemFacet extends AbstractAsciidoctorFacet implements AsciidoctorGemFacet
{

   /**
    * Return a {@link Map} where KEY represents a {@link Dependency} to be installed if none of the VALUE
    * {@link Dependency} are installed.
    */
   protected abstract Map<Dependency, List<Dependency>> getRequiredDependencyOptions();

   protected abstract void addRequiredDependency();

   @Override
   public boolean install()
   {
      addGemRepository();
      addGemPlugin();
      addRequiredDependency();
      updateAsciidoctorPlugin();

      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return dependencyRequirementsMet();
   }

   protected boolean dependencyRequirementsMet()
   {
      DependencyFacet deps = origin.getFacet(DependencyFacet.class);
      for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions().entrySet())
      {
         boolean satisfied = false;
         for (Dependency dependency : group.getValue())
         {
            if (deps.hasEffectiveDependency(dependency))
            {
               satisfied = true;
               break;
            }
         }

         if (!satisfied)
            return false;
      }
      return true;
   }

   private void addGemPlugin()
   {
      final MavenPluginFacet facet = origin.getFacet(MavenPluginFacet.class);
      Coordinate gemCoordinate = CoordinateBuilder.create()
               .setGroupId("de.saumya.mojo")
               .setArtifactId("gem-maven-plugin").setVersion("1.0.5");

      if (!facet.hasPlugin(gemCoordinate))
      {
         final MavenPluginBuilder gmp = MavenPluginBuilder.create().setCoordinate(gemCoordinate);

         final ConfigurationBuilder configuration = ConfigurationBuilder.create();
         configuration.createConfigurationElement("jrubyVersion").setText("1.7.9");
         configuration.createConfigurationElement("gemHome").setText("${project.build.directory}/gems");
         configuration.createConfigurationElement("gemPath").setText("${project.build.directory}/gems");

         final ExecutionBuilder execution = ExecutionBuilder.create()
                  .addGoal("initialize")
                  .setConfig(configuration);
         gmp.addExecution(execution);

         facet.addPlugin(gmp);
      }
   }

   private void updateAsciidoctorPlugin()
   {
      final MavenPluginFacet facet = origin.getFacet(MavenPluginFacet.class);

      // FIXME final MavenPlugin adp = facet.getPlugin(getAsciidoctorCoordinateWithLatestVersion());
      final MavenPluginBuilder adp = MavenPluginBuilder.create().setCoordinate(
               getAsciidoctorMPCoordinateWithLatestVersion());

      ConfigurationBuilder configuration = ConfigurationBuilder.create();
      configuration.createConfigurationElement("gemPath").setText(
               "${project.build.directory}/gems-provided");
      // FIXME : bug , the updatePlugin doesn't work, we need to re-create the plugin
      configuration.createConfigurationElement("sourceDirectory").setText("src/docs/asciidoc");

      adp.setConfiguration(configuration);

      facet.updatePlugin(adp);
   }

   private void addGemRepository()
   {
      origin.getFacet(DependencyFacet.class).addRepository("RubyGems.org Proxy (Releases)",
               "http://rubygems-proxy.torquebox.org/releases");
   }

}