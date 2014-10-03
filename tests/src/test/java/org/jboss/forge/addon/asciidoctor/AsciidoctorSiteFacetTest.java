package org.jboss.forge.addon.asciidoctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
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
public class AsciidoctorSiteFacetTest
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

   private String asciidoctorMavenPluginVersion;

   @Before
   public void setUp()
   {
      project = projectHelper.createJavaLibraryProject();
      asciidoctorMavenPluginVersion = "1.5.0";
   }

   @Test
   public void testAsciidoctorInstallationForSitePlugin() throws Exception
   {
      projectHelper.installAsciidoctorSite(project);
      MavenPluginFacet facet = project.getFacet(MavenPluginFacet.class);
      MavenPlugin mavenSitePlugin = facet.getPlugin(CoordinateBuilder
               .create("org.apache.maven.plugins:maven-site-plugin"));

      assertNotNull("maven-site-plugin should have been added.", mavenSitePlugin);
      assertTrue("AsciidoctorSiteFacet should have been added.", project.hasFacet(AsciidoctorSiteFacet.class));
      assertNotNull("maven-site-plugin  should have been added with a groupid.", mavenSitePlugin.getCoordinate()
               .getGroupId());
      assertNotNull("maven-site-plugin  should have been added with a version.", mavenSitePlugin.getCoordinate()
               .getVersion());

      // Check asciidoctor-maven-plugin dependency
      String ampVersion = "";
      boolean isDependencyInstalled = false;
      for (Dependency d : mavenSitePlugin.getDirectDependencies())
      {
         if ("asciidoctor-maven-plugin".equals(d.getCoordinate().getArtifactId()))
         {
            isDependencyInstalled = true;
            ampVersion = d.getCoordinate().getVersion();
            break;
         }
      }
      assertTrue("Plugin asciidoctor-maven-plugin should have been added.", isDependencyInstalled);
      assertEquals("Plugin asciidoctor-maven-plugin should have been added with the default version.",
               asciidoctorMavenPluginVersion, ampVersion);
   }
}