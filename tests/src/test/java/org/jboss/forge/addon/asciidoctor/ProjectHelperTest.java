package org.jboss.forge.addon.asciidoctor;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.projects.facets.PackagingFacet;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(Arquillian.class)
public class ProjectHelperTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:maven"),
            @AddonDependency(name = "org.jboss.forge.addon.asciidoctor:asciidoctor-forge-addon")
   })
   public static ForgeArchive getDeployment()
   {
      return ShrinkWrap
               .create(ForgeArchive.class)
               .addBeansXML()
               .addClass(ProjectHelper.class)
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:maven"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:projects"),
                        AddonDependencyEntry.create("org.jboss.forge.addon.asciidoctor:asciidoctor-forge-addon")
               );
   }

   @Inject
   private ProjectHelper projectHelper;

   @Test
   public void testInjection()
   {
      Assert.assertNotNull(projectHelper);
   }

   @Test
   public void testJavaLibraryProjectCreation()
   {
      Project project = projectHelper.createJavaLibraryProject();
      Assert.assertTrue(project.hasFacet(MetadataFacet.class));
      Assert.assertTrue(project.hasFacet(PackagingFacet.class));
      Assert.assertTrue(project.hasFacet(DependencyFacet.class));
      Assert.assertTrue(project.hasFacet(ResourcesFacet.class));
      Assert.assertTrue(project.hasFacet(JavaSourceFacet.class));
      Assert.assertTrue(project.hasFacet(JavaCompilerFacet.class));
      Assert.assertFalse(project.hasFacet(WebResourcesFacet.class));
   }

   @Test
   public void testAsciidoctorSetup()
   {
      Project project = projectHelper.createJavaLibraryProject();
      AsciidoctorFacet asciidoctor = projectHelper.installAsciidoctor(project);
      Assert.assertNotNull(asciidoctor);
      Assert.assertTrue(project.hasFacet(AsciidoctorFacet.class));
   }


}