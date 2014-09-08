package org.jboss.forge.addon.asciidoctor.ui.setup;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.Converter;
import org.jboss.forge.addon.asciidoctor.converters.HTML5Converter;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.asciidoctor.ui.AbstractAsciidoctorCommand;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.resource.URLResource;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
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

public class AsciidoctorSetupWizardImpl extends AbstractAsciidoctorCommand implements AsciidoctorSetupWizard
{
   @Inject
   @WithAttributes(shortName = 'n', label = "AsciiDocFile", required = true, defaultValue = "manual.adoc")
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
   private Imported<AsciidoctorGemFacet> asciidoctorGemFacet;

   @Inject
   private HTML5Converter defaultConverter;

   @Inject
   private FacetFactory facetFactory;

   @Inject
   TemplateFactory templateFactory;

   @Inject
   ResourceFactory resourceFactory;

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
      builder.add(asciiDocFile).add(useAsciidoctorDiagram).add(converter).add(docWriter);
   }

   @Override
   public Result execute(final UIExecutionContext context) throws Exception
   {
      applyUIValues(context.getUIContext());

      Project project = getSelectedProject(context);
      AsciidoctorFacet facet = asciidoctorFacet.get();
      if (facetFactory.install(project, facet))
      {
         createAsciiDocFile(context);
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

   private void createAsciiDocFile(final UIExecutionContext context) throws Exception
   {
      FileResource<?> asciiDocFileResource = getAsciiDocFileResource(context.getUIContext(), asciiDocFile.getValue());
      Resource<URL> templateAsciiDocFile = resourceFactory.create(
               getClass().getResource("/templates/example-manual.ftl")).reify(URLResource.class);
      Template template = templateFactory.create(templateAsciiDocFile, FreemarkerTemplate.class);
      Map<String, Object> templateContext = new HashMap<>();
      templateContext.put("docWriter", docWriter.getValue());
      asciiDocFileResource.createNewFile();
      asciiDocFileResource.setContents(template.process(templateContext));
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
      attributeMap.put("asciiDocFile", asciiDocFile.getValue());
      attributeMap.put("useAsciidoctorDiagram", useAsciidoctorDiagram.getValue());
      attributeMap.put(Converter.class, converter.getValue());
      attributeMap.put("docWriter", docWriter.getValue());
   }

   private FileResource<?> getAsciiDocFileResource(
            UIContext context, String adocFileName)
   {
      ResourcesFacet facet = getSelectedProject(context).getFacet(ResourcesFacet.class);
      FileResource<?> resource = facet.getResource(".." + File.separator + "asciidoc" + File.separator + adocFileName);
      return resource;
   }

}