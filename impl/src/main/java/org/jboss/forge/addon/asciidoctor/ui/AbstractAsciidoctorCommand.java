package org.jboss.forge.addon.asciidoctor.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.resource.URLResource;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public abstract class AbstractAsciidoctorCommand extends AbstractProjectCommand
{
   @Inject
   private ProjectFactory projectFactory;
   
   @Inject
   TemplateFactory templateFactory;

   @Inject
   ResourceFactory resourceFactory;

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass()).category(Categories.create("Asciidoctor"));
   }

   @Override
   protected ProjectFactory getProjectFactory()
   {
      return projectFactory;
   }
   
   protected void createAsciiDocFile(final UIExecutionContext context, String path, String author) throws Exception
   {
      FileResource<?> asciiDocFileResource = getAsciiDocFileResource(context.getUIContext(), path);
      Resource<URL> templateAsciiDocFile = resourceFactory.create(
               getClass().getResource("/templates/example-manual.ftl")).reify(URLResource.class);
      Template template = templateFactory.create(templateAsciiDocFile, FreemarkerTemplate.class);
      Map<String, Object> templateContext = new HashMap<>();
      templateContext.put("docWriter", author);
      asciiDocFileResource.createNewFile();
      asciiDocFileResource.setContents(template.process(templateContext));
   }
   
   protected FileResource<?> getAsciiDocFileResource(
            UIContext context, String path)
   {
      ResourcesFacet facet = getSelectedProject(context).getFacet(ResourcesFacet.class);
      FileResource<?> resource = facet.getResource(path);
      return resource;
   }
}