package org.jboss.forge.addon.asciidoctor.ui.setup;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.Converter;
import org.jboss.forge.addon.asciidoctor.converters.HTML5Converter;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.asciidoctor.ui.AbstractAsciidoctorCommand;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.furnace.services.Imported;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class AsciidoctorSetupWizardImpl extends AbstractAsciidoctorCommand implements AsciidoctorSetupWizard
{
   @Inject
   @WithAttributes(shortName = 'v', label = "Asciidoctor version (AsciidoctorJ)", required = true, defaultValue = "1.5.1")
   private UIInput<String> asciidoctorVersion;

   @Inject
   @WithAttributes(shortName = 'n', label = "AsciiDoc file name", required = true, defaultValue = "manual.adoc")
   private UIInput<String> asciiDocFile;

   @Inject
   @WithAttributes(shortName = 'd', label = "Use the Asciidoctor Diagram project ?")
   private UIInput<Boolean> useAsciidoctorDiagram;

   @Inject
   @WithAttributes(shortName = 'c', label = "Asciidoctor converter to use")
   private UISelectOne<Converter> converter;

   @Inject
   @WithAttributes(shortName = 'w', label = "Writer name", required = true, defaultValue = "Doc Writer")
   private UIInput<String> docWriter;

   @Inject
   private Imported<AsciidoctorFacet> asciidoctorFacet;

   @Inject
   private HTML5Converter defaultConverter;

   @Inject
   private FacetFactory facetFactory;

   @Override
   public Metadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass())
               .name("Asciidoctor: Setup")
               .description("Setup Asciidoctor for the project documentation.");
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      initConverters();
      builder.add(asciidoctorVersion).add(asciiDocFile).add(useAsciidoctorDiagram).add(converter).add(docWriter);
   }

   @Override
   public Result execute(final UIExecutionContext context) throws Exception
   {
      applyUIValues(context.getUIContext());

      Project project = getSelectedProject(context);
      AsciidoctorFacet facet = asciidoctorFacet.get();
      if (facetFactory.install(project, facet))
      {
         createAsciiDocFile(context, getPath(), docWriter.getValue());
         return Results.success("Asciidoctor setup SUCCESS.");
      }

      return Results.fail("Could not install Asciidoctor.");
   }

   private void initConverters()
   {
      converter.setItemLabelConverter(new org.jboss.forge.addon.convert.Converter<Converter, String>()
      {
         @Override
         public String convert(Converter source)
         {
            return source != null ? source.getName() : null;
         }
      });
      converter.setDefaultValue(defaultConverter);
   }

   @Override
   protected boolean isProjectRequired()
   {
      return true;
   }

   @Override
   public NavigationResult next(UINavigationContext context) throws Exception
   {
      applyUIValues(context.getUIContext());
      return Results.navigateTo(AsciidoctorSetupConfigurationStep.class);
   }

   private void applyUIValues(final UIContext context)
   {
      Map<Object, Object> attributeMap = context.getAttributeMap();
      attributeMap.put("asciidoctorVersion", asciidoctorVersion.getValue());
      attributeMap.put("asciiDocFile", asciiDocFile.getValue());
      attributeMap.put("useAsciidoctorDiagram", useAsciidoctorDiagram.getValue());
      attributeMap.put(Converter.class, converter.getValue());
      attributeMap.put("docWriter", docWriter.getValue());
   }

   private String getPath()
   {
      // src/docs/asciidoc
      return ".." + File.separator + ".." + File.separator + "docs"
               + File.separator + "asciidoc" + File.separator + asciiDocFile.getValue();
   }

}