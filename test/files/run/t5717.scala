import scala.tools.partest._
import java.io.File

object Test extends StoreReporterDirectTest {
  def code = "package a { class B }"

  override def extraSettings: String = s"-cp ${pathOf(sys.props("partest.lib"), testOutput.path)}"

  // TODO
  // Don't assume output is on physical disk
  // Let the compiler tell us output dir
  // val sc = newCompiler("-cp", classpath, "-d", testOutput.path)
  // val out = sc.settings.outputDirs.getSingleOutput.get
  def show(): Unit = {
    // Don't crash when we find a file 'a' where package 'a' should go.
    scala.reflect.io.File(testOutput.path + "/a").writeAll("a")
    compile()
    val List(i) = filteredInfos
    // for some reason, nio doesn't throw the same exception on windows and linux/mac
    import File.separator
    val expected = s"error writing ${testOutput.path}${separator}a${separator}B.class: Can't create directory ${testOutput.path}${separator}a" +
      "; there is an existing (non-directory) file in its path"
    assert(i.msg == expected, i.msg)
  }
}
