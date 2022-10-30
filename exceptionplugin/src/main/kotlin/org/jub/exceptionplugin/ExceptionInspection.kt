package org.jub.exceptionplugin

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiCallExpression
import com.intellij.psi.PsiElementVisitor

class ExceptionInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitCallExpression(callExpression: PsiCallExpression?) {
                if (callExpression == null) {
                    return
                }
                if (callExpression.parent.text.startsWith("throw")) {
                    holder.registerProblem(callExpression, "This is an exception!")
                }
            }
        }
    }
}