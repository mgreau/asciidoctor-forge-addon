package org.jboss.forge.addon.asciidoctor;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.converters.HTML5Converter;
import org.jboss.forge.addon.asciidoctor.converters.PDFConverter;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;

public class ConverterOperationsImpl implements ConverterOperations
{
   @Inject
   private FacetFactory facetFactory;

   @Override
   public boolean setup(String execId, Project project, Converter converter, String asciidoctorVersion)
   {
      if (project != null)
      {
         // Manage GEM DEPS
         if (converter.isGemRequired())
         {
            Iterable<AsciidoctorGemFacet> facets = facetFactory.createFacets(project,
                     AsciidoctorGemFacet.class);
            // TODO : diagram with PDF ?
            for (AsciidoctorGemFacet gemFacet : facets)
            {
               if (converter instanceof PDFConverter && gemFacet instanceof AsciidoctorPDFFacetImpl
                        && facetFactory.install(project, gemFacet))
               {
                  break;
               }

               if (converter instanceof HTML5Converter && gemFacet instanceof AsciidoctorDiagramFacetImpl
                        && facetFactory.install(project, gemFacet))
               {
                  break;
               }
            }
         }

         MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);
         Coordinate asciidoctorCoordinate = createAsciidoctorCoordinate();
         if (facet.hasPlugin(asciidoctorCoordinate))
         {
            MavenPlugin asciidoctorPlugin = facet.getPlugin(asciidoctorCoordinate);

            Coordinate asciidoctorCoordinateWithVersion = createAsciidoctorCoordinate().setVersion(asciidoctorVersion);
            
            ConfigurationBuilder configuration = ConfigurationBuilder.create();
            for (Map.Entry<String, String> element : converter.getConfiguration().entrySet())
            {
               if (element.getValue() != null)
               {
                  configuration.createConfigurationElement(element.getKey()).setText(element.getValue());
               }
            }

            // attributes configuration
            ConfigurationElementBuilder attrs = configuration.createConfigurationElement("attributes");

            for (Map.Entry<String, String> attr : converter.getAttributes().entrySet())
            {
               if (attr.getValue() != null)
               {
                  attrs.addChild(attr.getKey()).setText(attr.getValue());
               }
            }
            ExecutionBuilder execution = ExecutionBuilder.create()
                     .setId(execId != null ? execId : converter.getId())
                     .setPhase("generate-sources")
                     .addGoal("process-asciidoc")
                     .setConfig(configuration);

            final MavenPlugin asciidoctorPluginUpdate = MavenPluginBuilder.create(asciidoctorPlugin).addExecution(
                     execution).setCoordinate(asciidoctorCoordinateWithVersion);
            facet.updatePlugin(asciidoctorPluginUpdate);

            return true;
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
