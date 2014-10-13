package al.franzis.cheshire.cdi.proc;

import al.franzis.cheshire.api.nativecode.ICDIRuntimeLibPathProvider;
import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.cdi.rt.CDIModuleFramework;
import al.franzis.cheshire.cdi.rt.ICDIModuleManifest;
import al.franzis.cheshire.cdi.rt.NativeLibManager;
import com.google.common.base.Objects;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Helpers {
  public static List<MethodDeclaration> findAnnotatedMethod(final ClassDeclaration annotatedClass, final Class<?> annotation) {
    List<MethodDeclaration> _xblockexpression = null;
    {
      Iterable<? extends MethodDeclaration> _declaredMethods = annotatedClass.getDeclaredMethods();
      final Function1<MethodDeclaration, Boolean> _function = new Function1<MethodDeclaration, Boolean>() {
        public Boolean apply(final MethodDeclaration m) {
          Iterable<? extends AnnotationReference> _annotations = m.getAnnotations();
          final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
            public Boolean apply(final AnnotationReference a) {
              AnnotationTypeDeclaration _annotationTypeDeclaration = a.getAnnotationTypeDeclaration();
              String _simpleName = _annotationTypeDeclaration.getSimpleName();
              String _simpleName_1 = annotation.getSimpleName();
              return Boolean.valueOf(Objects.equal(_simpleName, _simpleName_1));
            }
          };
          return Boolean.valueOf(IterableExtensions.exists(_annotations, _function));
        }
      };
      Iterable<? extends MethodDeclaration> _filter = IterableExtensions.filter(_declaredMethods, _function);
      final List<? extends MethodDeclaration> annotatedMethods = IterableExtensions.toList(_filter);
      _xblockexpression = ((List<MethodDeclaration>) annotatedMethods);
    }
    return _xblockexpression;
  }
  
  public static MutableMethodDeclaration findAnnotatedMethod(final MutableClassDeclaration annotatedClass, final Class<?> annotation) {
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
  
  public final static String CLASSNAME_ICDIModuleManifest = ICDIModuleManifest.class.getCanonicalName();
  
  public final static String ClASSNAME_ICDIRuntimeLibPathProvider = ICDIRuntimeLibPathProvider.class.getCanonicalName();
  
  public final static String CLASSNAME_STRING = String.class.getCanonicalName();
  
  public final static String CLASSNAME_INJECT = Inject.class.getCanonicalName();
  
  public final static String CLASSNAME_POSTCONSTRUCT = PostConstruct.class.getCanonicalName();
  
  public final static String CLASSNAME_NATIVELIBMANAGER = NativeLibManager.class.getCanonicalName();
  
  public final static String CLASSNAME_ISERVICEDEFINITION = IServiceDefinition.class.getCanonicalName();
  
  public final static String CLASSNAME_CDIMODULEFRAMEWORK = CDIModuleFramework.class.getCanonicalName();
  
  public final static String CLASSNAME_INSTANCE = Instance.class.getCanonicalName();
}
