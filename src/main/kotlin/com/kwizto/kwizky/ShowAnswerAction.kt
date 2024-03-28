package com.kwizto.kwizky
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager

class ShowAnswerAction: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val currentProject: Project? = event.project
        val toolWindow: ToolWindow? = currentProject?.let { ToolWindowManager.getInstance(it).getToolWindow("Kwizky") }
        toolWindow?.let {
            KwizkyToolWindowFactory.updateToolWindowContent(it,"showAnswer")
        }

    }

}