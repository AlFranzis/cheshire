package al.franzis.cheshire;

import al.franzis.cheshire.Module;
import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Test;

@SuppressWarnings("all")
public class ModuleTest {
  @Extension
  private XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(Module.class);
  
  @Test
  public void testCompiler() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import al.franzis.cheshire.Module");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Module");
    _builder.newLine();
    _builder.append("class ManifestExample {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("val bundleName = \"bundleABC\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("val exportedPackages = #[ \"com\", \"a\"]");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("\t\t\t");
    _builder_1.newLine();
    this.compilerTester.assertCompilesTo(_builder, _builder_1);
  }
}
