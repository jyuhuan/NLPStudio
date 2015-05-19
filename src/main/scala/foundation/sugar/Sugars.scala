package foundation.sugar

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/18/15.
 */
object Sugars {
  implicit def sweetenSeq[T](seq: Seq[T]): SweetenedSeq[T] = new SweetenedSeq[T](seq)
}