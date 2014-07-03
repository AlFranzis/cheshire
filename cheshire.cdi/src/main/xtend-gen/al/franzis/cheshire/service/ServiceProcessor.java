package al.franzis.cheshire.service;

import al.franzis.cheshire.service.Service;
import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ServiceProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
    for (final ClassDeclaration clazz : annotatedSourceElements) {
      {
        CompilationUnit _compilationUnit = clazz.getCompilationUnit();
        final Path filePath = _compilationUnit.getFilePath();
        final Path projectPath = context.getProjectFolder(filePath);
        final Path osgiInfPath = projectPath.append("OSGI-INF");
        final boolean success = context.mkdir(osgiInfPath);
        String _simpleName = clazz.getSimpleName();
        String _plus = (_simpleName + ".xml");
        final Path file = osgiInfPath.append(_plus);
        this.parseServiceDefinition(clazz, Service.class);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _builder.newLine();
        _builder.append("<scr:component xmlns:scr=\"http://www.osgi.org/xmlns/scr/v1.1.0\" name=\"");
        String _qualifiedName = clazz.getQualifiedName();
        _builder.append(_qualifiedName, "");
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("   \t\t\t\t");
        _builder.append("<implementation class=\"");
        String _qualifiedName_1 = clazz.getQualifiedName();
        _builder.append(_qualifiedName_1, "   \t\t\t\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
        _builder.append("   \t\t\t\t");
        _builder.append("<service>");
        _builder.newLine();
        _builder.append("      \t\t\t\t");
        _builder.append("<provide interface=\"s\"/>");
        _builder.newLine();
        _builder.append("   \t\t\t\t");
        _builder.append("</service>");
        _builder.newLine();
        _builder.append("   \t\t\t\t");
        _builder.append("<reference bind=\"addPlugin\" cardinality=\"0..n\" interface=\"al.franzis.cheshire.test.osgi.service.IPlugin\" name=\"IPlugin\" policy=\"static\"/>");
        _builder.newLine();
        _builder.append("</scr:component>");
        _builder.newLine();
        context.setContents(file, _builder);
      }
    }
  }
  
  private void parseServiceDefinition(final ClassDeclaration annotatedClass, final Class<? extends Object> annotation) {
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference,Boolean> _function = new Function1<AnnotationReference,Boolean>() {
      public Boolean apply(final AnnotationReference a) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = a.getAnnotationTypeDeclaration();
        String _simpleName = _annotationTypeDeclaration.getSimpleName();
        String _simpleName_1 = annotation.getSimpleName();
        boolean _equals = Objects.equal(_simpleName, _simpleName_1);
        return Boolean.valueOf(_equals);
      }
    };
    final AnnotationReference serviceAnnotation = IterableExtensions.findFirst(_annotations, _function);
    Object _value = serviceAnnotation.getValue("name");
    final String serviceName = ((String) _value);
    InputOutput.<String>println(("Service name: " + serviceName));
  }
}
