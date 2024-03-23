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
    var currentCardCount = 0
    var cardArray = arrayOf(
        Card("one q","one a"),
        Card("two q","two a"),
        Card("three q","three a"),
        Card("four q","four a"),
        )
    fun updateToolWindowContent(toolWindow: ToolWindow) {
        if(currentCardCount >= cardArray.size )
            currentCardCount = 0

        // Modify the content of your tool window
        val updatedLabel = JLabel(cardArray[currentCardCount].questionString)
        val content = toolWindow.contentManager.contents[0].component as? JPanel
        println("in update tool window")
//        println(content?.components?.forEach { it.name })
        content?.let {
            it.removeAll()
            it.add(updatedLabel)
            it.revalidate()
            it.repaint()
        }
        currentCardCount++
    }
}
    // Register the action with IntelliJ's action system
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {


        val nextQuestionAction = NextQuestionAction()
        val actionList = mutableListOf<AnAction>()
        actionList.add(nextQuestionAction)
        toolWindow.setTitleActions(actionList)
        val toolWindowContent = TestToolWindowContent()
        val content = ContentFactory.getInstance().createContent(toolWindowContent.getWindowContentPanel(),"",false)
        toolWindow.contentManager.addContent(content)
    }
}

private class TestToolWindowContent{
    val contentPanel = JPanel()
    val questionText = JLabel()

    constructor(){
        contentPanel.name = "panel"
        questionText.text = "Hello World"
        contentPanel.add(questionText)
    }
    fun getWindowContentPanel():JPanel{
        return contentPanel
    }
}