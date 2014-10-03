package org.jboss.forge.addon.asciidoctor.ui.setup;

import java.io.File;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.asciidoctor.ui.AbstractAsciidoctorCommand;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
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
   @WithAttributes(shortName = 'v', label = "Asciidoctor version (AsciidoctorJ)", required = true, defaultValue = "1.5.1")
   private UIInput<String> asciidoctorVersion;
   
   @Inject
   private Imported<AsciidoctorSiteFacet> asciidoctorSiteFacet;

   @Inject
   private FacetFactory facetFactory;

   @Override
   public Metadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass())
               .name("Asciidoctor: Generate Site")
               .description("Process AsciiDoc in a Maven site using the Asciidoctor Maven plugin.");
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      builder.add(asciidoctorVersion);
   }

   @Override
   public Result execute(final UIExecutionContext context) throws Exception
   {
      Project project = getSelectedProject(context);
      AsciidoctorSiteFacet facet = asciidoctorSiteFacet.get();
      if (facetFactory.install(project, facet))
      {
         createAsciiDocFile(context, getPath(), "Doc Writer");
         return Results.success("Asciidoctor configuration for Maven Site Plugin SUCCESS.");
      }
      return Results.fail("Could not configure Asciidoctor for the Maven Site Plugin.");
   }

   @Override
   protected boolean isProjectRequired()
   {
      return true;
   }
   
   private String getPath(){
      // src/site/asciidoc
      return ".." + File.separator + ".." + File.separator + "site"
               + File.separator + "asciidoc" + File.separator + "hello.adoc";
   }

}