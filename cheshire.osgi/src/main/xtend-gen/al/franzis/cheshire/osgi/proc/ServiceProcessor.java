package al.franzis.cheshire.osgi.proc;

import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceActivationMethod;
import al.franzis.cheshire.api.service.ServiceBindMethod;
import al.franzis.cheshire.osgi.proc.Helpers;
import al.franzis.cheshire.osgi.proc.Logger;
import al.franzis.cheshire.osgi.proc.PathHelper;
import al.franzis.cheshire.osgi.proc.ReferencedServiceInfo;
import al.franzis.cheshire.osgi.proc.ServiceInfo;
import com.google.common.base.Objects;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend.lib.macro.file.FileLocations;
import org.eclipse.xtend.lib.macro.file.FileSystemSupport;
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ServiceProcessor extends AbstractClassProcessor {
  private final StringBuffer logMsgBuf = new StringBuffer();
  
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final TypeReference componentContextType = context.newTypeReference(Helpers.CLASSNAME_COMPONENTCONTEXT);
    final List<MethodDeclaration> activationMethods = this.findAnnotatedMethod(annotatedClass, ServiceActivationMethod.class);
    boolean _isEmpty = activationMethods.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      int _size = activationMethods.size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        this.logMsgBuf.append("ERROR: Multiple service activation methods!");
      } else {
        Iterator<MethodDeclaration> _iterator = activationMethods.iterator();
        MethodDeclaration _next = _iterator.next();
        final String activationMethodName = _next.getSimpleName();
        final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
          public void apply(final MutableMethodDeclaration it) {
            it.addParameter("componentContext", componentContextType);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append(Helpers.CLASSNAME_OSGISERVICECONTEXT, "");
                _builder.append(" osgiComponentContext = new ");
                _builder.append(Helpers.CLASSNAME_OSGISERVICECONTEXT, "");
                _builder.append("(componentContext);");
                _builder.newLineIfNotEmpty();
                _builder.append(activationMethodName, "");
                _builder.append("(osgiComponentContext);");
                _builder.newLineIfNotEmpty();
                return _builder;
              }
            };
            it.setBody(_function);
          }
        };
        annotatedClass.addMethod("activate", _function);
      }
    }
    boolean _isEclipseEnvironment = PathHelper.isEclipseEnvironment();
    boolean _not_1 = (!_isEclipseEnvironment);
    if (_not_1) {
      this.createOSGiServiceFile(context, null, context, annotatedClass);
    }
  }
  
  public void doGenerateCode(final ClassDeclaration annotatedClass, @Extension final CodeGenerationContext context) {
    boolean _isEclipseEnvironment = PathHelper.isEclipseEnvironment();
    if (_isEclipseEnvironment) {
      this.createOSGiServiceFile(context, context, context, annotatedClass);
    }
    final Logger logger = Logger.getLogger(annotatedClass, context);
    String _string = this.logMsgBuf.toString();
    logger.info(_string);
  }
  
  private void createOSGiServiceFile(final FileLocations fl, @Extension final MutableFileSystemSupport mfss, @Extension final FileSystemSupport fs, final ClassDeclaration serviceClass) {
    CompilationUnit _compilationUnit = serviceClass.getCompilationUnit();
    final Path filePath = _compilationUnit.getFilePath();
    final Path projectPath = fl.getProjectFolder(filePath);
    final Path osgiInfPath = projectPath.append("OSGI-INF");
    String _simpleName = serviceClass.getSimpleName();
    String _plus = (_simpleName + ".xml");
    final Path file = osgiInfPath.append(_plus);
    final ServiceInfo serviceInfo = this.parseServiceDefinition(serviceClass, Service.class);
    PathHelper _instance = PathHelper.getInstance();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<scr:component xmlns:scr=\"http://www.osgi.org/xmlns/scr/v1.1.0\" name=\"");
    String _name = serviceInfo.getName();
    _builder.append(_name, "");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("<implementation class=\"");
    String _qualifiedName = serviceClass.getQualifiedName();
    _builder.append(_qualifiedName, "\t\t\t");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("<service>");
    _builder.newLine();
    {
      String[] _providedServices = serviceInfo.getProvidedServices();
      for(final String providedService : _providedServices) {
        _builder.append("\t\t\t\t");
        _builder.append("<provide interface=\"");
        _builder.append(providedService, "\t\t\t\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("</service>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.newLine();
    {
      ReferencedServiceInfo[] _referencedServices = serviceInfo.getReferencedServices();
      for(final ReferencedServiceInfo refService : _referencedServices) {
        _builder.append("\t\t\t");
        _builder.append("<reference bind=\"");
        String _bindMethodName = refService.getBindMethodName();
        _builder.append(_bindMethodName, "\t\t\t");
        _builder.append("\" cardinality=\"0..n\" interface=\"");
        String _name_1 = refService.getName();
        _builder.append(_name_1, "\t\t\t");
        _builder.append("\" name=\"IPlugin\" policy=\"static\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Map<String, String> _properties = serviceInfo.getProperties();
      Set<Map.Entry<String, String>> _entrySet = _properties.entrySet();
      for(final Map.Entry<String, String> prop : _entrySet) {
        _builder.append("<property name=\"");
        String _key = prop.getKey();
        _builder.append(_key, "");
        _builder.append("\" type=\"String\" value=\"");
        String _value = prop.getValue();
        _builder.append(_value, "");
        _builder.append("\"/>\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</scr:component>");
    _builder.newLine();
    _instance.setContents(mfss, osgiInfPath, file, _builder);
  }
  
  private ServiceInfo parseServiceDefinition(final ClassDeclaration annotatedClass, final Class<?> annotation) {
    ServiceInfo _xblockexpression = null;
    {
      Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
      final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
        public Boolean apply(final AnnotationReference a) {
          AnnotationTypeDeclaration _annotationTypeDeclaration = a.getAnnotationTypeDeclaration();
          String _simpleName = _annotationTypeDeclaration.getSimpleName();
          String _simpleName_1 = annotation.getSimpleName();
          return Boolean.valueOf(Objects.equal(_simpleName, _simpleName_1));
        }
      };
      final AnnotationReference serviceAnnotation = IterableExtensions.findFirst(_annotations, _function);
      Object _value = serviceAnnotation.getValue("name");
      final String serviceName = ((String) _value);
      InputOutput.<String>println(("Service name: " + serviceName));
      Object _value_1 = serviceAnnotation.getValue("providedServices");
      final String[] providedServicesNames = ((String[]) _value_1);
      InputOutput.<String[]>println(providedServicesNames);
      Object _value_2 = serviceAnnotation.getValue("referencedServices");
      final String[] referencedServicesNames = ((String[]) _value_2);
      InputOutput.<String[]>println(referencedServicesNames);
      final Map<String, String> bindMethodMap = this.getBindMethodsMap(annotatedClass);
      final Function1<String, ReferencedServiceInfo> _function_1 = new Function1<String, ReferencedServiceInfo>() {
        public ReferencedServiceInfo apply(final String sn) {
          ReferencedServiceInfo _xblockexpression = null;
          {
            final String bindMethodName = bindMethodMap.get(sn);
            _xblockexpression = new ReferencedServiceInfo(sn, bindMethodName);
          }
          return _xblockexpression;
        }
      };
      final List<ReferencedServiceInfo> referencedServices = ListExtensions.<String, ReferencedServiceInfo>map(((List<String>)Conversions.doWrapArray(referencedServicesNames)), _function_1);
      Object _value_3 = serviceAnnotation.getValue("properties");
      final String[] properties = ((String[]) _value_3);
      final Map<String, String> propertiesMap = new HashMap<String, String>();
      int i = (-1);
      while (((i + 2) < properties.length)) {
        {
          final int k = (i + 1);
          final int v = (i + 2);
          i = (i + 2);
          String _get = properties[k];
          String _get_1 = properties[v];
          propertiesMap.put(_get, _get_1);
        }
      }
      InputOutput.<String>println(("Service properties: " + propertiesMap));
      _xblockexpression = new ServiceInfo(serviceName, ((ReferencedServiceInfo[])Conversions.unwrapArray(referencedServices, ReferencedServiceInfo.class)), providedServicesNames, propertiesMap);
    }
    return _xblockexpression;
  }
  
  private Map<String, String> getBindMethodsMap(final ClassDeclaration clazzDeclaration) {
    Map<String, String> _xblockexpression = null;
    {
      final Map<String, String> bindMethodsMap = new HashMap<String, String>();
      List<MethodDeclaration> _findAnnotatedMethod = this.findAnnotatedMethod(clazzDeclaration, ServiceBindMethod.class);
      for (final MethodDeclaration method : _findAnnotatedMethod) {
        Iterable<? extends ParameterDeclaration> _parameters = method.getParameters();
        Iterator<? extends ParameterDeclaration> _iterator = _parameters.iterator();
        ParameterDeclaration _next = _iterator.next();
        TypeReference _type = _next.getType();
        String _name = _type.getName();
        String _simpleName = method.getSimpleName();
        bindMethodsMap.put(_name, _simpleName);
      }
      _xblockexpression = bindMethodsMap;
    }
    return _xblockexpression;
  }
  
  private List<MethodDeclaration> findAnnotatedMethod(final ClassDeclaration annotatedClass, final Class<?> annotation) {
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
}
