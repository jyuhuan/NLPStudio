package nlpstudio.tools.wordnet
import scala.collection.JavaConversions._

import java.io.File
import java.net.URL

import edu.mit.jwi.Dictionary
import edu.mit.jwi.item._
import nlpstudio.core._
import nlpstudio.core.GlobalCodebooks._


/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 5/16/15.
 */
class WordNet(wordNetHome: String) {
  val path = wordNetHome + File.separator + "dict"
  val url = new URL("file", null, path)
  val dict = new Dictionary(url)
  dict.open()

  private def pos2pos(pos: WordNetPartOfSpeech): POS = {
    if (pos == WordNetPartOfSpeech.Noun) POS.NOUN
    else if (pos == WordNetPartOfSpeech.Noun) POS.NOUN
    else if (pos == WordNetPartOfSpeech.Noun) POS.NOUN
    else if (pos == WordNetPartOfSpeech.Noun) POS.NOUN
    else if (pos == WordNetPartOfSpeech.Noun) POS.NOUN
    else null
  }

  def hypernymsOf(word: Int, partOfSpeech: WordNetPartOfSpeech): Array[Phrase] = {
    val wordnetPos = pos2pos(partOfSpeech)

    val indexWord = dict.getIndexWord(cb(word), wordnetPos)
    val wordId = indexWord.getWordIDs.head
    val wordnetWord = dict.getWord(wordId)
    val synset = wordnetWord.getSynset
    val hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM)

    hypernyms.flatMap(h ⇒ dict.getSynset(h).getWords.map(w ⇒ {
      val lemma = w.getLemma
      val words = lemma.split('_').map(w ⇒ cb(w))
      Phrase(words)
    })).toArray
  }

  def hyponymsOf(word: Int, partOfSpeech: WordNetPartOfSpeech): Array[Phrase] = {
    val wordnetPos = pos2pos(partOfSpeech)

    val indexWord = dict.getIndexWord(cb(word), wordnetPos)
    val wordId = indexWord.getWordIDs.head
    val wordnetWord = dict.getWord(wordId)
    val synset = wordnetWord.getSynset
    val hypernyms = synset.getRelatedSynsets(Pointer.HYPONYM)

    hypernyms.flatMap(h ⇒ dict.getSynset(h).getWords.map(w ⇒ {
      val lemma = w.getLemma
      val words = lemma.split('_').map(w ⇒ cb(w))
      Phrase(words)
    })).toArray
  }

  def synonymsOf(word: Int, partOfSpeech: WordNetPartOfSpeech): Array[Phrase] = {
    val wordnetPos = pos2pos(partOfSpeech)

    val indexWord = dict.getIndexWord(cb(word), wordnetPos)
    val wordId = indexWord.getWordIDs.head
    val wordnetWord = dict.getWord(wordId)
    val synset = wordnetWord.getSynset

    synset.getWords.map(w ⇒ {
      val lemma = w.getLemma
      val words = lemma.split('_').map(w ⇒ cb(w))
      Phrase(words)
    }).toArray
  }
}
