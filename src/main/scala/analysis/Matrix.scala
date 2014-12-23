package analysis

import com.sksamuel.scrimage.Image

private object MatrixUtils{
  def fromImage(image: Image) = Matrix(for (y <- 0 until image.height) yield for(x<-0 until image.width) yield image.pixel(x,y))
}

case class Matrix(data: Seq[Seq[Int]]){
  val height = data.size
  val width = data.head.size
  override def toString = data.map(_.mkString(";")).mkString("\n")
}
