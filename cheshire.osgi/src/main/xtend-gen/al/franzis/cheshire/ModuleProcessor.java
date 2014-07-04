package al.franzis.cheshire;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
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
        final Map<String,String> fieldMap = this.parse(clazz);
        final String bundleName = fieldMap.remove("Bundle-Name");
        CompilationUnit _compilationUnit = clazz.getCompilationUnit();
        final Path filePath = _compilationUnit.getFilePath();
        Path _targetFolder = context.getTargetFolder(filePath);
        final Path file = _targetFolder.append("MANIFEST.MF");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Manifest-Version: 1.0");
        _builder.newLine();
        _builder.append("Bundle-ManifestVersion: 2");
        _builder.newLine();
        _builder.append("Bundle-Name: ");
        _builder.append(bundleName, "");
        _builder.newLineIfNotEmpty();
        _builder.append("Bundle-SymbolicName: ");
        _builder.append(bundleName, "");
        _builder.newLineIfNotEmpty();
        {
          Set<Map.Entry<String,String>> _entrySet = fieldMap.entrySet();
          for(final Map.Entry<String, String> entry : _entrySet) {
            String _key = entry.getKey();
            _builder.append(_key, "");
            _builder.append(": ");
            String _value = entry.getValue();
            _builder.append(_value, "");
            _builder.newLineIfNotEmpty();
          }
        }
        context.setContents(file, _builder);
      }
    }
  }
  
  private Map<String,String> parse(final ClassDeclaration annotatedClass) {
    Map<String,String> _xblockexpression = null;
    {
      HashMap<String,String> _hashMap = new HashMap<String, String>();
      final Map<String,String> keyValueMap = _hashMap;
      Iterable<? extends FieldDeclaration> _declaredFields = annotatedClass.getDeclaredFields();
      for (final FieldDeclaration field : _declaredFields) {
        {
          String _simpleName = field.getSimpleName();
          final String key = _simpleName.replace("_", "-");
          Expression _initializer = field.getInitializer();
          String fieldValue = _initializer.toString();
          int _length = fieldValue.length();
          int _minus = (_length - 1);
          String _substring = fieldValue.substring(1, _minus);
          fieldValue = _substring;
          String _replace = fieldValue.replace("\\\"", "\"");
          fieldValue = _replace;
          String value = "";
          TypeReference _type = field.getType();
          boolean _isArray = _type.isArray();
          if (_isArray) {
            String _replace_1 = fieldValue.replace("[", "");
            String _replace_2 = _replace_1.replace("]", "");
            fieldValue = _replace_2;
            final String[] multiValues = fieldValue.split(",");
            StringBuffer _stringBuffer = new StringBuffer();
            final StringBuffer buf = _stringBuffer;
            boolean first = true;
            for (final String sv : multiValues) {
              {
                final String trimmed = sv.trim();
                int _length_1 = trimmed.length();
                int _minus_1 = (_length_1 - 1);
                final String s = trimmed.substring(1, _minus_1);
                if ((!first)) {
                  buf.append(",\n ");
                }
                buf.append(s);
                first = false;
              }
            }
            String _string = buf.toString();
            value = _string;
          } else {
            value = fieldValue;
          }
          keyValueMap.put(key, value);
        }
      }
      _xblockexpression = (keyValueMap);
    }
    return _xblockexpression;
  }
}
