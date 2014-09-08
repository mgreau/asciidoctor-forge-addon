package org.jboss.forge.addon.asciidoctor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
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
   public void testAsciidoctorGemInstallation() throws Exception
   {
      projectHelper.installAsciidoctorGem(project);

      DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
      DependencyBuilder wrongDependency = DependencyBuilder.create("javax.ejb:ejb-api");
      DependencyBuilder correctDependency = DependencyBuilder
               .create("gem:asciidoctor-diagram");
      assertFalse("Dependency " + wrongDependency + " should not have been added",
               dependencyFacet.hasEffectiveManagedDependency(wrongDependency));
      assertTrue("Dependency " + correctDependency + " should have been added",
               dependencyFacet.hasEffectiveManagedDependency(correctDependency));
    
   }

}