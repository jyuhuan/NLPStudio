## Penn Treebank Reader
Each tree in the Penn Treebank (PTB) is represented as a `PennTreebankEntry`, which consists of
`PennTreebankNodes`. 

### Class `PennTreebankNode`
A `PennTreebankNode` represents a node in a parse tree. There are two types of nodes: 

1.	An **internal node** represents a non-terminal symbol, which subsumes one or more consecutive words. It is sometimes also referred to as a **constituent**. A constituent is mainly labeled by its **syntactic category**, including `S`, `NP`, `VP`, `ADJP`, etc.). Besides these usual syntactic category labels, each internal node can have some additional data:

	*	One or more **functional labels**, which mark the function of the constituent. For example, a node marked by `LGS` means that it is the **l**o**g**ical **s**ubject of the predicate in the tree, and `BNF` marks an internal node as playing the semantic role of a **b**e**n**e**f**iciary. In the original PTB annotation, functional labels are connected to the a constituent's syntactic category with a hyphen: `NP-LGS-BNF`.  
	*	A **co-index**, which is an integer number used to mark the constituent when
	
		*	a **null word** somewhere else in the tree is **co-indexing** with it. That null word should have the same co-index as this constituent's. See description of word movements (below) for examples.
		* 	a constituent somewhere else in the tree is mapping to it due to **gapping**. The constituent being mapped to this constituent should be labelled with a **gap-mapping index**. 
		* 	a constituent somewhere else in the tree is paired with it via **conjunction**. The corresponding constituents between the two conjunctions are marked with increasing indices. 
		
		In the original PTB annotation, a co-index is connected to a constituent's syntactic category with a hyphen: `NP-SBJ-2`. 
	*	A **gap-mapping index**, which marks a constituent when it should be mapped to another constituent due to gapping. An example is shown below:
	
		<img src="http://yuhuan.me/articles/img/ptb-explained/gapping-mapping.png" width="600" />
	
		**Figure 1**: A mapping of gappping. the `NP` for *Bill* maps to the  the `NP` for *Mary*, and the `NP` for *a pencil* maps to the `NP` for *a book*.
		
		In the original PTB annotation, a gap-mapping index is connected to a constituent's syntactic category with an equal sign, before the co-index: `NP-BNF=2-1`

2.	A **word node** represents a word in the parse tree. Notice that PTB comes with two versions of trees. One is without the part-of-speech (POS) tags. The other has POS tags one level right above the words. The difference is shown in the following figure.
	
	<img src="http://yuhuan.me/articles/img/ptb-explained/pos-vs-nopos.png" width="550" />
	
	**Figure 2**: Two types of tree annotations found in *PennTreebank*.  One with part-of-speech labels (right), the other without them (left).
	
	This library uses the latter type. Thus a word node contains both the word itself and the POS tag. Besides theses, a word node may also contain: 
	
	*	A **co-index** pointing to the constituent marked with the same co-index. The whole idea of co-indexing is used for constituent movements. Three major cause of movements are **<i>wh-</i> questions**, **topicalization**, and **passive voices**. Examples are shown below: 
	
	<img src="http://yuhuan.me/articles/img/ptb-explained/wh-move.png" width="500" />
	
	**Figure 3**: *Wh-* movement. 
	
	<img src="http://yuhuan.me/articles/img/ptb-explained/topicalization-move.png" width="600" />
	
	**Figure 4**: Topicalization. 
	
	In the original PTB annotation, the co-index is connected to the word with a hyphen.

#### Properties & Methods
One thing to notice is that, unless said otherwise, when the property does not exist (e.g., when you want to see the `posTag` of an internal node), `null` is returned. An exception is avoided because catching exception is a headache, and we want the user client code to be simple.

Use `isWord` to check whether the node is an internal node (i.e., a non-terminal like `NP`, `S`, `ADJP`, etc.) or a leaf node (a terminal, like `NNP/John`, `JJ/good`, etc.) 

##### General Properties & Methods
###### Perperties
For any node (either internal or leaf), the following properties are meaningful:

*	`depth`: The depth of the node in the tree. The root node (usually *S*) has depth 0. The depth of a child is always 1 greater than the parent.
*	`parentNode`: The parent node. If no parent is found (i.e., when this node is already the root, `null` is returned. 
*	`childrenNodes`: The children nodes. Similar to the difference of `parent` and `parentNode`, `childrenNodes` returns directly an `ArrayBuffer` of `PennTreebankNode`s, while `children` returns an `ArrayBuffer` of `Node[String]`s. If no children is found, the returned value should be an empty `ArrayBuffer[PennTreebankNode]` rather than `null`. 
*	`indexInSiblings`: The index of this node among all siblings, starting from 0. If there is not parent, `-1` is returned. For example, in the following figure, 
	
	<center><img src="http://yuhuan.me/articles/img/ptb-explained/sibling-indices.png" width="200" /></center>
	
	the node with syntactic category `CC` is the second among its siblings. Thus its `indexInsiblings` is `1`. For the `NP` at the top, its `indexInSiblings` is `-1`. 

* 	`syntacticHead`: Syntactic head of the node (still a node object). Does not go all the way to word nodes. Only returns the head that is an immediate child of this node. When called on a leaf node, it simply returns the leaf node itself. When no head is found (e.g., when the children of this node are all empty elements), `null` is returned. (The implementation is based on Michael Collins' thesis.)
*	`syntacticHeadWord`: Syntactic head word of the node (the node of that word actually). `syntacticHead` is called multiple times to find the word node. When no head is found `null` is returned.
*	`semanticHead`: Semantic head word of the node. (Implementation is from Matthrew Gerber's open source C# PennTreebank reader.) 
*	`rule`: The context-free grammar rule that expanded the node. 
* 	`isWord`: Whether the node is a word node. 
*  	`isNullElement`: For a leaf node, if its part-of-speech label is "-NONE-", then it is considered a null element. For an internal node, if all its children are null element, it itself is considered a null element. (Implementation is from Matthrew Gerber's open source C# PennTreebank reader.)
*   `isPassive`: Test whether the word is a passive verb. (Implementation is from Matthrew Gerber's open source C# PennTreebank reader.)
*	`leftSiblings`: All siblings to the left of this node. If this node has no left siblings, an empty `ArrayBuffer[PennTreebankNode]` is returned instead of `null`.
* 	`leftSibling`: The sibling node immediately to the left of this node. If no immediate left sibling is found, `null` is returned.
*  	`rightSiblings`: Similar to `leftSiblings`
*   `rightSibling`: Similar to `leftSibling`
*	`ancestors`: An array of ancestors of this node (in bottom-up order).
* 	`root`: The root of the tree where this node resides.
*  	`prevWordNode`: The word node that is to the immediate left of the constituent that this node represents, if this node is an internal node; or simply the word before this node, if this node is a leaf node. 
*   `nextWordNode`: Similar to `prevWordNode`.
 

###### Methods
*	`apply(idx: Int)`: A short-hand to get the child at index `idx`. 
*   `rightMost(isGoal: PennTreebankNode ⇒ Boolean)`: Finds the right most node that makes the given predicate true.
*   `lowestCommonAncestor(that: PennTreebankNode)`: Finds the lowest common ancestor of this node and `that` node.
*   `isRightAdjacentTo(condition: PennTreebankNode ⇒ Boolean)`: Finds a node that is adjacent to this node from the right. Can be used to find either an internal node or a word node.
*   `isLeftAdjacentTo(condition: PennTreebankNode ⇒ Boolean)`: Finds a node that is adjacent to this node from the right. Can be used to find either an internal node or a word node.
*   `isBefore(that: PennTreebankNode)`: Test if this node lies before `that` node. An example is shown below:
*   `pathTo(isGoal: PennTreebankNode => Boolean)`: Finds the path to the node that makes the given predicate true. The path is a string of the form: `"NNP↑NP↑S↓VP↓VP↓VB"`. An example is given below:

##### Special Properties

The following properties are only meaningful to **internal** nodes.

*	`syntacticCategory`: The syntactic category of the node. This does not contain functional labels (e.g., `BNF`, `CLR`), co-indices (e.g., the number `2` in `NP-SBJ-2`), or gap-mapping index (e.g., the number `1` in `NP-BNF=1`). 
*	`functionalLabels`: The functional labels (e.g., `BNF`, `CLR`)
*	`allNodes`: All nodes subsumed under this node, including internal and leaf nodes. 
* 	`wordNodes`: All leaf nodes subsumed under this node. 
*  	`words`: All words (`String`s) subsumed under this node. 
*   `firstWordNode`: The node of the first word subsumed by this node.
*   `firstWord`: The first word (as a `String`) subsumed by this node.
*   `firstPos`: The part-of-speech label of the first word (as a `String`) subsumed by this node.
*   `lastWordNode`: Similar to `firstWordNode`
*   `lastWord`: Similar to `firstWord`
*   `lastWordPos`: Similar to `firstPos` 


The following properties are only meaningful to **leaf** nodes.

*	`surface`: The surface of the word, i.e., the word as it is in the corpus. 
*	`wordIndex`: The index of the word in the sentence, starting form 0.
* 	`posTag`: The part-of-speech tag of the word. 

##### A Note for Curious Users

There are some methods (defined using the `def` keyword) that you find unmentioned above, namely, `data`, `isLeaf`, `parent`, `children`, `leaves`. They are inherited from the parent trait `Node[String]`. 

*	`data` is simply set to be the same as `content`. If the user tries to call `data`, he/she actually gets the value of the variable `content` (discussed below) at that time. 
* 	`isLeaf` is already implemented in the trait `Node[String]`. The class `PennTreebankNode` exposes instead a property named `isWord`. A simple change, a largely more straightforward name. 
*	`parent`'s return value, as defined in the trait, should be of type `Node[String]`. But the client user of my `PennTreebankNode` will want just a `PennTreebankNode` object to be returned. Of course, one may also take a detour to get the parent node by calling `parent` on a `PennTreebankNode` object, and then cast the result to `PennTreebankNode` using `.parent.asInstanceOf[PennTreebankNode]`.
* 	`children` is similar to `parent`, but there is a technical problem that prevents client users to use this. They are more likely to want `ArrayBuffer[PennTreebankNode]` rather than `ArrayBuffer[Node[String]]`. However, `ArrayBuffer[T]` is a mutable type. Therefore, its type parameter `T` is invariant, rendering `ArrayBuffer[PennTreebankNode]` not being a subclass of `ArrayBuffer[Node[String]]`, even though `PennTreebankNode` is a subclass of `Node[String]`. 
*  	`leaves` returns all leaf nodes, just like what `wordNodes` does. For the same reason as `children`, I do not suggest users to access the leaf word nodes via this property. 

There is one more thing unmentioned, `content`. The variable `content` is not exposed to user because it will confuse the user. The confusion comes from the fact that a `PennTreebankNode` can sometimes be an internal node, and sometimes a leaf. When the node is an internal node, the `content` variable holds the syntactic category. When the node is a leaf, the `content` variable holds the surface word. Users are recommended to use the properties `syntacticCategory` and `surface` to access these two information respectively. 

#### FAQ
##### How May `PennTreebank` Facilitate Cross Validation?
A convenient method is exposed for users to split the PTB by sections, in a conventional way that many papers do. 

### Limitations
Currently, the PTB module in this project is only tested with the wsj part. 
