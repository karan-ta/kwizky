package com.kwizto.kwizky
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JLabel
import javax.swing.JPanel
data class Card(
    var questionString: String,
    var answerString:String
)
class KwizkyToolWindowFactory:ToolWindowFactory,DumbAware {
companion object {
    val contentPanel = JPanel()
    val questionText = JLabel()
    var currentCardCount = 0
    var cardArray = arrayOf(
        Card("one q","one a"),
        Card("two q","two a"),
        Card("three q","three a"),
        Card("four q","four a"),
        )
    fun updateToolWindowContent(toolWindow: ToolWindow) {
        // Modify the content of your tool window
        //First Time
        if(currentCardCount == 0){
            val questionLabel = JLabel(cardArray[0].questionString)
            val answerLabel = JLabel(cardArray[0].answerString)
            contentPanel.add(questionLabel)
            contentPanel.add(answerLabel)
        }
        else{
            if(currentCardCount >= cardArray.size)
                currentCardCount = 0
            val questionLabel = JLabel(cardArray[currentCardCount].questionString)
            val answerLabel = JLabel(cardArray[currentCardCount].answerString)
            val content = toolWindow.contentManager.contents[0].component as? JPanel
            println("in update tool window showing below string")
            println(cardArray[currentCardCount].questionString)
//        println(content?.components?.forEach { it.name })
            content?.let {
                it.removeAll()
                it.add(questionLabel)
                it.add(answerLabel)
                it.revalidate()
                it.repaint()
            }
        }

        currentCardCount++
    }

    fun getToolWindowContentPanel():JPanel{
        return contentPanel
    }
}
    // Register the action with IntelliJ's action system
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {


        val nextQuestionAction = NextQuestionAction()
        val actionList = mutableListOf<AnAction>()
        actionList.add(nextQuestionAction)
        toolWindow.setTitleActions(actionList)
        KwizkyToolWindowFactory.updateToolWindowContent(toolWindow)
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