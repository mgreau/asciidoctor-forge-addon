package org.jboss.forge.addon.asciidoctor.ui.setup;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.Converter;
import org.jboss.forge.addon.asciidoctor.ConverterOperations;
import org.jboss.forge.addon.asciidoctor.ui.AbstractAsciidoctorCommand;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;

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
      Boolean useAsciidoctorDiagram = (Boolean) uiContext.getAttributeMap().get("useAsciidoctorDiagram");
      String execId = mavenExecutionId.getValue();
      
      //Manage attributes
      converter.setAttribute(sourceHighlighter.getName(), sourceHighlighter.getValue());
      converter.setAttribute(toc.getName(), toc.getValue());
      
      converterOperations.setup(execId, project, converter, useAsciidoctorDiagram);
      return Results.success("Converter "+ converter.getName()  + " is configured (gem -> " + useAsciidoctorDiagram + ").");

   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      final UIContext uiContext = builder.getUIContext();
      Converter converter = (Converter) uiContext.getAttributeMap().get(Converter.class);
      
      builder.add(sourceHighlighter).add(toc).add(mavenExecutionId);

   }

   @Override
   protected boolean isProjectRequired()
   {
      return true;
   }

}