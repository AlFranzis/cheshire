package al.franzis.cheshire;

import java.util.List;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.expression.Expression;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class ModuleProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    InputOutput.<TransformationContext>println(context);
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
    for (final ClassDeclaration clazz : annotatedSourceElements) {
      {
        CompilationUnit _compilationUnit = clazz.getCompilationUnit();
        final Path filePath = _compilationUnit.getFilePath();
        Path _targetFolder = context.getTargetFolder(filePath);
        String _qualifiedName = clazz.getQualifiedName();
        String _replace = _qualifiedName.replace(".", "/");
        String _plus = (_replace + ".properties");
        final Path file = _targetFolder.append(_plus);
        StringConcatenation _builder = new StringConcatenation();
        {
          Iterable<? extends FieldDeclaration> _declaredFields = clazz.getDeclaredFields();
          for(final FieldDeclaration field : _declaredFields) {
            String _simpleName = field.getSimpleName();
            _builder.append(_simpleName, "");
            _builder.append(" : ");
            Expression _initializer = field.getInitializer();
            String _string = _initializer.toString();
            _builder.append(_string, "");
            _builder.newLineIfNotEmpty();
          }
        }
        context.setContents(file, _builder);
      }
    }
  }
}
