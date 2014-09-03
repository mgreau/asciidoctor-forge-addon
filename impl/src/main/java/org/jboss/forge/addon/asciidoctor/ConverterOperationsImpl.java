package org.jboss.forge.addon.asciidoctor;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.plugins.Configuration;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElement;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.furnace.services.Imported;

public class ConverterOperationsImpl implements ConverterOperations
{
   @Inject
   private FacetFactory facetFactory;
   
   @Override
   public boolean setup(String execId, Project project, Converter converter, boolean isGemMandatory)
   {
      if (project != null)
      {
         MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);
         CoordinateBuilder asciidoctorCoordinate = createAsciidoctorCoordinate();
         if (facet.hasPlugin(asciidoctorCoordinate))
         {
            MavenPlugin asciidoctorPlugin = facet.getPlugin(asciidoctorCoordinate);

            ConfigurationBuilder configuration = ConfigurationBuilder.create();
            configuration.createConfigurationElement("backend").setText(converter.getBackend());
            ConfigurationElementBuilder attrs = configuration.createConfigurationElement("attributes");

            for (Map.Entry<String, String> attr : converter.getAttributes().entrySet())
            {
            	attrs = attrs.addChild(attr.getKey()).setText(attr.getValue());
            }
            ExecutionBuilder execution = ExecutionBuilder.create()
                     .setId(execId != null ? execId : converter.getId())
                     .setPhase("generate-sources")
                     .addGoal("process-asciidoc")
                     .setConfig(configuration);

            final MavenPlugin asciidoctorPluginUpdate = MavenPluginBuilder.create(asciidoctorPlugin).addExecution(
                     execution);
            facet.updatePlugin(asciidoctorPluginUpdate);

            // Manage GEM DEPS
            if (isGemMandatory)
            {
                Iterable<AsciidoctorGemFacet> facets = facetFactory.createFacets(project,
                		AsciidoctorGemFacet.class);
               for (AsciidoctorGemFacet metaModelFacet : facets)
               {
                  if (facetFactory.install(project, metaModelFacet))
                  {
                     break;
                  }
               }
            }

            return true;
         }

      }
      return false;
   }

   private boolean isConverterAlreadyPresent(Project project, Converter converter)
   {
      MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);
      CoordinateBuilder dependency = createAsciidoctorCoordinate().setVersion(null);
      MavenPlugin plugin = facet.getPlugin(dependency);
      if (plugin.listExecutions().size() > 0)
      {
         Configuration config = plugin.listExecutions().get(0).getConfig();
         if (config.hasConfigurationElement("backend"))
         {
            ConfigurationElement element = config.getConfigurationElement("backend");
            return element.getText().equals(converter.getBackend());
         }
      }
      return false;
   }

   private CoordinateBuilder createAsciidoctorCoordinate()
   {
      return CoordinateBuilder.create()
               .setGroupId("org.asciidoctor")
               .setArtifactId("asciidoctor-maven-plugin");
   }

}
