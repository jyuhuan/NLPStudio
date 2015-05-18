package nlpstudio.resources.bethardeventrel

import java.io.File

import nlpstudio.io.dataset.DatasetManager

import scala.xml.XML

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/17/15.
 */
object BethardEventRelation {

  /**
   * Extracts the section ID and file ID from the Penn Treebank file name.
   * Eg: "wsj_0112" will result in (1, 12), where:
   * <ul>
   *   <li> 1 is the section ID (WSJ part has sections 00 to 24). </li>
   *   <li> 12 refers to the file in section 1 (section 1 has files 00 to 99). </li>
   * </ul>
   * @param ptbFileName A string like "wsj_0112"
   * @return A pair of the form (section ID, file ID)
   */
  def parsePtbFileName(ptbFileName: String): (Int, Int) = {
    val digits = ptbFileName.substring(4, ptbFileName.length)
    (digits.substring(0, 2).toInt, digits.substring(2, 4).toInt)
  }

  def load(dir: String): Array[BethardEventRelationEntry] = {
    val dataXml = XML.loadFile(dir + File.separator + "treebank-verb-conj-anns.xml")

    //val ptb = DatasetManager.PennTreebankWSJPart

    val annotations = dataXml \\ "link"

    for (a ← annotations) yield {
      // Annotation ID
      val id = (a \ "@id").text.toInt

      // Extract the causal and temporal labels
      val labels = a \\ "label"
      val causalLabelText = labels.head.text
      val temporalLabelText = labels.last.text

      val causalLabel = if (causalLabelText == "UNRELATED") BethardCausalLabels.Unrelated else BethardCausalLabels.Causal
      val temporalLabel = if (temporalLabelText == "OVERLAP") BethardTemporalLabels.Overlap else if (temporalLabelText == "BEFORE") BethardTemporalLabels.Before else BethardTemporalLabels.After

      // Extract the two events
      val annotations = a \\ "annotation"

      // Extract event 1
      val e1Info = annotations.head
      val e1PtbFileName = (e1Info \ "@file").text
      val (e1SectionID, e1FileId) = parsePtbFileName(e1PtbFileName)
      val e1WordId = (e1Info \ "@leaf").text.toInt
      val e1SentenceId = (e1Info \ "@sentence").text.toInt

      // Extract event 2
      val e2Info = annotations.last
      val e2PtbFileName = (e2Info \ "@file").text
      val (e2SectionID, e2FileId) = parsePtbFileName(e2PtbFileName)
      val e2WordId = (e2Info \ "@leaf").text.toInt
      val e2SentenceId = (e2Info \ "@sentence").text.toInt

      new BethardEventRelationEntry(id, causalLabel, temporalLabel, e1SectionID, e1FileId, e1SentenceId, e1WordId, e2SectionID, e2FileId, e2SentenceId, e2WordId)
    }
  }.toArray
}
