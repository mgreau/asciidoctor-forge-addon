package org.jboss.forge.addon.asciidoctor;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;

/**
 * Implementation of {@link AbstractAsciidoctorFacet}
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
@FacetConstraints({
         @FacetConstraint(MavenPluginFacet.class)
})
public class AsciidoctorSiteFacetImpl extends AbstractAsciidoctorFacet implements AsciidoctorSiteFacet
{

   public AsciidoctorSiteFacetImpl()
   {

   }

   @Override
   public boolean install()
   {
      addAsciidoctorPluginToSitePlugin();

      return true;
   }

   private void addAsciidoctorPluginToSitePlugin()
   {
      MavenPluginFacet facet = getFaceted().getFacet(MavenPluginFacet.class);
      // check maven site plugin
      String versionMavenSitePlugin = "3.3";

      CoordinateBuilder mavenSitePluginCoordinate = createMavenSitePluginCoordinate();
      if (facet.hasPlugin(mavenSitePluginCoordinate))
      {
         versionMavenSitePlugin = facet.getPlugin(mavenSitePluginCoordinate).getCoordinate().getVersion();
      }

      final MavenPlugin mavenSitePlugin = MavenPluginBuilder.create()
               .setCoordinate(createMavenSitePluginCoordinate().setVersion(versionMavenSitePlugin))
               .addPluginDependency(
                        createAsciidoctorMavenPluginDependency());
      facet.updatePlugin(mavenSitePlugin);
   }

   private boolean asciidoctorConfiguredForMavenSitePlugin()
   {
      MavenPluginFacet facet = getFaceted().getFacet(MavenPluginFacet.class);

      CoordinateBuilder mavenSitePluginCoordinate = createMavenSitePluginCoordinate();
      if (facet.hasPlugin(mavenSitePluginCoordinate))
      {
         for (Dependency d : facet.getPlugin(mavenSitePluginCoordinate).getDirectDependencies())
         {
            if ("asciidoctor-maven-plugin".equals(d.getCoordinate().getArtifactId()))
            {
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public boolean isInstalled()
   {
      return asciidoctorConfiguredForMavenSitePlugin();
   }

   protected CoordinateBuilder createMavenSitePluginCoordinate()
   {
      return CoordinateBuilder.create()
               .setGroupId("org.apache.maven.plugins")
               .setArtifactId("maven-site-plugin");
   }

   private DependencyBuilder createAsciidoctorMavenPluginDependency()
   {
      return DependencyBuilder.create().setCoordinate(getAsciidoctorMPCoordinateWithLatestVersion());
   }

}