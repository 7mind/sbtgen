package izumi.sbtgen.impl

import izumi.sbtgen.model.ArtifactId

trait WithBasicRenderers {
  protected def renderName(s: String): String = s"`$s`"
  protected def renderName(s: ArtifactId): String = renderName(s.value)
  protected def renderProjectRef(path: String, id: String): String = {
    s"""ProjectRef(file("$path"), "$id")"""
  }

  def stringLit(s: String): String = {
    if (s.contains("\"")) {
      val q = "\"" * 3
      q + s + q
    } else {
      '"'.toString + s + '"'
    }
  }
}
