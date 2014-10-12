package org.jboss.forge.addon.asciidoctor.shell;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.maven.Maven;
import org.asciidoctor.Asciidoctor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.asciidoctor.ProjectHelper;
import org.jboss.forge.addon.asciidoctor.cli.AsciidoctorCliMonitor;
import org.jboss.forge.addon.shell.test.ShellTest;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.arquillian.maven.MavenModelResolver;
import org.jboss.forge.furnace.manager.maven.addon.MavenAddonDependencyResolver;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.furnace.util.OperatingSystemUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 */
@RunWith(Arquillian.class)
public class AsciidoctorCommandTest
{
   @Deployment
   @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:ui"),
            @AddonDependency(name = "org.jboss.forge.addon:shell-test-harness"),
            @AddonDependency(name = "org.jboss.forge.addon:resources"),
            @AddonDependency(name = "org.jboss.forge.furnace.container:cdi"),
            @AddonDependency(name = "org.jboss.forge.addon.asciidoctor:asciidoctor-forge-addon")
   })
   public static ForgeArchive getDeployment()
   {
      ForgeArchive archive = ShrinkWrap
               .create(ForgeArchive.class)
               .addClass(ProjectHelper.class)
               .addPackage(Asciidoctor.class.getPackage())
               .addBeansXML()
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.addon:ui"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:shell-test-harness"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:resources"),
                        AddonDependencyEntry.create("org.jboss.forge.furnace.container:cdi"),
                        AddonDependencyEntry.create("org.jboss.forge.addon.asciidoctor:asciidoctor-forge-addon")
               );

      return archive;
   }

   @Inject
   ShellTest shell;
   
   @Inject
   AsciidoctorCliMonitor cli;

   @Test
   public void testProcessFileToHTML() throws Exception
   {
      File tmpDir = OperatingSystemUtils.createTempDir();
      String file = "test.adoc";
      String fileGenerated = "test.html";
      shell.execute("cd " + tmpDir.getAbsolutePath(), 5, TimeUnit.SECONDS);

      File fileSourceAsciidoc = new File(tmpDir, file);
      //Assert.assertTrue(fileSourceAsciidoc.exists());
      
      shell.execute("asciidoctor  -a stylesheet! " + fileSourceAsciidoc.getAbsolutePath(), 25, TimeUnit.SECONDS);
      
      File fileTargetHTML = new File(tmpDir, fileGenerated);
      Assert.assertFalse(fileTargetHTML.exists());
   }

  
  
}