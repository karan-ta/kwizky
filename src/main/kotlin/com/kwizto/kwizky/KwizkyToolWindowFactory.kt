package com.kwizto.kwizky
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.progress.ModalTaskOwner.project
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.FilenameIndex.processAllFileNames
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ArrayUtilRt
import java.awt.FlowLayout
import javax.swing.*
import com.intellij.util.containers.CollectionFactory;
import kotlin.io.path.Path

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
    var cardList = mutableListOf<Card>()
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
            val questionLabel = JLabel(cardList[0].questionString)
            val answerLabel = JLabel(cardList[0].answerString)
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
            if(currentCardCount >= cardList.size)
                currentCardCount = 0
            if(currentCardCount < 0)
                currentCardCount = cardList.size - 1
            val questionLabel = JLabel(cardList[currentCardCount].questionString)
            val answerLabel = JLabel(cardList[currentCardCount].answerString)
//            questionLabel.alignmentX = Component.CENTER_ALIGNMENT
//            answerLabel.alignmentX = Component.CENTER_ALIGNMENT
//            questionLabel.preferredSize = Dimension(200, questionLabel.preferredSize.height)
//            answerLabel.preferredSize = Dimension(200, answerLabel.preferredSize.height)
            println("in update tool window showing below string")
            println(cardList[currentCardCount].questionString)
            updateContentOfOuterPanel(toolWindow,questionLabel,answerLabel,"question")

        }

    }
    fun showAnswer(toolWindow:ToolWindow){
        val questionLabel = JLabel(cardList[currentCardCount].questionString)
        val answerLabel = JLabel(cardList[currentCardCount].answerString)
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
    fun prepareCardsByReadingFile(project: Project){
//        val names: MutableSet<String> = CollectionFactory.createSmallMemoryFootprintSet()
//        processAllFileNames({ s: String ->
//            println("name of file is "+s)
//            names.add(s)
//            true
//        }, GlobalSearchScope.allScope(project), null)
//        val filePath = Path("/c/Users/KaranAhuja/works/android-kotlin-vault/kotlin-conditionals-loops.md")
//        println(filePath)
//        val cardDataFile = LocalFileSystem.getInstance().findFileByPath()
//        println(cardDataFile)
//        if (cardDataFile != null) {
//            VfsUtil.loadText(cardDataFile)
//        };
        val myFiles = FilenameIndex.getVirtualFilesByName("kotlin-conditionals-loops.md", GlobalSearchScope.allScope(project))
        println(myFiles)
        val fileContent = VfsUtil.loadText(myFiles.first())
        println(fileContent)
        val allLines = fileContent.lines()
        var questionString = ""
        var answerString = ""
        var isCardComplete = false
        var isQuestion = true
        var isAnswer = false
        allLines.forEach{

            when(it.trim()){
                "____" ->{
                    isAnswer = true
                    isQuestion = false
                    isCardComplete = false
                }
                "" -> {
                    isCardComplete = true
                    isQuestion = true
                    isAnswer = false
                }
            }
            if(isCardComplete){
                if(questionString != "" && answerString != "")
                cardList.add(
                    Card(questionString,answerString))
                isCardComplete = false
                questionString = ""
                answerString = ""

            }
            if(it != "" && it != "____" && !isCardComplete) {
                if (isQuestion)
                    questionString += it.trim()
                if(isAnswer)
                    answerString += it.trim()
            }
        }


        println(cardList)

    }
    // Register the action with IntelliJ's action system
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        prepareCardsByReadingFile(project)
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