package org.jboss.forge.addon.asciidoctor;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
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
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean isInstalled()
   {
      // TODO Auto-generated method stub
      return false;
   }

  

}