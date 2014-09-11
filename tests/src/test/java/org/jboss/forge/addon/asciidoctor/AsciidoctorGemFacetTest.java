package org.jboss.forge.addon.asciidoctor;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorDiagramFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AsciidoctorGemFacetTest
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

   @Before
   public void setUp()
   {
      project = projectHelper.createJavaLibraryProject();
   }

   @Test
   public void testAsciidoctorDiagramGemInstallation() throws Exception
   {
      projectHelper.installAsciidoctorDiagramGem(project);

      DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);

      // rubygems repository
      assertTrue(dependencyFacet.hasRepository("http://rubygems-proxy.torquebox.org/releases"));

      // dependency
      assertTrue("Dependency " + AsciidoctorDiagramFacet.ASCIIDOCTOR_DIAGRAM + " should have been added",
               dependencyFacet.hasDirectDependency(AsciidoctorDiagramFacet.ASCIIDOCTOR_DIAGRAM));

      // plugin
      MavenPluginFacet pluginFacet = project.getFacet(MavenPluginFacet.class);
      CoordinateBuilder c = CoordinateBuilder.create().setGroupId("de.saumya.mojo").setArtifactId("gem-maven-plugin")
               .setVersion("1.0.5");

      assertTrue("Plugin " + c + " should have been added",
               pluginFacet.hasPlugin(c));
      
      CoordinateBuilder admp = CoordinateBuilder.create().setGroupId("or.asciidoctor").setArtifactId("asciidoctor-maven-plugin")
               .setVersion("1.5.0");
      assertTrue("Plugin " + admp + " should have been added",
               pluginFacet.hasPlugin(admp));
      assertTrue(pluginFacet.getPlugin(admp).getConfig().hasConfigurationElements());
      assertTrue(pluginFacet.getPlugin(admp).getConfig().listConfigurationElements().size() > 1);
   }

}