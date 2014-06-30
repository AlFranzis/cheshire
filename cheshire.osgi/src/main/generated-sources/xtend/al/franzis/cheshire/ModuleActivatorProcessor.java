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
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableAnnotationReference;
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
    MutableMethodDeclaration _findAnnotatedMethod = this.findAnnotatedMethod(annotatedClass, ModuleContextMethod.class);
    final String moduleContextMethodName = _findAnnotatedMethod.getSimpleName();
    MutableMethodDeclaration _findAnnotatedMethod_1 = this.findAnnotatedMethod(annotatedClass, ModuleStartMethod.class);
    final String startMethodName = _findAnnotatedMethod_1.getSimpleName();
    MutableMethodDeclaration _findAnnotatedMethod_2 = this.findAnnotatedMethod(annotatedClass, ModuleStopMethod.class);
    final String stopMethodName = _findAnnotatedMethod_2.getSimpleName();
    final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addParameter("bundleContext", osgiBundleContextType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append(moduleContextMethodName, "");
            _builder.append("( al.franzis.cheshire.osgi.OSGiModuleFramework.getInstance().getOrCreateModule( bundleContext ).getModuleContext() );");
            _builder.newLineIfNotEmpty();
            _builder.append(startMethodName, "");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("start", _function);
    final Procedure1<MutableMethodDeclaration> _function_1 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addParameter("bundleContext", osgiBundleContextType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append(stopMethodName, "");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("stop", _function_1);
  }
  
  private MutableMethodDeclaration findAnnotatedMethod(final MutableClassDeclaration annotatedClass, final Class<? extends Object> annotation) {
    Iterable<? extends MutableMethodDeclaration> _declaredMethods = annotatedClass.getDeclaredMethods();
    for (final MutableMethodDeclaration method : _declaredMethods) {
      Iterable<? extends MutableAnnotationReference> _annotations = method.getAnnotations();
      final Function1<MutableAnnotationReference,Boolean> _function = new Function1<MutableAnnotationReference,Boolean>() {
        public Boolean apply(final MutableAnnotationReference m) {
          AnnotationTypeDeclaration _annotationTypeDeclaration = m.getAnnotationTypeDeclaration();
          String _simpleName = _annotationTypeDeclaration.getSimpleName();
          String _simpleName_1 = annotation.getSimpleName();
          boolean _equals = Objects.equal(_simpleName, _simpleName_1);
          return Boolean.valueOf(_equals);
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
