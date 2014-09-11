package org.jboss.forge.addon.asciidoctor;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorDiagramFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorPDFFacet;
import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorSiteFacet;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.parser.java.projects.JavaProjectType;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;

/**
 * Helps with the configuration of a project
 *
 * @author <a href="greaumaxime@google.fr">Maxime Gréau</a>
 */
public class ProjectHelper
{
   @Inject
   private ProjectFactory projectFactory;

   @Inject
   private FacetFactory facetFactory;

   @Inject
   private JavaProjectType javaProjectType;


   /**
    * Creates a project installing the required facets from {@link JavaProjectType#getRequiredFacets()}
    */
   public Project createJavaLibraryProject()
   {
      return projectFactory.createTempProject(javaProjectType.getRequiredFacets());
   }

   /**
    * Installs the {@link AsciidoctorGemFacet} facet
    */
   public AsciidoctorGemFacet installAsciidoctorDiagramGem(Project project)
   {
      return facetFactory.install(project, AsciidoctorDiagramFacet.class);
   }
   
   /**
    * Installs the {@link AsciidoctorGemFacet} facet
    */
   public AsciidoctorGemFacet installAsciidoctorPDFGem(Project project)
   {
      return facetFactory.install(project, AsciidoctorPDFFacet.class);
   }

   /**
    * Installs the {@link AsciidoctorFacet} facet
    */
   public AsciidoctorFacet installAsciidoctor(Project project)
   {
      return facetFactory.install(project, AsciidoctorFacet.class);
   }
   
   /**
    * Installs the {@link AsciidoctorSiteFacet} facet
    */
   public AsciidoctorSiteFacet installAsciidoctorSite(Project project)
   {
      return facetFactory.install(project, AsciidoctorSiteFacet.class);
   }

   public Project refreshProject(Project project)
   {
      return projectFactory.findProject(project.getRoot());
   }
}