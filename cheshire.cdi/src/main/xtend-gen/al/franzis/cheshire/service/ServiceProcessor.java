package al.franzis.cheshire.service;

import al.franzis.cheshire.service.ReferencedServiceInfo;
import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceBindMethod;
import al.franzis.cheshire.service.ServiceInfo;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
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
    final TypeReference serviceDefinitionType = context.newTypeReference("al.franzis.cheshire.service.IServiceDefinition");
    Iterable<? extends TypeReference> _implementedInterfaces = annotatedClass.getImplementedInterfaces();
    final Iterable<TypeReference> implInterfaces = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(Lists.<TypeReference>newArrayList(serviceDefinitionType)));
    annotatedClass.setImplementedInterfaces(implInterfaces);
    final TypeReference cdiModuleFrameworkType = context.newTypeReference("al.franzis.cheshire.cdi.CDIModuleFramework");
    final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration it) {
        it.setType(cdiModuleFrameworkType);
      }
    };
    annotatedClass.addField("moduleFramework", _function);
    final Type injectAnnotationType = context.findTypeGlobally("javax.inject.Inject");
    final Procedure1<MutableMethodDeclaration> _function_1 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addAnnotation(injectAnnotationType);
        it.addParameter("moduleFramework", cdiModuleFrameworkType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("this.moduleFramework = moduleFramework;");
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("setModuleFramework", _function_1);
    final Type postConstructAnnotationType = context.findTypeGlobally("javax.annotation.PostConstruct");
    final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
      public void apply(final MutableMethodDeclaration it) {
        it.addAnnotation(postConstructAnnotationType);
        final CompilationStrategy _function = new CompilationStrategy() {
          public CharSequence compile(final CompilationStrategy.CompilationContext it) {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("moduleFramework.registerCDIService(this);");
            _builder.newLine();
            return _builder;
          }
        };
        it.setBody(_function);
      }
    };
    annotatedClass.addMethod("init", _function_2);
    final ServiceInfo serviceInfo = this.parseServiceDefinition(annotatedClass, Service.class);
    int i = 0;
    ReferencedServiceInfo[] _referencedServices = serviceInfo.getReferencedServices();
    for (final ReferencedServiceInfo referencedService : _referencedServices) {
      {
        final String refServiceTypeName = referencedService.getName();
        final TypeReference instancesParameterType = context.newTypeReference(refServiceTypeName);
        final TypeReference instancesType = context.newTypeReference("javax.enterprise.inject.Instance", instancesParameterType);
        final String bindMethodName = referencedService.getBindMethodName();
        final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
          public void apply(final MutableMethodDeclaration it) {
            it.addAnnotation(injectAnnotationType);
            it.addParameter("instances", instancesType);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("java.util.Iterator<");
                _builder.append(refServiceTypeName, "");
                _builder.append("> it = instances.iterator();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t\t\t");
                _builder.append("while(it.hasNext()) {");
                _builder.newLine();
                _builder.append("\t\t\t\t\t\t");
                _builder.append(bindMethodName, "\t\t\t\t\t\t");
                _builder.append("(it.next());");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t\t\t");
                _builder.append("}");
                _builder.newLine();
                return _builder;
              }
            };
            it.setBody(_function);
          }
        };
        annotatedClass.addMethod(("setInstances" + Integer.valueOf(i)), _function_3);
        i = (i + 1);
      }
    }
  }
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedSourceElements, @Extension final CodeGenerationContext context) {
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
