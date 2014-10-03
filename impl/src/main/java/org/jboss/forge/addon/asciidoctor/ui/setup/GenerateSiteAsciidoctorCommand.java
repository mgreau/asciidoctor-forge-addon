package org.jboss.forge.addon.asciidoctor.ui.setup;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.asciidoctor.ui.AbstractAsciidoctorCommand;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.furnace.services.Imported;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class GenerateSiteAsciidoctorCommand extends AbstractAsciidoctorCommand
{
   @Inject
   private Imported<AsciidoctorSiteFacet> asciidoctorSiteFacet;

   @Inject
   private FacetFactory facetFactory;

   @Override
   public Metadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass())
               .name("Asciidoctor: Generate Site")
               .description("Generate SiteWeb with Asciidoctor in your project");
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
   }

   @Override
   public Result execute(final UIExecutionContext context) throws Exception
   {
      return Results.fail("Not yet implemented.");
   }

   @Override
   protected boolean isProjectRequired()
   {
      return true;
   }

}