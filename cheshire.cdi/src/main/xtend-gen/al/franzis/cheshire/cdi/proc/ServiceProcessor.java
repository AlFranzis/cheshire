package al.franzis.cheshire.cdi.proc;

import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceBindMethod;
import al.franzis.cheshire.cdi.proc.Helpers;
import al.franzis.cheshire.cdi.proc.Logger;
import al.franzis.cheshire.cdi.proc.ReferencedServiceFactoryInfo;
import al.franzis.cheshire.cdi.proc.ReferencedServiceInfo;
import al.franzis.cheshire.cdi.proc.ServiceInfo;
import al.franzis.cheshire.cdi.rt.ServiceImplementation;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
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
import org.eclipse.xtend.lib.macro.declaration.MutableParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ServiceProcessor extends AbstractClassProcessor {
  private final StringBuffer logMsgBuf = new StringBuffer();
  
  public void doTransform(final MutableClassDeclaration annotatedClass, @Extension final TransformationContext context) {
    final TypeReference serviceDefinitionType = context.newTypeReference(Helpers.CLASSNAME_ISERVICEDEFINITION);
    Iterable<? extends TypeReference> _implementedInterfaces = annotatedClass.getImplementedInterfaces();
    final Iterable<TypeReference> implInterfaces = Iterables.<TypeReference>concat(_implementedInterfaces, Collections.<TypeReference>unmodifiableList(CollectionLiterals.<TypeReference>newArrayList(serviceDefinitionType)));
    annotatedClass.setImplementedInterfaces(implInterfaces);
    final AnnotationReference serviceImplementationAnnotationType = context.newAnnotationReference(ServiceImplementation.class);
    annotatedClass.addAnnotation(serviceImplementationAnnotationType);
    final TypeReference cdiModuleFrameworkType = context.newTypeReference(Helpers.CLASSNAME_CDIMODULEFRAMEWORK);
    final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration it) {
        it.setType(cdiModuleFrameworkType);
      }
    };
    annotatedClass.addField("moduleFramework", _function);
    final AnnotationReference injectAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_INJECT);
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
    final AnnotationReference postConstructAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_POSTCONSTRUCT);
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
        boolean _equals = Objects.equal(instancesParameterType, null);
        if (_equals) {
          this.logMsgBuf.append("WARN: Referenced service type name not found!\n");
        }
        final TypeReference instancesType = context.newTypeReference(Helpers.CLASSNAME_INSTANCE, instancesParameterType);
        final String bindMethodName = referencedService.getBindMethodName();
        final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
          public void apply(final MutableMethodDeclaration it) {
            it.addAnnotation(injectAnnotationType);
            MutableParameterDeclaration _addParameter = it.addParameter("instances", instancesType);
            _addParameter.addAnnotation(serviceImplementationAnnotationType);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("java.util.Iterator<");
                _builder.append(refServiceTypeName, "");
                _builder.append("> it = instances.iterator();");
                _builder.newLineIfNotEmpty();
                _builder.append("while(it.hasNext()) {");
                _builder.newLine();
                _builder.append("\t\t\t\t\t\t");
                _builder.append(bindMethodName, "\t\t\t\t\t\t");
                _builder.append("(it.next());");
                _builder.newLineIfNotEmpty();
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
  
  public void doGenerateCode(final List<? extends ClassDeclaration> annotatedClasses, @Extension final CodeGenerationContext context) {
    for (final ClassDeclaration annotatedClass : annotatedClasses) {
      {
        final Logger logger = Logger.getLogger(annotatedClass, context);
        String _string = this.logMsgBuf.toString();
        logger.info(_string);
      }
    }
  }
  
  private ServiceInfo parseServiceDefinition(final ClassDeclaration annotatedClass, final Class<?> annotation) {
    ServiceInfo _xtrycatchfinallyexpression = null;
    try {
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
        Object _value_1 = serviceAnnotation.getValue("providedServices");
        final String[] providedServicesNames = ((String[]) _value_1);
        final Map<String, String> bindMethodMap = this.getBindMethodsMap(annotatedClass);
        Object _value_2 = serviceAnnotation.getValue("referencedServices");
        final String[] referencedServicesNames = ((String[]) _value_2);
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
        Object _value_3 = serviceAnnotation.getValue("referencedServiceFactories");
        final String[] referencedServiceFactoryNames = ((String[]) _value_3);
        final Function1<String, ReferencedServiceFactoryInfo> _function_2 = new Function1<String, ReferencedServiceFactoryInfo>() {
          public ReferencedServiceFactoryInfo apply(final String fn) {
            ReferencedServiceFactoryInfo _xblockexpression = null;
            {
              final String bindMethodName = bindMethodMap.get("al.franzis.cheshire.api.service.IServiceFactory");
              _xblockexpression = new ReferencedServiceFactoryInfo(fn, bindMethodName);
            }
            return _xblockexpression;
          }
        };
        final List<ReferencedServiceFactoryInfo> referencedServiceFactories = ListExtensions.<String, ReferencedServiceFactoryInfo>map(((List<String>)Conversions.doWrapArray(referencedServiceFactoryNames)), _function_2);
        Object _value_4 = serviceAnnotation.getValue("properties");
        final String[] properties = ((String[]) _value_4);
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
        Object _value_5 = serviceAnnotation.getValue("factory");
        final String factory = ((String) _value_5);
        _xblockexpression = new ServiceInfo(serviceName, ((ReferencedServiceInfo[])Conversions.unwrapArray(referencedServices, ReferencedServiceInfo.class)), ((ReferencedServiceFactoryInfo[])Conversions.unwrapArray(referencedServiceFactories, ReferencedServiceFactoryInfo.class)), providedServicesNames, factory, propertiesMap);
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof Throwable) {
        final Throwable t = (Throwable)_t;
        Object _xblockexpression_1 = null;
        {
          this.logMsgBuf.append(("Error: " + t));
          _xblockexpression_1 = null;
        }
        _xtrycatchfinallyexpression = ((ServiceInfo)_xblockexpression_1);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  private Map<String, String> getBindMethodsMap(final ClassDeclaration clazzDeclaration) {
    Map<String, String> _xblockexpression = null;
    {
      final Map<String, String> bindMethodsMap = new HashMap<String, String>();
      List<MethodDeclaration> _findAnnotatedMethod = Helpers.findAnnotatedMethod(clazzDeclaration, ServiceBindMethod.class);
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
}
