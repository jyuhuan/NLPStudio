package nlpstudio.resources.subjectivelexicons

import nlpstudio.io.files.TextFile

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 4/3/15.
 */
object MpqaSubjectivityLexicon {
  def parseSingleEntry(s: String):MpqaSubjectivityLexiconEntry = {
    val parts = s.split(' ')
    val result = parts.map(x ⇒ {
      val keyAndValue = x.split('=')
      keyAndValue(0) → keyAndValue(1)
    }).toMap

    val intensity = if (result("type") == "weaksubj") SubjectivityIntensity.Weak else SubjectivityIntensity.Strong
    val word = result("word1")
    val partOfSpeech = result("pos1")
    val polarity = if (result("priorpolarity") == "positive") SubjectivityPolarity.Positive else SubjectivityPolarity.Negative
    MpqaSubjectivityLexiconEntry(intensity, word, partOfSpeech, polarity)
  }

  def load(path: String): Array[MpqaSubjectivityLexiconEntry] = {
    val lines = TextFile.readLines(path)
    (for (line ← lines) yield parseSingleEntry(line)).toArray
  }

  /**
   * Reads the MPQA Subjectivity Lexicon as a dictionary, in which a key is a pair of (word, POS tag),
   * and a value is the polarity.
   * @param path Where the MPQA Subjectivity Lexicon file is. The file name should be
   *             "subjclueslen1-HLTEMNLP05.tff".
   * @return A map that maps a pair of (word, POS tag) to polarity. To look up for polarr
   */
  def loadAsSimpleLexicon(path: String): Map[(String, String), SubjectivityPolarity] = {
    load(path).map(x ⇒ (x.word, x.partOfSpeech) → x.polarity).toMap
  }

}
