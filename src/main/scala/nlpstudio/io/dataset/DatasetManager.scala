package nlpstudio.io.dataset

import nlpstudio.core.Dataset
import nlpstudio.exceptions.DatasetNotInstalledException
import nlpstudio.resources.penntreebank.PennTreebank

import scala.collection.mutable
import scala.xml.XML

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/1/15.
 */
object DatasetManager {

  val corpusDirs = mutable.HashMap[String, Dataset]()
  load("/Users/yuhuan/work/data/NLPStudio/Config.xml")

  def NomBankPath = {
    if (corpusDirs contains "NomBank") corpusDirs("NomBank").path
    else throw new DatasetNotInstalledException("NomBank is not installed!")
  }

  def NomBank = nlpstudio.resources.nombank.NomBank.load(NomBankPath, PennTreebankWSJPartPath)

  def PennTreebankWSJPartPath = {
    if (corpusDirs contains "PennTreebankWSJPart") corpusDirs("PennTreebankWSJPart").path
    else throw new DatasetNotInstalledException("PennTreebankWSJPart is not installed")
  }

  def PennTreebankWSJPart = PennTreebank.load(PennTreebankWSJPartPath)

  def NomLexPlusPath = {
    if (corpusDirs contains "NomLexPlus") corpusDirs("NomLexPlus").path
    else throw new DatasetNotInstalledException("NomLexPlus is not installed!")
  }

  def NomLexPlus = nlpstudio.resources.nomlexplus.NomLexPlus.load(NomLexPlusPath)

  def MPQASubjectivityLexiconPath = {
    if (corpusDirs contains "MPQASubjectivityLexicon") corpusDirs("MPQASubjectivityLexicon").path
    else throw new DatasetNotInstalledException("MPQASubjectivityLexicon is not installed!")
  }

  def MPQASubjectivityLexicon = nlpstudio.resources.subjectivelexicons.MpqaSubjectivityLexicon.load(MPQASubjectivityLexiconPath)

  def load(pathToConfig: String) = {
    val configXml = XML.loadFile(pathToConfig)

    //val datasetRootDir = (configXml \\ "RootDir").text.trim

    val id2Comments = (configXml \\ "Comment").map(x ⇒ {
      val id = (x \ "@id").text.trim
      val content = x.text.trim
      (id, content)
    }).toMap

    (configXml \\ "Dataset").foreach(x ⇒ {
      val name = (x \ "@name").text.trim
      val version = (x \ "@version").text.trim
      val year = (x \ "@year").text.trim
      val commentId = (x \ "@commentId").text.trim
      val comment = id2Comments.getOrElse(commentId, "")
      val path = (x \ "Location").text.trim
      val d = Dataset(name, version, year, comment, path)
      corpusDirs.put((x \ "@name").text.trim, d)
    })

    val bp = 0
  }


}
