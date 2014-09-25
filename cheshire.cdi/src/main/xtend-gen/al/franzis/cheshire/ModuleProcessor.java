package al.franzis.cheshire;

import al.franzis.cheshire.NativeLibHandler;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModuleProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    try {
      final TypeReference moduleManifestType = context.newTypeReference("al.franzis.cheshire.cdi.ICDIModuleManifest");
      Iterable<? extends TypeReference> _implementedInterfaces = annotatedClass.getImplementedInterfaces();
      final Iterable<TypeReference> implInterfaces = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(Lists.<TypeReference>newArrayList(moduleManifestType)));
      annotatedClass.setImplementedInterfaces(implInterfaces);
      final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          TypeReference _newTypeReference = context.newTypeReference("java.lang.String");
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
      final AnnotationReference injectAnnotationType = context.newAnnotationReference("javax.inject.Inject");
      final TypeReference runtimeLibPathProviderType = context.newTypeReference("al.franzis.cheshire.IRuntimeLibPathProvider");
      final Procedure1<MutableFieldDeclaration> _function_1 = new Procedure1<MutableFieldDeclaration>() {
        public void apply(final MutableFieldDeclaration it) {
          it.addAnnotation(injectAnnotationType);
          it.setType(runtimeLibPathProviderType);
        }
      };
      annotatedClass.addField("libPathProvider", _function_1);
      final String nativeClauses = this.parseNativeClauses(annotatedClass);
      final NativeLibHandler libHandler = new NativeLibHandler(nativeClauses);
      final String nativeLibs = libHandler.getModuleNativeLibs();
      final AnnotationReference postConstructAnnotationType = context.newAnnotationReference("javax.annotation.PostConstruct");
      final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.addAnnotation(postConstructAnnotationType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("String[] nativeLibsPaths = ");
              _builder.append(nativeLibs, "");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("String effectiveNativeLibsPaths = al.franzis.cheshire.NativeLibHandler.effectiveNativeLibsPaths(this, libPathProvider, nativeLibsPaths);");
              _builder.newLine();
              _builder.append("al.franzis.cheshire.NativeLibHandler.augmentJavaLibraryPath(effectiveNativeLibsPaths);");
              _builder.newLine();
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      annotatedClass.addMethod("init", _function_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void doGenerateCode(final ClassDeclaration annotatedClass, @Extension final CodeGenerationContext context) {
  }
  
  private String parseNativeClauses(final ClassDeclaration annotatedClass) {
    String _xblockexpression = null;
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
    return _xblockexpression;
  }
}
