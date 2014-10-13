package al.franzis.cheshire.cdi.proc;

import al.franzis.cheshire.cdi.proc.Helpers;
import al.franzis.cheshire.cdi.rt.NativeOSGiLibraryClauseParser;
import com.google.common.collect.Iterables;
import java.util.Collections;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend.lib.macro.expression.Expression;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModuleManifestProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final TypeReference moduleManifestType = context.newTypeReference(Helpers.CLASSNAME_ICDIModuleManifest);
    Iterable<? extends TypeReference> _implementedInterfaces = annotatedClass.getImplementedInterfaces();
    final Iterable<TypeReference> implInterfaces = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(CollectionLiterals.<TypeReference>newArrayList(moduleManifestType)));
    annotatedClass.setImplementedInterfaces(implInterfaces);
    final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        TypeReference _newTypeReference = context.newTypeReference(Helpers.CLASSNAME_STRING);
        it.setReturnType(_newTypeReference);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("return Bundle_Name;");
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("getBundleName", _function);
    final AnnotationReference injectAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_INJECT);
    final TypeReference runtimeLibPathProviderType = context.newTypeReference(Helpers.ClASSNAME_ICDIRuntimeLibPathProvider);
    final Procedure1<MutableFieldDeclaration> _function_1 = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration it) {
        it.addAnnotation(injectAnnotationType);
        it.setType(runtimeLibPathProviderType);
      }
    };
    annotatedClass.addField("libPathProvider", _function_1);
    final String nativeClauses = this.parseNativeClauses(annotatedClass);
    final NativeOSGiLibraryClauseParser libManager = new NativeOSGiLibraryClauseParser(nativeClauses);
    final String nativeLibs = libManager.toStringLiteral();
    final AnnotationReference postConstructAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_POSTCONSTRUCT);
    final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addAnnotation(postConstructAnnotationType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("String[][][] nativeLibsPaths = ");
            _builder.append(nativeLibs, "");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("String effectiveNativeLibsPathDirs = ");
            _builder.append(Helpers.CLASSNAME_NATIVELIBMANAGER, "");
            _builder.append(".effectiveNativeLibsPaths(this, libPathProvider, nativeLibsPaths);");
            _builder.newLineIfNotEmpty();
            _builder.append(Helpers.CLASSNAME_NATIVELIBMANAGER, "");
            _builder.append(".augmentJavaLibraryPath(effectiveNativeLibsPathDirs);");
            _builder.newLineIfNotEmpty();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("init", _function_2);
  }
  
  public void doGenerateCode(final ClassDeclaration annotatedClass, @Extension final CodeGenerationContext context) {
  }
  
  private String parseNativeClauses(final ClassDeclaration annotatedClass) {
    Object _xblockexpression = null;
    {
      Iterable<? extends FieldDeclaration> _declaredFields = annotatedClass.getDeclaredFields();
      for (final FieldDeclaration field : _declaredFields) {
        {
          final String key = field.getSimpleName();
          boolean _equals = "Bundle_NativeCode".equals(key);
          if (_equals) {
            Expression _initializer = field.getInitializer();
            final String nativeClauses = _initializer.toString();
            return nativeClauses;
          }
        }
      }
      _xblockexpression = null;
    }
    return ((String)_xblockexpression);
  }
}
