package al.franzis.cheshire;

import al.franzis.cheshire.Logger;
import com.google.common.base.Objects;
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
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class ModuleProcessor extends AbstractClassProcessor {
  private Logger logger;
  
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
    try {
      for (final ClassDeclaration clazz : annotatedSourceElements) {
        {
          Logger _logger = Logger.getLogger(clazz, context);
          this.logger = _logger;
          try {
            final Map<String, String> fieldMap = this.parse(clazz);
            final String bundleName = fieldMap.remove("Bundle-Name");
            String _remove = fieldMap.remove("Service-Component");
            final String serviceDefs = this.processServiceDefinitions(_remove);
            CompilationUnit _compilationUnit = clazz.getCompilationUnit();
            final Path filePath = _compilationUnit.getFilePath();
            final Path projectPath = context.getProjectFolder(filePath);
            final Path metaInfPath = projectPath.append("META-INF");
            final Path file = metaInfPath.append("MANIFEST.MF");
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
            _builder.append("Service-Component: ");
            _builder.append(serviceDefs, "");
            _builder.newLineIfNotEmpty();
            {
              Set<Map.Entry<String, String>> _entrySet = fieldMap.entrySet();
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
          } catch (final Throwable _t) {
            if (_t instanceof Throwable) {
              final Throwable t = (Throwable)_t;
              this.logger.error("Error while processing Module Manifest", t);
              throw t;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private String processServiceDefinitions(final String serviceDefinitions) {
    String _xblockexpression = null;
    {
      boolean _equals = Objects.equal(serviceDefinitions, null);
      if (_equals) {
        return "";
      }
      String _replaceAll = serviceDefinitions.replaceAll("\\s", "");
      String[] sdefs = _replaceAll.split(",");
      final String[] _converted_sdefs = (String[])sdefs;
      final Function1<String, String> _function = new Function1<String, String>() {
        public String apply(final String s) {
          String _xblockexpression = null;
          {
            final int idx = s.lastIndexOf(".");
            String unq = s;
            if ((idx != (-1))) {
              int _length = s.length();
              String _substring = s.substring((idx + 1), _length);
              unq = _substring;
            }
            _xblockexpression = unq = (("OSGI-INF/" + unq) + ".xml");
          }
          return _xblockexpression;
        }
      };
      final List<String> ss = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(_converted_sdefs)), _function);
      _xblockexpression = this.flatten(((String[])Conversions.unwrapArray(ss, String.class)));
    }
    return _xblockexpression;
  }
  
  private Map<String, String> parse(final ClassDeclaration annotatedClass) {
    Map<String, String> _xblockexpression = null;
    {
      final Map<String, String> keyValueMap = new HashMap<String, String>();
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
            String _flatten = this.flatten(multiValues);
            value = _flatten;
          } else {
            value = fieldValue;
          }
          keyValueMap.put(key, value);
        }
      }
      _xblockexpression = keyValueMap;
    }
    return _xblockexpression;
  }
  
  private String flatten(final String[] ss) {
    String _xblockexpression = null;
    {
      final StringBuffer buf = new StringBuffer();
      boolean first = true;
      for (final String s : ss) {
        {
          String _s = s.trim();
          boolean _and = false;
          boolean _startsWith = _s.startsWith("\"");
          if (!_startsWith) {
            _and = false;
          } else {
            boolean _endsWith = _s.endsWith("\"");
            _and = _endsWith;
          }
          if (_and) {
            int _length = _s.length();
            int _minus = (_length - 1);
            String _substring = _s.substring(1, _minus);
            _s = _substring;
          }
          if ((!first)) {
            buf.append(",\n ");
          }
          buf.append(_s);
          first = false;
        }
      }
      _xblockexpression = buf.toString();
    }
    return _xblockexpression;
  }
}
