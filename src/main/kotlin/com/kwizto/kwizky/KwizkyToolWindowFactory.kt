package com.kwizto.kwizky
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.Box

data class Card(
    var questionString: String,
    var answerString:String
)
class KwizkyToolWindowFactory:ToolWindowFactory,DumbAware {
companion object {
    val contentOuterPanel = JPanel()
    val contentPanel = JPanel()
    val questionText = JLabel()
    val questionAnswerSeparator = JSeparator(JSeparator.HORIZONTAL)

    var currentCardCount = 0
    var cardArray = arrayOf(
        Card("one q","one a"),
        Card("two q","two a"),
        Card("three q","three a"),
        Card("four q","four a")
        )
    fun updateContentOfOuterPanel(toolWindow:ToolWindow,questionLabel: JLabel?,answerLabel:JLabel?,mode:String){
        val content = toolWindow.contentManager.contents[0].component as? JPanel
        content?.let {
            it.removeAll()
            val contentInnerPanel = JPanel()
            contentInnerPanel.setLayout(BoxLayout(contentInnerPanel, BoxLayout.Y_AXIS))

            contentInnerPanel.add(questionLabel)
            if(mode == "questionAnswer") {
                contentInnerPanel.add(Box.createVerticalStrut(20))
                contentInnerPanel.add(answerLabel)
            }
            it.add(contentInnerPanel)
            it.revalidate()
            it.repaint()
        }
    }
    fun showRequiredCard(toolWindow:ToolWindow, mode:String){
        when(mode){
            "next" -> currentCardCount++
            "previous" ->  currentCardCount--
        }
        if(mode == "initial"){
            val questionLabel = JLabel(cardArray[0].questionString)
            val answerLabel = JLabel(cardArray[0].answerString)
//            questionLabel.alignmentX = Component.CENTER_ALIGNMENT
//            answerLabel.alignmentX = Component.CENTER_ALIGNMENT
//            questionLabel.preferredSize = Dimension(200, questionLabel.preferredSize.height)
//            answerLabel.preferredSize = Dimension(200, answerLabel.preferredSize.height)
            contentOuterPanel.layout = FlowLayout(FlowLayout.CENTER)
            contentPanel.setLayout(BoxLayout(contentPanel, BoxLayout.Y_AXIS))
            contentPanel.add(questionLabel)
//            contentPanel.add(questionAnswerSeparator)
//            contentPanel.add(Box.createVerticalStrut(20))
//            contentPanel.add(answerLabel)
            contentOuterPanel.add(contentPanel)
        }
        else{
            if(currentCardCount >= cardArray.size)
                currentCardCount = 0
            if(currentCardCount < 0)
                currentCardCount = cardArray.size - 1
            val questionLabel = JLabel(cardArray[currentCardCount].questionString)
            val answerLabel = JLabel(cardArray[currentCardCount].answerString)
//            questionLabel.alignmentX = Component.CENTER_ALIGNMENT
//            answerLabel.alignmentX = Component.CENTER_ALIGNMENT
//            questionLabel.preferredSize = Dimension(200, questionLabel.preferredSize.height)
//            answerLabel.preferredSize = Dimension(200, answerLabel.preferredSize.height)
            println("in update tool window showing below string")
            println(cardArray[currentCardCount].questionString)
            updateContentOfOuterPanel(toolWindow,questionLabel,answerLabel,"question")

        }

    }
    fun showAnswer(toolWindow:ToolWindow){
        val questionLabel = JLabel(cardArray[currentCardCount].questionString)
        val answerLabel = JLabel(cardArray[currentCardCount].answerString)
        updateContentOfOuterPanel(toolWindow,questionLabel,answerLabel,"questionAnswer")
    }
    fun updateToolWindowContent(toolWindow: ToolWindow,mode:String) {
        // Modify the content of your tool window
        //First Time
        if (mode == "next" || mode == "previous" || mode == "initial"){
            showRequiredCard(toolWindow,mode)
        }
        if(mode == "showAnswer"){
            showAnswer(toolWindow)
        }

    }

    fun getToolWindowContentPanel():JPanel{
        return contentOuterPanel
    }
}
    // Register the action with IntelliJ's action system
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val nextQuestionAction = NextQuestionAction()
        val actionList = mutableListOf<AnAction>()
        actionList.add(nextQuestionAction)
        toolWindow.setTitleActions(actionList)
        KwizkyToolWindowFactory.updateToolWindowContent(toolWindow,"initial")
        val toolWindowContentPanel = KwizkyToolWindowFactory.getToolWindowContentPanel()
        val content = ContentFactory.getInstance().createContent(toolWindowContentPanel,"",false)
        toolWindow.contentManager.addContent(content)
    }
}

//private class TestToolWindowContent{


//    constructor(){
//        KwizkyToolWindowFactory.updateToolWindowContent()
//    }
//    fun getWindowContentPanel():JPanel{
//        return contentPanel
//    }
//}