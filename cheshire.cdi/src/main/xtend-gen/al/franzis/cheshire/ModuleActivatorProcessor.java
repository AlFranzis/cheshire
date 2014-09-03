package al.franzis.cheshire;

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
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ModuleActivatorProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final TypeReference cdiModuleActivatorType = context.newTypeReference("al.franzis.cheshire.cdi.ICDIModuleActivator");
    Iterable<? extends TypeReference> _implementedInterfaces = annotatedClass.getImplementedInterfaces();
    final Iterable<TypeReference> implInterfaces = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(Lists.<TypeReference>newArrayList(cdiModuleActivatorType)));
    annotatedClass.setImplementedInterfaces(implInterfaces);
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
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
