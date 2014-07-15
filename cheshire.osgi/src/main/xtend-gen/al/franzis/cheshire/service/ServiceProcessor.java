package al.franzis.cheshire.service;

import al.franzis.cheshire.service.ReferencedServiceInfo;
import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceBindMethod;
import al.franzis.cheshire.service.ServiceInfo;
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
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final TypeReference componentContextType = context.newTypeReference("org.osgi.service.component.ComponentContext");
    final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addParameter("componentContext", componentContextType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("al.franzis.cheshire.osgi.OSGiServiceContext osgiComponentContext = new al.franzis.cheshire.osgi.OSGiServiceContext(componentContext);");
            _builder.newLine();
            _builder.append("activate(osgiComponentContext);");
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("activate", _function);
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
    for (final ClassDeclaration clazz : annotatedSourceElements) {
      {
        CompilationUnit _compilationUnit = clazz.getCompilationUnit();
        final Path filePath = _compilationUnit.getFilePath();
        final Path projectPath = context.getProjectFolder(filePath);
        final Path osgiInfPath = projectPath.append("OSGI-INF");
        context.mkdir(osgiInfPath);
        String _simpleName = clazz.getSimpleName();
        String _plus = (_simpleName + ".xml");
        final Path file = osgiInfPath.append(_plus);
        final ServiceInfo serviceInfo = this.parseServiceDefinition(clazz, Service.class);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _builder.newLine();
        _builder.append("<scr:component xmlns:scr=\"http://www.osgi.org/xmlns/scr/v1.1.0\" name=\"");
        String _name = serviceInfo.getName();
        _builder.append(_name, "");
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("   \t\t\t\t");
        _builder.append("<implementation class=\"");
        String _qualifiedName = clazz.getQualifiedName();
        _builder.append(_qualifiedName, "   \t\t\t\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
        _builder.append("   \t\t\t\t");
        _builder.append("<service>");
        _builder.newLine();
        {
          String[] _providedServices = serviceInfo.getProvidedServices();
          for(final String providedService : _providedServices) {
            _builder.append("<provide interface=\"");
            _builder.append(providedService, "");
            _builder.append("\"/>");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("   \t\t\t\t");
        _builder.append("</service>");
        _builder.newLine();
        _builder.append("   \t\t\t\t");
        _builder.newLine();
        {
          ReferencedServiceInfo[] _referencedServices = serviceInfo.getReferencedServices();
          for(final ReferencedServiceInfo refService : _referencedServices) {
            _builder.append("   \t\t\t\t");
            _builder.append("<reference bind=\"");
            String _bindMethodName = refService.getBindMethodName();
            _builder.append(_bindMethodName, "   \t\t\t\t");
            _builder.append("\" cardinality=\"0..n\" interface=\"");
            String _name_1 = refService.getName();
            _builder.append(_name_1, "   \t\t\t\t");
            _builder.append("\" name=\"IPlugin\" policy=\"static\"/>");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          Map<String,String> _properties = serviceInfo.getProperties();
          Set<Map.Entry<String,String>> _entrySet = _properties.entrySet();
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
        context.setContents(file, _builder);
      }
    }
  }
  
  private ServiceInfo parseServiceDefinition(final ClassDeclaration annotatedClass, final Class<? extends Object> annotation) {
    ServiceInfo _xblockexpression = null;
    {
      Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
      final Function1<AnnotationReference,Boolean> _function = new Function1<AnnotationReference,Boolean>() {
        public Boolean apply(final AnnotationReference a) {
          AnnotationTypeDeclaration _annotationTypeDeclaration = a.getAnnotationTypeDeclaration();
          String _simpleName = _annotationTypeDeclaration.getSimpleName();
          String _simpleName_1 = annotation.getSimpleName();
          boolean _equals = Objects.equal(_simpleName, _simpleName_1);
          return Boolean.valueOf(_equals);
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
      final Map<String,String> bindMethodMap = this.getBindMethodsMap(annotatedClass);
      final Function1<String,ReferencedServiceInfo> _function_1 = new Function1<String,ReferencedServiceInfo>() {
        public ReferencedServiceInfo apply(final String sn) {
          ReferencedServiceInfo _xblockexpression = null;
          {
            final String bindMethodName = bindMethodMap.get(sn);
            ReferencedServiceInfo _referencedServiceInfo = new ReferencedServiceInfo(sn, bindMethodName);
            _xblockexpression = (_referencedServiceInfo);
          }
          return _xblockexpression;
        }
      };
      final List<ReferencedServiceInfo> referencedServices = ListExtensions.<String, ReferencedServiceInfo>map(((List<String>)Conversions.doWrapArray(referencedServicesNames)), _function_1);
      Object _value_3 = serviceAnnotation.getValue("properties");
      final String[] properties = ((String[]) _value_3);
      HashMap<String,String> _hashMap = new HashMap<String, String>();
      final Map<String,String> propertiesMap = _hashMap;
      int i = (-1);
      int _length = properties.length;
      boolean _lessThan = ((i + 2) < _length);
      boolean _while = _lessThan;
      while (_while) {
        {
          final int k = (i + 1);
          final int v = (i + 2);
          i = (i + 2);
          String _get = properties[k];
          String _get_1 = properties[v];
          propertiesMap.put(_get, _get_1);
        }
        int _length_1 = properties.length;
        boolean _lessThan_1 = ((i + 2) < _length_1);
        _while = _lessThan_1;
      }
      InputOutput.<String>println(("Service properties: " + propertiesMap));
      ServiceInfo _serviceInfo = new ServiceInfo(serviceName, ((ReferencedServiceInfo[])Conversions.unwrapArray(referencedServices, ReferencedServiceInfo.class)), providedServicesNames, propertiesMap);
      _xblockexpression = (_serviceInfo);
    }
    return _xblockexpression;
  }
  
  private Map<String,String> getBindMethodsMap(final ClassDeclaration clazzDeclaration) {
    Map<String,String> _xblockexpression = null;
    {
      HashMap<String,String> _hashMap = new HashMap<String, String>();
      final Map<String,String> bindMethodsMap = _hashMap;
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
      _xblockexpression = (bindMethodsMap);
    }
    return _xblockexpression;
  }
  
  private List<MethodDeclaration> findAnnotatedMethod(final ClassDeclaration annotatedClass, final Class<? extends Object> annotation) {
    List<MethodDeclaration> _xblockexpression = null;
    {
      Iterable<? extends MethodDeclaration> _declaredMethods = annotatedClass.getDeclaredMethods();
      final Function1<MethodDeclaration,Boolean> _function = new Function1<MethodDeclaration,Boolean>() {
        public Boolean apply(final MethodDeclaration m) {
          Iterable<? extends AnnotationReference> _annotations = m.getAnnotations();
          final Function1<AnnotationReference,Boolean> _function = new Function1<AnnotationReference,Boolean>() {
            public Boolean apply(final AnnotationReference a) {
              AnnotationTypeDeclaration _annotationTypeDeclaration = a.getAnnotationTypeDeclaration();
              String _simpleName = _annotationTypeDeclaration.getSimpleName();
              String _simpleName_1 = annotation.getSimpleName();
              boolean _equals = Objects.equal(_simpleName, _simpleName_1);
              return Boolean.valueOf(_equals);
            }
          };
          boolean _exists = IterableExtensions.exists(_annotations, _function);
          return Boolean.valueOf(_exists);
        }
      };
      Iterable<? extends MethodDeclaration> _filter = IterableExtensions.filter(_declaredMethods, _function);
      final List<? extends MethodDeclaration> annotatedMethods = IterableExtensions.toList(_filter);
      _xblockexpression = (((List<MethodDeclaration>) annotatedMethods));
    }
    return _xblockexpression;
  }
}
