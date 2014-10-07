package org.jboss.forge.addon.asciidoctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.asciidoctor.converters.HTML5Converter;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.maven.plugins.Configuration;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AsciidoctorFacetTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:maven"),
            @AddonDependency(name = "org.jboss.forge.addon.asciidoctor:asciidoctor-forge-addon")
   })
   public static ForgeArchive getDeployment()
   {
      return ShrinkWrap.create(ForgeArchive.class)
               .addBeansXML()
               .addClass(ProjectHelper.class)
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:projects"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:maven"),
                        AddonDependencyEntry.create("org.jboss.forge.addon.asciidoctor:asciidoctor-forge-addon")
               );
   }

   private Project project;

   @Inject
   FacetFactory facetFactory;

   @Inject
   private ProjectHelper projectHelper;

   @Inject
   private ConverterOperations converterOps;

   private String asciidoctorVersion;
   private String asciidoctorMavenPluginVersion;

   @Before
   public void setUp()
   {
      project = projectHelper.createJavaLibraryProject();
      asciidoctorVersion = "1.6.0";
      asciidoctorMavenPluginVersion = "1.5.0";
   }

   @Test
   public void testAsciidoctorMPInstallation() throws Exception
   {
      projectHelper.installAsciidoctor(project);
      MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);
      MavenPlugin asciidoctorPlugin = facet.getPlugin(CoordinateBuilder
               .create("org.asciidoctor:asciidoctor-maven-plugin"));

      assertTrue("AsciidoctorFacet should have been added.", project.hasFacet(AsciidoctorFacet.class));
      assertNotNull("Plugin asciidoctor should have been added.", asciidoctorPlugin);
      assertNotNull("Plugin asciidoctor should have been added with a version.", asciidoctorPlugin.getCoordinate()
               .getVersion());
      assertEquals("Plugin asciidoctor should have been added with the default versionf.",
               asciidoctorMavenPluginVersion, asciidoctorPlugin.getCoordinate().getVersion());
   }

   @Test
   public void testAsciidoctorMPConfiguration() throws Exception
   {
      projectHelper.installAsciidoctor(project);
      MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);

      Converter html5Converter = new HTML5Converter();
      html5Converter.setAttribute("toc", "left");
      html5Converter.useAsciidoctorDiagram(false);
      converterOps.setup("id-test", project, html5Converter, asciidoctorVersion);

      MavenPlugin asciidoctorPlugin = facet.getPlugin(CoordinateBuilder
               .create("org.asciidoctor:asciidoctor-maven-plugin"));
      assertEquals("Plugin asciidoctor should have been configured with attributes.", 1, asciidoctorPlugin
               .listExecutions().size());

      // Check configuration
      Configuration asciidoctorConfig = asciidoctorPlugin.listExecutions().get(0).getConfig();
      assertEquals("html5",
               asciidoctorConfig.getConfigurationElement("backend").getText());
      assertEquals("left",
               asciidoctorConfig.getConfigurationElement("attributes").getChildByName("toc").getText());
   }

   @Test
   public void testAsciidoctorMPDependency() throws Exception
   {
      projectHelper.installAsciidoctor(project);
      MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);

      Converter html5Converter = new HTML5Converter();
      html5Converter.setAttribute("toc", "left");
      html5Converter.useAsciidoctorDiagram(false);
      converterOps.setup("id-test", project, html5Converter, asciidoctorVersion);

      MavenPlugin asciidoctorPlugin = facet.getPlugin(CoordinateBuilder
               .create("org.asciidoctor:asciidoctor-maven-plugin"));

      // Check asciidoctorj dependency
      String adjVersion = "";
      for (Dependency d : asciidoctorPlugin.getDirectDependencies())
      {
         if ("asciidoctorj".equals(d.getCoordinate().getArtifactId()))
         {
            adjVersion = d.getCoordinate().getVersion();
            break;
         }
      }
      assertEquals(asciidoctorVersion, adjVersion);
   }

}