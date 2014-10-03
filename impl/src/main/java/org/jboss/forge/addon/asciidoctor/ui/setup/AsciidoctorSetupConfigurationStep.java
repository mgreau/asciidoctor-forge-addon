package org.jboss.forge.addon.asciidoctor.ui.setup;

import java.util.Iterator;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.Converter;
import org.jboss.forge.addon.asciidoctor.ConverterOperations;
import org.jboss.forge.addon.asciidoctor.attributes.Attribute;
import org.jboss.forge.addon.asciidoctor.attributes.AttributeConverter;
import org.jboss.forge.addon.asciidoctor.ui.AbstractAsciidoctorCommand;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UIInputMany;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class AsciidoctorSetupConfigurationStep extends AbstractAsciidoctorCommand implements UIWizardStep
{
   @Inject
   @WithAttributes(label = "Source Highlighter", required = false)
   private UIInput<String> sourceHighlighter;

   @Inject
   @WithAttributes(label = "Table of Content ?", required = false)
   private UIInput<String> toc;

   @Inject
   @WithAttributes(label = "Maven execution ID", required = false)
   private UIInput<String> mavenExecutionId;
   
   @Inject
   @WithAttributes(shortName = 'a', label = "Attributes", required = false,
            description = "The asciidoctor attributes configuration to be added [icons=font imagesdir=/images]")
   private UIInputMany<Attribute> attributes;

   @Inject
   private ConverterOperations converterOperations;

   @Override
   public NavigationResult next(UINavigationContext arg0) throws Exception
   {
      // No next step
      return null;
   }

   @Override
   public Metadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass()).name("Asciidoctor: Converter Settings")
               .description("Configure your converter settings");
   }

   @Override
   public Result execute(UIExecutionContext context) throws Exception
   {
      UIContext uiContext = context.getUIContext();
      Project project = getSelectedProject(uiContext);
      Converter converter = (Converter) uiContext.getAttributeMap().get(Converter.class);
      converter.useAsciidoctorDiagram((Boolean) uiContext.getAttributeMap().get("useAsciidoctorDiagram"));
      String execId = mavenExecutionId.getValue();
      String asciidoctorVersion = (String)uiContext.getAttributeMap().get("asciidoctorVersion");

      // Manage attributes
      converter.setAttribute(sourceHighlighter.getName(), sourceHighlighter.getValue());
      converter.setAttribute(toc.getName(), toc.getValue());
      
      for (Iterator<Attribute> attrs = attributes.getValue().iterator(); attrs.hasNext();)
      {
         Attribute attr = attrs.next();
         converter.setAttribute(attr.getName(), attr.getValue());
      }

      converterOperations.setup(execId, project, converter, asciidoctorVersion);
      return Results.success("Converter " + converter.getName() + " is configured - Asciidoctor version " + asciidoctorVersion + " - (gem configured -> "
               + converter.isGemRequired() + ").");
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      attributes.setValueConverter(new AttributeConverter());
      builder.add(attributes).add(sourceHighlighter).add(toc).add(mavenExecutionId);
   }

   @Override
   protected boolean isProjectRequired()
   {
      return true;
   }

}