package nlpstudio.visualization.core

import javax.swing.JFrame

import com.mxgraph.layout.mxCompactTreeLayout
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import foundation.math.graph.Graph

import scala.collection.mutable

/**
 * Created by Yuhuan Jiang (jyuhuan@gmail.com) on 2/7/15.
 */
class GraphViewer[TVertex, TEdge](graph: Graph[TVertex, TEdge]) extends JFrame {

  var drawingWidth = 400
  var drawingHeight = 400

  {
    val graphView = new mxGraph()
    val graphViewParent = graphView.getDefaultParent
    graphView.getModel.beginUpdate()
    try {

      var verticesCreated = mutable.HashMap[TVertex, Object]()
      for ((parent, childrenAndLabels) ← graph.childTable) {
        var parentVertex: Object = null
        if (verticesCreated contains parent) {
          parentVertex = verticesCreated(parent)
        }
        else {
          parentVertex = graphView.insertVertex(graphViewParent, null, parent, 10, 10, 80, 30)
          verticesCreated += (parent → parentVertex)
        }

        for ((child, label) ← childrenAndLabels) {
          var childVertex: Object = null
          if (verticesCreated contains child) {
            childVertex = verticesCreated(child)
          }
          else {
            childVertex = graphView.insertVertex(graphViewParent, null, child, 10, 10, 80, 30)
            verticesCreated += (child → childVertex)
          }
          graphView.insertEdge(graphViewParent, null, label, parentVertex, childVertex)
        }
      }
    }
    finally {
      graphView.getModel.endUpdate()
    }

    graphView.setAutoSizeCells(true)

    val layout = new mxCompactTreeLayout(graphView)
    layout.setHorizontal(false)
    layout.setLevelDistance(50)
    layout.setEdgeRouting(false)
    layout.execute(graphView.getDefaultParent)

    val graphComponent = new mxGraphComponent(graphView)
    getContentPane.add(graphComponent)

    drawingWidth = graphView.getGraphBounds.getWidth.toInt + 40
    drawingHeight = graphView.getGraphBounds.getHeight.toInt + 50

  }

  def showWindow() {
    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(drawingWidth, drawingHeight)
    setVisible(true)
  }
}
