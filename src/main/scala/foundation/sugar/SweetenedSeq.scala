package foundation.sugar

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/18/15.
 */
class SweetenedSeq[T](seq: Seq[T]) {
  def shuffle = util.Random.shuffle(seq)
}
