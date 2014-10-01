package al.franzis.cheshire;

import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.ModuleStopMethod;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModuleActivatorProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    this.turnIntoOSGiActivator(annotatedClass, context);
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
  }
  
  public void turnIntoOSGiActivator(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final TypeReference osgiBundleActivatorType = context.newTypeReference("org.osgi.framework.BundleActivator");
    Iterable<? extends TypeReference> _implementedInterfaces = annotatedClass.getImplementedInterfaces();
    final Iterable<TypeReference> implInterfaces = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(Lists.<TypeReference>newArrayList(osgiBundleActivatorType)));
    annotatedClass.setImplementedInterfaces(implInterfaces);
    final TypeReference osgiBundleContextType = context.newTypeReference("org.osgi.framework.BundleContext");
    MutableMethodDeclaration _findAnnotatedMethod = this.findAnnotatedMethod(annotatedClass, ModuleStartMethod.class);
    final String startMethodName = _findAnnotatedMethod.getSimpleName();
    CompilationStrategy startMethodBody = null;
    final MutableMethodDeclaration moduleContextMethod = this.findAnnotatedMethod(annotatedClass, ModuleContextMethod.class);
    boolean _notEquals = (!Objects.equal(moduleContextMethod, null));
    if (_notEquals) {
      final String moduleContextMethodName = moduleContextMethod.getSimpleName();
      final CompilationStrategy _function = new CompilationStrategy() {
        public CharSequence compile(final CompilationStrategy.CompilationContext it) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("try {");
          _builder.newLine();
          _builder.append("      \t\t\t");
          _builder.append(moduleContextMethodName, "      \t\t\t");
          _builder.append("( al.franzis.cheshire.osgi.OSGiModuleFramework.getInstance().getOrCreateModule( bundleContext ).getModuleContext() );");
          _builder.newLineIfNotEmpty();
          _builder.append("      \t\t\t");
          _builder.append(startMethodName, "      \t\t\t");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
          _builder.append("} catch(Exception e) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("throw new RuntimeException(\"Exception while starting Activator\", e);");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          return _builder;
        }
      };
      startMethodBody = _function;
    } else {
      final CompilationStrategy _function_1 = new CompilationStrategy() {
        public CharSequence compile(final CompilationStrategy.CompilationContext it) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("try {");
          _builder.newLine();
          _builder.append("      \t\t\t");
          _builder.append(startMethodName, "      \t\t\t");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
          _builder.append("} catch(Exception e) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("throw new RuntimeException(\"Exception while starting Activator\", e);");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          return _builder;
        }
      };
      startMethodBody = _function_1;
    }
    final CompilationStrategy finalStartMethodBody = startMethodBody;
    MutableMethodDeclaration _findAnnotatedMethod_1 = this.findAnnotatedMethod(annotatedClass, ModuleStopMethod.class);
    final String stopMethodName = _findAnnotatedMethod_1.getSimpleName();
    final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addParameter("bundleContext", osgiBundleContextType);
        it.setBody(finalStartMethodBody);
      }
    };
    annotatedClass.addMethod("start", _function_2);
    final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addParameter("bundleContext", osgiBundleContextType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("try {");
            _builder.newLine();
            _builder.append(stopMethodName, "");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("} catch(Exception e) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("throw new RuntimeException(\"Exception while stopping Activator\", e);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("stop", _function_3);
  }
  
  private MutableMethodDeclaration findAnnotatedMethod(final MutableClassDeclaration annotatedClass, final Class<?> annotation) {
    Iterable<? extends MutableMethodDeclaration> _declaredMethods = annotatedClass.getDeclaredMethods();
    for (final MutableMethodDeclaration method : _declaredMethods) {
      Iterable<? extends AnnotationReference> _annotations = method.getAnnotations();
      final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
        public Boolean apply(final AnnotationReference m) {
          AnnotationTypeDeclaration _annotationTypeDeclaration = m.getAnnotationTypeDeclaration();
          String _simpleName = _annotationTypeDeclaration.getSimpleName();
          String _simpleName_1 = annotation.getSimpleName();
          return Boolean.valueOf(Objects.equal(_simpleName, _simpleName_1));
        }
      };
      boolean _exists = IterableExtensions.exists(_annotations, _function);
      if (_exists) {
        return method;
      }
    }
    return null;
  }
}
