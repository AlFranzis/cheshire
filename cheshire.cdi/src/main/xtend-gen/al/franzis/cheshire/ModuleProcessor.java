package al.franzis.cheshire;

import al.franzis.cheshire.NativeLibHandler;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.expression.Expression;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModuleProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    try {
      final String nativeClauses = this.parseNativeClauses(annotatedClass);
      NativeLibHandler _nativeLibHandler = new NativeLibHandler(nativeClauses);
      final NativeLibHandler libHandler = _nativeLibHandler;
      final String nativeLibs = libHandler.getModuleNativeLibs();
      final Type postConstructAnnotationType = context.findTypeGlobally("javax.annotation.PostConstruct");
      final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          it.addAnnotation(postConstructAnnotationType);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("String nativeLibs = \"");
              _builder.append(nativeLibs, "");
              _builder.append("\";");
              _builder.newLineIfNotEmpty();
              _builder.append("al.franzis.cheshire.NativeLibHandler.augmentJavaLibraryPath(nativeLibs);");
              _builder.newLine();
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      annotatedClass.addMethod("init", _function);
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
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
