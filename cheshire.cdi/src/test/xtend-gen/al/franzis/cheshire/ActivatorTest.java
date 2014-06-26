package al.franzis.cheshire;

import al.franzis.cheshire.ModuleActivator;
import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Test;

@SuppressWarnings("all")
public class ActivatorTest {
  @Extension
  private XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(ModuleActivator.class);
  
  @Test
  public void testCompiler() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import al.franzis.cheshire.ModuleActivator");
    _builder.newLine();
    _builder.append("import al.franzis.cheshire.ModuleStartMethod");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@ModuleActivator");
    _builder.newLine();
    _builder.append("class ActivatorExample {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@ModuleStartMethod");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def void start() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("println(\"Start\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("\t\t\t");
    _builder_1.newLine();
    this.compilerTester.assertCompilesTo(_builder, _builder_1);
  }
}
