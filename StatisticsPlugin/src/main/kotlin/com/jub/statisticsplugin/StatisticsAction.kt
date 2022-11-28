package com.jub.statisticsplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilBase
import com.intellij.psi.util.elementType
import com.intellij.vcs.log.Hash
import com.intellij.vcs.log.VcsFullCommitDetails
import com.intellij.vcs.log.data.VcsLogMultiRepoJoiner
import com.intellij.vcs.log.impl.VcsProjectLog
import groovy.lang.Tuple2
import java.util.Date
import kotlin.concurrent.thread


class StatisticsAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val description = event.presentation.description
        val icon = Messages.getInformationIcon()

        val project = event.project ?: throw RuntimeException("Event project is null")
        val editor = event.getData(CommonDataKeys.EDITOR) ?: run {
            Messages.showMessageDialog(
                project,
                "You don't have a cursor on the file",
                description,
                icon
            )
            return
        }
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
            ?: throw RuntimeException("Event virtual file is null")

        val counterMethods = getNumberOfMethods(project, editor)

        val (counterLocalVariables, methodName) = getNumberOfLocalVariablesAndMethodName(project, editor, virtualFile)

        val (author, timestamp) = getLastAuthorAndTimestamp(project, virtualFile)

        val message = "Methods in this file: $counterMethods${System.lineSeparator()}" +
                "Local variables in the method=$methodName (where the cursor): $counterLocalVariables${System.lineSeparator()}" +
                "Last commit author and timestamp of this file: $author $timestamp"

        Messages.showMessageDialog(
            project,
            message,
            description,
            icon
        )
    }

    private fun getNumberOfMethods(project: Project, editor: Editor): Int {
        val psiFile = PsiUtilBase.getPsiFileInEditor(editor, project)
            ?: throw RuntimeException("Event psi file is null")

        var counterMethods = 0
        psiFile.accept(object : JavaRecursiveElementVisitor() {
            override fun visitMethod(method: PsiMethod?) {
                counterMethods++
            }
        })
        return counterMethods
    }

    private fun getNumberOfLocalVariablesAndMethodName(
        project: Project,
        editor: Editor,
        virtualFile: VirtualFile
    ): Tuple2<Int, String?> {
        val psiElement = PsiManager.getInstance(project).findFile(virtualFile)?.findElementAt(editor.caretModel.offset)
        val psiMethod = PsiTreeUtil.findFirstParent(psiElement) { element ->
            element.elementType.toString() == "METHOD"
        } as PsiMethod?

        var counterLocalVariables = 0
        psiMethod?.accept(object : JavaRecursiveElementVisitor() {
            override fun visitLocalVariable(variable: PsiLocalVariable?) {
                counterLocalVariables++
            }
        })
        return Tuple2(counterLocalVariables, psiMethod?.name)
    }

    private fun getLastAuthorAndTimestamp(project: Project, virtualFile: VirtualFile): Tuple2<String?, Date?> {
        var author: String? = null
        var timestamp: Date? = null
        thread {
            VcsProjectLog.getLogProviders(project)
                .map { (root, logProvider) ->
                    val commitsMetadata = logProvider.readFirstBlock(root) { Integer.MAX_VALUE }.commits
                    val commits = mutableListOf<VcsFullCommitDetails>()
                    logProvider.readFullDetails(root, commitsMetadata.map { it.id.toString() }) {
                        commits.add(it)
                    }
                    return@map commits.filter { details ->
                        details.changes.any { change ->
                            change.virtualFile == virtualFile
                        }
                    }
                }
                .let(VcsLogMultiRepoJoiner<Hash, VcsFullCommitDetails>()::join)
                .maxWithOrNull(compareBy { it.timestamp })
                ?.let {
                    author = it.author.toString()
                    timestamp = Date(it.timestamp)
                }
        }.join()
        return Tuple2(author, timestamp)
    }
}
