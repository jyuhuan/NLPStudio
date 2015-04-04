## Penn Treebank Reader
Each Penn Treebank (PTB) tree is represented as a `PennTreebankTree`, which consists of
`PennTreebankNodes`. 

### Class `PennTreebankNode`
A `PennTreebankNode` represents a node in the PTB parse tree. There are two types of nodes: 

1.	An **internal node** represents a non-terminal symbol (or, **constituent**) in the parse tree (i.e., the **syntactic category** labels like `S`, `NP`, `VP`, `ADJP`, etc.). Besides these usual syntactic category labels, each internal node can have some additional stuffs including:

	*	One or more **functional labels**, which mark the function of the constituent. For example, a node marked by `LGS` means that it is the **l**o**g**ical **s**ubject of the predicate in the tree, and `BNF` marks an internal node as playing the semantic role of a beneficiary. In the original PTB annotation, functional labels are connected to the a constituent's syntactic categorie with a hyphen: `NP-LGS-BNF`.  
	*	A **co-index**, which is an integer number used to mark the constituent when
	
		*	a **null word** somewhere else in the tree is **co-indexing** with it.
		* 	a constituent somewhere else in the tree is mapping to it due to **gapping**. 
		* 	a constituent somewhere else in the tree is paired with it via **conjunction**. 
		
		In the original PTB annotation, a co-index is connected to a constituent's syntactic category with a hyphen: `NP-SBJ-2`. 
	*	A **gap-mapping index**, which marks a constituent when it should be mapped to another constituent due to gapping. An example is shown below:
	
	<center><img src="http://yuhuan.me/articles/img/ptb-explained/gap-mapping.png" width="600" /></center>
	<center> **Figure 1**: A mapping of gappping. the `NP` for *Bill* maps to the  the `NP` for *Mary*, and the `NP` for *a pencil* maps to the `NP` for *a book*. </center>

2.	A **word node** represents a word in the parse tree. Notice that PTB comes with two versions of trees. One is without the part-of-speech (POS) tags. The other has POS tags one level right above the words. The difference is shown in the following figure.
	
	<center><img src="http://yuhuan.me/articles/img/ptb-explained/pos-vs-no-pos.png" width="500" /></center>
	<center> **Figure 2**: Two types of tree annotations found in *PennTreebank*.  One with part-of-speech labels (right), the other without them (left).</center>
	
	This library uses the latter type. Thus a word node contains both the word itself and the POS tag. Besides theses, a word node may also contain: 
	
	*	A **co-index** pointing to the constituent marked with the same co-index. The whole idea of co-indexing is used for constituent movements. Three major cause of movements are **<i>wh-</i> questions**, **topicalization**, and **passive voices**. Examples are shown below: 
	
	<center><img src="http://yuhuan.me/articles/img/ptb-explained/wh-movement.png" width="350" /></center>
	<center> **Figure 3**: *Wh-* movement. </center>
	
	<center><img src="http://yuhuan.me/articles/img/ptb-explained/topicalization.png" width="500" /></center>
	<center> **Figure 4**: Topicalization. </center>


Use `isLeaf` to check whether the node is an internal node (i.e., a non-terminal like `NP`, `S`, `ADJP`, etc.) or a leaf node (a word, or a terminal, like `NNP/John`, `JJ/good`, etc.) For any node (either internal or leaf), the following fields are meaningful:

*	`depth`: The depth of the node in the tree. The root node (usually *S*) has depth 0. The depth of a child is always 1 greater than the parent.
*	`parentNode`: The parent node. This differs from the inherited field `parent` in that `parent` returns the base trait `Node[String]` while `parentNode` returns a `PennTreebankNode` object. One may get the parent node by calling `parent` on a `PennTreebankNode` object, and then cast result to `PennTreebankNode` using `.parent.asInstanceOf[PennTreebankNode]`. 
*	`childrenNodes`: The children nodes. Similar to the difference of `parent` and `parentNode`, `childrenNodes` returns directly an `ArrayBuffer` of `PennTreebankNode`s, while `children` returns an `ArrayBuffer` of the trait `Node[String]`. 
*	`syntacticCategory`: The syntactic category of the node. This does not contain functional tags (e.g., `BNF`, `CLR`) or any co-indices (e.g., the number `2` in `NP-SBJ-2`). For an internal node (a non-terminal), this is the syntactic category itself. For a leaf node (a word), it is the POS tag of the word. 
*	`indexInSiblings`: The index of this node among all siblings, starting from 0. For example, in the following figure, 
	
	<center><img src="http://yuhuan.me/articles/img/ptb-explained/sibling-indices.png" width="200" /></center>
	
	the node with syntactic category `CC` is the second among its siblings. Thus its `indexInsiblings` is `1`.

The following fields are only meaningful to leaf nodes.

*	`wordIndex`: The index of the word in the sentence, starting form 0.
* 	(to be finished...)

Currently, the PTB module in this project is tested with the wsj part. 
