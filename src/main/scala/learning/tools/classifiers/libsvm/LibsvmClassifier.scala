package learning.tools.classifiers.libsvm

import libsvm._

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/14/15.
 */
class LibsvmClassifier {
  var model: svm_model = null

  def train(path: String): Unit = {
    // Read a LIBSVM problem from file using default parameters
    val params = LibsvmHelper.DefaultParameters()
    val problem = LibsvmHelper.CreateProblemFromFile(path, params)

    // Train and keep the svm_model object
    this.model = svm.svm_train(problem, params)
  }

  def save(path: String): Unit = svm.svm_save_model(path, this.model)
  def load(path: String): Unit = svm.svm_load_model(path)
}