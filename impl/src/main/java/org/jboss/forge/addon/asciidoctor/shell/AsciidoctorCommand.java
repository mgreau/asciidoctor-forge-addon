package org.jboss.forge.addon.asciidoctor.shell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.cli.AsciidoctorCliMonitor;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.shell.ui.AbstractShellCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.input.UIInputMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;

public class AsciidoctorCommand extends AbstractShellCommand
{
   @Inject
   ResourceFactory resourceFactory;

   @Inject
   AsciidoctorCliMonitor asciidoctorCli;

   @Inject
   @WithAttributes(label = "Arguments", required = true)
   private UIInputMany<String> arguments;

   private String target = null;

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      builder.add(arguments);
   }

   @Override
   public void validate(UIValidationContext validator)
   {
      //TODO Check that there is at least one file argument
   }

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), AsciidoctorCommand.class).name("asciidoctor")
               .description("Process file with asciidoctor command.");
   }

   @Override
   public Result execute(UIExecutionContext context) throws Exception
   {
      UISelection<Resource<?>> initialSelection = context.getUIContext().getInitialSelection();
      Resource<?> directory = initialSelection.get();
      Iterator<String> argIterator = arguments.getValue().iterator();
      //target = argIterator.next();
      final String[] args = checkArguments(argIterator);
      Resource<?> sourceResource = resolveResource(directory, target);
      if (isDirectory(sourceResource))
      {
         throw new RuntimeException("Cannot process directory type: " + sourceResource.getClass().getSimpleName());
      }
      else if (isFile(sourceResource))
      {
         asciidoctorCli.monitorResource(sourceResource, args);
         try
         {
            asciidoctorCli.processResource(sourceResource, args);
            return Results.success();
         }
         catch (Exception ex)
         {
            return Results.fail("Cannot process resource with Asciidoctor");
         }
      }
      else
      {
         throw new RuntimeException("cannot process resource type: " + sourceResource.getClass().getSimpleName());
      }
   }
   
   private String[] checkArguments(final Iterator<String> args)
   {
      String[] argsForCLI = {};
      List<String> list = new ArrayList<>();
      for(Iterator<String> argIt = args; args.hasNext();){
         String arg = argIt.next();
         if (arg.contains("."))
         {
            //this is the adoc file to process
            target = arg;
            list.add(arg);
            break;
         }else{
            list.add("-a");
            list.add(arg);
         }
      }
      return list.toArray(argsForCLI);
   }

   private Resource<?> resolveResource(Resource<?> resource, final String target)
   {
      List<Resource<?>> results = resource.resolveChildren(target);
      if (results.size() > 1)
      {
         throw new RuntimeException("ambiguous target file name: " + target);
      }
      else if (results.isEmpty())
      {
         throw new RuntimeException("no resources found under path: " + target);
      }
      else
      {
         return results.get(0);
      }
   }

   private boolean isFile(Resource<?> source)
   {
      return source instanceof FileResource;
   }

   private boolean isDirectory(Resource<?> source)
   {
      return source instanceof DirectoryResource;
   }

}