## NLPStudio


## Corpus Management
Do you hate writing a corpus reader everytime you start an NLP project? 
Have you ever forgotten how you segmented the source language in a machine translation experiment a 
few months ago? 
Do you ever want to manage the corpora (perhaps of different versions, different 
segmentation specifications, different case conversions) that you have in a unified way? 

If the answers are "yes", you're in need of **NLPStudio**, a general framework for NLP needs.


## What Does It Do?
* Corpus Reading (Current)
    * *PennTreebank* (See `nlpstudio/resources/penntreebank`)
    * *NomBank* (See `nlpstudio/resources/nombank`)
    * *PropBank* (Not released yet)
    * *VerbBank* (Not released yet)
    * *WordNet* (Not released yet)
    * Subjectivity Lexicons (See `nlpstudio/resources/subjectivitylexicons`)
    
* Corpus Management (Future)
    * Manage by metadata (e.g., segmentation tool, version, capitalization style, ...)
    * Corpora Library (similar to media libraries in music players)

* Common Algorithms (Current)
    * Wrappers
        * Stanford Word Segmentation Tool
        * Stanford Parser
        * Stanford Part-of-Speech Tagger
    * Own Implementation
        * Topic Modeling Tools

* Experiment Project Management (Future)
    * Manage experiment pipeline by project profiles
    * Maximized reusability


## Need a Name!
The name *NLPStudio* is only temporary. Maybe I'll think of something better in the future, but I'd
like to call it *NLPStudio* now, just to pay tribute to the almighty **Microsoft Visual Studio**.
