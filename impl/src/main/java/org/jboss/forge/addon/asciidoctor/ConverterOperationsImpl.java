package org.jboss.forge.addon.asciidoctor;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.converters.HTML5Converter;
import org.jboss.forge.addon.asciidoctor.converters.PDFConverter;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
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
         Coordinate asciidoctorMPCoordinate = createAsciidoctorMavenPluginCoordinate();
         if (facet.hasPlugin(asciidoctorMPCoordinate))
         {
            MavenPlugin asciidoctorMavenPlugin = facet.getPlugin(asciidoctorMPCoordinate);

            Coordinate asciidoctorMPCoordinateWithVersion = createAsciidoctorMavenPluginCoordinate().setVersion("1.5.0");

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

            final MavenPlugin asciidoctorMPluginUpdate= MavenPluginBuilder.create(asciidoctorMavenPlugin).addExecution(
                     execution).setCoordinate(asciidoctorMPCoordinateWithVersion)
                     // add the asciidoctorj dependency to match with the asciidoctor version provided
                     .addPluginDependency(createAsciidoctorJCoordinate(asciidoctorVersion));

            facet.updatePlugin(asciidoctorMPluginUpdate);

            return true;
         }

      }
      return false;
   }

   private DependencyBuilder createAsciidoctorJCoordinate(String asciidoctorVersion)
   {
      return DependencyBuilder.create(AsciidoctorFacet.ASCIIDOCTORJ).setVersion(asciidoctorVersion);
   }
   
   private CoordinateBuilder createAsciidoctorMavenPluginCoordinate()
   {
      return CoordinateBuilder.create()
               .setGroupId("org.asciidoctor")
               .setArtifactId("asciidoctor-maven-plugin");
   }

}
