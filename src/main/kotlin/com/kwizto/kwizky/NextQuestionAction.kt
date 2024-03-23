package com.kwizto.kwizky

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ModalTaskOwner.project
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import io.ktor.util.reflect.*
import org.jetbrains.annotations.NotNull
import javax.swing.JLabel


class NextQuestionAction: AnAction() {
    override fun update(@NotNull event: AnActionEvent) {
        // Using the event, evaluate the context,
        // and enable or disable the action.
    }

    override fun actionPerformed(event: AnActionEvent) {
        // Using the event, create and show a dialog

        val currentProject: Project? = event.project
        val toolWindow: ToolWindow? = currentProject?.let { ToolWindowManager.getInstance(it).getToolWindow("Kwizky") }
        toolWindow?.let {
            KwizkyToolWindowFactory.updateToolWindowContent(it)
        }
//        Messages.showMessageDialog(
//            currentProject,
//            "hello world",
//            "helloworld",
//            Messages.getInformationIcon()
//        )
    }
}