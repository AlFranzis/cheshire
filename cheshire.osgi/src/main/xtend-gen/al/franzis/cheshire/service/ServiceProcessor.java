package al.franzis.cheshire.service;

import al.franzis.cheshire.service.IServiceDefinition;
import al.franzis.cheshire.service.Service;
import com.google.common.base.Objects;
import java.lang.annotation.Annotation;
import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
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
        Path _targetFolder = context.getTargetFolder(filePath);
        String _qualifiedName = clazz.getQualifiedName();
        String _replace = _qualifiedName.replace(".", "/");
        String _plus = (_replace + ".xml");
        final Path file = _targetFolder.append(_plus);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _builder.newLine();
        _builder.append("<scr:component xmlns:scr=\"http://www.osgi.org/xmlns/scr/v1.1.0\" name=\"");
        String _qualifiedName_1 = clazz.getQualifiedName();
        _builder.append(_qualifiedName_1, "");
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("   \t\t\t\t");
        _builder.append("<implementation class=\"");
        String _qualifiedName_2 = clazz.getQualifiedName();
        _builder.append(_qualifiedName_2, "   \t\t\t\t");
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
  
  private Class<? extends IServiceDefinition> getServiceDefinition(final ClassDeclaration annotatedClass, final Class<? extends Object> annotation) {
    Class<? extends IServiceDefinition> _xblockexpression = null;
    {
      Class<? extends ClassDeclaration> _class = annotatedClass.getClass();
      Annotation[] _annotations = _class.getAnnotations();
      final Function1<Annotation,Boolean> _function = new Function1<Annotation,Boolean>() {
        public Boolean apply(final Annotation a) {
          Class<? extends Annotation> _class = a.getClass();
          String _simpleName = _class.getSimpleName();
          String _simpleName_1 = annotation.getSimpleName();
          boolean _equals = Objects.equal(_simpleName, _simpleName_1);
          return Boolean.valueOf(_equals);
        }
      };
      Annotation _findFirst = IterableExtensions.<Annotation>findFirst(((Iterable<Annotation>)Conversions.doWrapArray(_annotations)), _function);
      final Service serviceAnnotation = ((Service) _findFirst);
      Class<? extends IServiceDefinition> _definition = serviceAnnotation.definition();
      _xblockexpression = (_definition);
    }
    return _xblockexpression;
  }
}
